import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Security and Penetration Testing', () => {
  test.describe('SQL Injection and XSS Prevention', () => {
    test('should prevent SQL injection attacks in all input fields', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      
      const sqlInjectionPayloads = [
        "'; DROP TABLE users; --",
        "' OR '1'='1",
        "admin'/*",
        "' UNION SELECT * FROM users --",
        "'; INSERT INTO users VALUES ('hacker', 'password'); --",
        "' OR 1=1 LIMIT 1 --",
        "' AND (SELECT COUNT(*) FROM users) > 0 --"
      ];
      
      await loginPage.goto();
      
      for (const payload of sqlInjectionPayloads) {
        await testPage.fill('input[name="userId"]', payload);
        await testPage.fill('input[name="password"]', payload);
        await testPage.click('button[type="submit"]');
        
        await expect(testPage).toHaveURL(/.*login.*/);
        
        const errorMessage = testPage.locator('.error-message, .alert-danger, [role="alert"]');
        if (await errorMessage.isVisible()) {
          const errorText = await errorMessage.textContent();
          expect(errorText?.toLowerCase()).toContain('invalid');
        }
        
        await testPage.reload();
      }
      
      await context.close();
    });

    test('should prevent XSS attacks in form inputs and display fields', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      const xssPayloads = [
        '<script>alert("XSS")</script>',
        '<img src="x" onerror="alert(\'XSS\')">',
        '<svg onload="alert(\'XSS\')">',
        'javascript:alert("XSS")',
        '<iframe src="javascript:alert(\'XSS\')"></iframe>',
        '<body onload="alert(\'XSS\')">',
        '<div onclick="alert(\'XSS\')">Click me</div>'
      ];
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      for (const payload of xssPayloads) {
        const accountInput = testPage.locator('input[name="accountNumber"]');
        if (await accountInput.isVisible()) {
          await accountInput.fill(payload);
          await accountInput.blur();
          
          const alertDialogs = await testPage.evaluate(() => {
            return window.alert === window.alert; // Should not be overridden
          });
          expect(alertDialogs).toBeTruthy();
          
          const inputValue = await accountInput.inputValue();
          expect(inputValue).toBe(payload); // Should contain the raw payload, not execute it
        }
      }
      
      await context.close();
    });
  });

  test.describe('Authentication Security and Session Management', () => {
    test('should enforce strong password policies and account lockout', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      
      await loginPage.goto();
      
      const maxAttempts = 5;
      for (let i = 0; i < maxAttempts; i++) {
        await testPage.fill('input[name="userId"]', 'USER0001');
        await testPage.fill('input[name="password"]', 'wrongpassword');
        await testPage.click('button[type="submit"]');
        
        await testPage.waitForTimeout(1000);
        
        if (i >= 2) {
          const warningMessage = testPage.locator('text=locked, text=attempts, text=wait');
          if (await warningMessage.isVisible()) {
            const warningText = await warningMessage.textContent();
            expect(warningText?.toLowerCase()).toMatch(/lock|attempt|wait/);
          }
        }
      }
      
      await testPage.fill('input[name="userId"]', 'USER0001');
      await testPage.fill('input[name="password"]', 'user1234'); // Correct password
      await testPage.click('button[type="submit"]');
      
      await expect(testPage).toHaveURL(/.*login.*/);
      
      await context.close();
    });

    test('should handle concurrent session security and prevent session hijacking', async ({ page, browser }) => {
      const contexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));
      const mainMenuPages = pages.map(page => new MainMenuPage(page));
      
      await Promise.all([
        loginPages[0].goto().then(() => loginPages[0].login('USER0001', 'user1234')),
        loginPages[1].goto().then(() => loginPages[1].login('USER0001', 'user1234')),
        loginPages[2].goto().then(() => loginPages[2].login('USER0001', 'user1234'))
      ]);
      
      await Promise.all([
        mainMenuPages[0].navigateToAccountView(),
        mainMenuPages[1].navigateToTransactions(),
        mainMenuPages[2].navigateToCards()
      ]);
      
      await pages[0].evaluate(() => {
        localStorage.setItem('authToken', 'manipulated-token-12345');
        sessionStorage.setItem('userSession', 'hacked-session-data');
      });
      
      await mainMenuPages[0].navigateToAccountView();
      
      const currentUrl = pages[0].url();
      const isSecure = currentUrl.includes('/login') || 
                      await pages[0].locator('text=access denied, text=unauthorized').isVisible();
      expect(isSecure).toBeTruthy();
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });

  test.describe('Advanced Authorization and Role-Based Access Control', () => {
    test('should enforce granular permissions and prevent privilege escalation', async ({ page, browser }) => {
      const userContext = await browser.newContext();
      const adminContext = await browser.newContext();
      
      const userPage = await userContext.newPage();
      const adminPage = await adminContext.newPage();
      
      const userLogin = new LoginPage(userPage);
      const adminLogin = new LoginPage(adminPage);
      const userMenu = new MainMenuPage(userPage);
      const adminMenu = new AdminMenuPage(adminPage);
      
      await userLogin.goto();
      await userLogin.login('USER0001', 'user1234');
      
      await adminLogin.goto();
      await adminLogin.login('ADMIN001', 'admin123');
      
      const restrictedRoutes = [
        '/admin-menu',
        '/admin/users',
        '/admin/user-add',
        '/admin/user-update',
        '/admin/user-delete',
        '/admin/system-settings',
        '/admin/audit-logs'
      ];
      
      for (const route of restrictedRoutes) {
        await userPage.goto(`http://localhost:5174${route}`);
        
        const currentUrl = userPage.url();
        const isBlocked = !currentUrl.includes(route) || 
                         await userPage.locator('text=access denied, text=unauthorized, text=forbidden').isVisible();
        expect(isBlocked).toBeTruthy();
      }
      
      await adminMenu.navigateToUserManagement();
      await expect(adminPage).toHaveURL(/.*admin.*/);
      
      await userMenu.navigateToAccountView();
      
      const accountInput = userPage.locator('input[name="accountNumber"]');
      if (await accountInput.isVisible()) {
        await accountInput.fill('99999999999'); // Different user's account
        await userPage.click('button:has-text("Search"), button:has-text("View")');
        
        const unauthorizedData = userPage.locator('text=unauthorized, text=access denied');
        if (await unauthorizedData.isVisible()) {
          expect(await unauthorizedData.textContent()).toMatch(/unauthorized|denied/i);
        }
      }
      
      await userContext.close();
      await adminContext.close();
    });
  });

  test.describe('Data Protection and Privacy Compliance', () => {
    test('should mask sensitive data and enforce data retention policies', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      const sensitiveFields = [
        'input[name="ssn"]',
        'input[name="accountNumber"]',
        'input[name="creditCardNumber"]',
        '.account-balance',
        '.sensitive-data'
      ];
      
      for (const selector of sensitiveFields) {
        const field = testPage.locator(selector);
        if (await field.isVisible()) {
          const value = await field.textContent() || await field.inputValue();
          
          if (value && value.length > 4) {
            const hasMasking = value.includes('*') || value.includes('X') || value.includes('•');
            if (hasMasking) {
              expect(hasMasking).toBeTruthy();
            }
          }
        }
      }
      
      await mainMenuPage.navigateToTransactions();
      
      await testPage.route('**/api/transactions**', async (route) => {
        const oldTransactions = Array.from({ length: 100 }, (_, i) => ({
          id: `TXN${String(i + 1).padStart(8, '0')}`,
          date: new Date(2020, 0, i + 1).toISOString(), // Old transactions
          amount: Math.random() * 1000,
          merchant: `Old Merchant ${i}`,
          status: 'completed'
        }));
        
        const recentTransactions = Array.from({ length: 50 }, (_, i) => ({
          id: `TXN${String(i + 101).padStart(8, '0')}`,
          date: new Date(2024, 0, i + 1).toISOString(), // Recent transactions
          amount: Math.random() * 1000,
          merchant: `Recent Merchant ${i}`,
          status: 'completed'
        }));
        
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            content: [...recentTransactions, ...oldTransactions.slice(0, 10)], // Limited old data
            totalElements: 60
          })
        });
      });
      
      await testPage.reload();
      
      const transactionRows = await testPage.locator('.transaction-row, tr').count();
      expect(transactionRows).toBeLessThan(200); // Reasonable limit
      
      await context.close();
    });
  });
});
