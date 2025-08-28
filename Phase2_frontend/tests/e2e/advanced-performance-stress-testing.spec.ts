import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Performance and Stress Testing', () => {
  test.describe('High-Load Concurrent User Simulation', () => {
    test('should handle 10+ concurrent user sessions without performance degradation', async ({ page, browser }) => {
      const concurrentUsers = 12;
      const contexts = await Promise.all(
        Array.from({ length: concurrentUsers }, () => browser.newContext())
      );
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));
      const mainMenuPages = pages.map(page => new MainMenuPage(page));
      
      const loginStartTime = Date.now();
      
      const loginPromises = pages.map(async (page, index) => {
        const userId = index % 2 === 0 ? 'USER0001' : 'ADMIN001';
        const password = index % 2 === 0 ? 'user1234' : 'admin123';
        
        await loginPages[index].goto();
        await loginPages[index].login(userId, password);
        
        return Date.now();
      });
      
      const loginCompletionTimes = await Promise.all(loginPromises);
      const maxLoginTime = Math.max(...loginCompletionTimes) - loginStartTime;
      
      expect(maxLoginTime).toBeLessThan(15000); // All logins should complete within 15 seconds
      
      const navigationStartTime = Date.now();
      
      const navigationPromises = mainMenuPages.map(async (menuPage, index) => {
        const actions = [
          () => menuPage.navigateToAccountView(),
          () => menuPage.navigateToTransactions(),
          () => menuPage.navigateToCards(),
          () => menuPage.navigateToBillPayment()
        ];
        
        for (let i = 0; i < 5; i++) {
          const randomAction = actions[Math.floor(Math.random() * actions.length)];
          await randomAction();
          await pages[index].waitForTimeout(100 + Math.random() * 200);
        }
        
        return Date.now();
      });
      
      const navigationCompletionTimes = await Promise.all(navigationPromises);
      const maxNavigationTime = Math.max(...navigationCompletionTimes) - navigationStartTime;
      
      expect(maxNavigationTime).toBeLessThan(30000); // All navigation should complete within 30 seconds
      
      const formSubmissionPromises = pages.map(async (page, index) => {
        await mainMenuPages[index].navigateToAccountView();
        
        const accountInput = page.locator('input[name="accountNumber"]');
        if (await accountInput.isVisible()) {
          await accountInput.fill(`12345678${String(index).padStart(3, '0')}`);
          
          const submitButton = page.locator('button:has-text("Update"), button:has-text("Submit")');
          if (await submitButton.isVisible()) {
            await submitButton.click();
          }
        }
        
        return Date.now();
      });
      
      await Promise.all(formSubmissionPromises);
      
      for (let i = 0; i < pages.length; i++) {
        await expect(pages[i].locator('body')).toBeVisible();
        
        const currentUrl = pages[i].url();
        expect(currentUrl).not.toContain('/login');
      }
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });

    test('should maintain responsiveness under extreme memory pressure', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.route('**/api/**', async (route) => {
        const url = route.request().url();
        
        if (url.includes('transactions')) {
          const massiveDataset = Array.from({ length: 50000 }, (_, i) => ({
            id: `TXN${String(i + 1).padStart(8, '0')}`,
            amount: Math.random() * 10000,
            merchant: `Merchant ${i + 1} - ${Array(200).fill('Large description with lots of text').join(' ')}`,
            date: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
            category: 'dining',
            status: 'completed',
            metadata: {
              location: `Location ${i} with extensive geographical information and coordinates`,
              details: Array(500).fill(`Extensive detail ${i} with comprehensive information`).join(' '),
              tags: Array(100).fill(`tag${i}`),
              history: Array(50).fill({ 
                timestamp: Date.now(), 
                action: `action${i}`,
                details: Array(100).fill(`history detail ${i}`).join(' ')
              }),
              attachments: Array(20).fill({
                filename: `document${i}.pdf`,
                content: Array(1000).fill('document content').join(' ')
              })
            }
          }));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: massiveDataset,
              totalElements: massiveDataset.length
            })
          });
        } else if (url.includes('accounts')) {
          const accountDataset = Array.from({ length: 10000 }, (_, i) => ({
            accountNumber: `ACC${String(i + 1).padStart(10, '0')}`,
            balance: Math.random() * 100000,
            transactions: Array(1000).fill({
              id: `TXN${i}`,
              amount: Math.random() * 1000,
              description: Array(100).fill('transaction description').join(' ')
            })
          }));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(accountDataset)
          });
        } else {
          await route.continue();
        }
      });
      
      const memoryStressStart = Date.now();
      
      for (let cycle = 0; cycle < 5; cycle++) {
        await mainMenuPage.navigateToTransactions();
        await testPage.waitForTimeout(2000); // Allow data to load
        
        for (let scroll = 0; scroll < 50; scroll++) {
          await testPage.evaluate(() => window.scrollBy(0, 1000));
          await testPage.waitForTimeout(50);
        }
        
        await testPage.evaluate(() => window.scrollTo(0, 0));
        
        await mainMenuPage.navigateToAccountView();
        await testPage.waitForTimeout(1000);
        
        for (let interaction = 0; interaction < 100; interaction++) {
          const accountInput = testPage.locator('input[name="accountNumber"]');
          if (await accountInput.isVisible()) {
            await accountInput.fill(`${Math.random().toString().slice(2, 12)}`);
            await accountInput.clear();
          }
        }
      }
      
      const memoryStressTime = Date.now() - memoryStressStart;
      
      const responsiveTestStart = Date.now();
      await mainMenuPage.navigateToCards();
      const responsiveTestTime = Date.now() - responsiveTestStart;
      
      expect(memoryStressTime).toBeLessThan(120000); // Should complete within 2 minutes
      expect(responsiveTestTime).toBeLessThan(10000); // Navigation should remain reasonably fast
      
      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });
  });

  test.describe('Network Latency and Connectivity Stress Testing', () => {
    test('should handle high network latency and intermittent connectivity', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      let requestCount = 0;
      
      await testPage.route('**/api/**', async (route) => {
        requestCount++;
        
        const latency = 2000 + Math.random() * 3000;
        await new Promise(resolve => setTimeout(resolve, latency));
        
        if (Math.random() < 0.2) {
          await route.fulfill({
            status: 503,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Service temporarily unavailable' })
          });
          return;
        }
        
        if (route.request().url().includes('login')) {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              token: 'mock-jwt-token',
              user: { userId: 'USER0001', userType: 'U' }
            })
          });
        } else {
          await route.continue();
        }
      });
      
      const highLatencyTestStart = Date.now();
      
      await loginPage.goto();
      
      await loginPage.login('USER0001', 'user1234');
      
      const navigationActions = [
        () => mainMenuPage.navigateToAccountView(),
        () => mainMenuPage.navigateToTransactions(),
        () => mainMenuPage.navigateToCards(),
        () => mainMenuPage.navigateToBillPayment()
      ];
      
      for (const action of navigationActions) {
        const actionStart = Date.now();
        await action();
        const actionTime = Date.now() - actionStart;
        
        expect(actionTime).toBeLessThan(15000); // Allow up to 15 seconds per action
        
        await expect(testPage.locator('body')).toBeVisible();
      }
      
      const totalHighLatencyTime = Date.now() - highLatencyTestStart;
      expect(totalHighLatencyTime).toBeLessThan(120000); // Should complete within 2 minutes
      
      await context.close();
    });

    test('should recover from complete network disconnection', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      await testPage.route('**/api/**', async (route) => {
        await route.abort('failed');
      });
      
      await mainMenuPage.navigateToTransactions();
      
      const offlineIndicator = testPage.locator('text=offline, text=connection, text=network, .error-message');
      if (await offlineIndicator.isVisible()) {
        const offlineText = await offlineIndicator.textContent();
        expect(offlineText?.toLowerCase()).toMatch(/offline|connection|network|error/);
      }
      
      await testPage.unroute('**/api/**');
      
      await testPage.reload();
      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });
  });

  test.describe('Resource Exhaustion and Edge Case Testing', () => {
    test('should handle browser resource limits gracefully', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.evaluate(() => {
        for (let i = 0; i < 10000; i++) {
          const div = document.createElement('div');
          div.innerHTML = `<span>Element ${i}</span><input value="test${i}"><button>Button ${i}</button>`;
          document.body.appendChild(div);
        }
      });
      
      await mainMenuPage.navigateToTransactions();
      await expect(testPage.locator('body')).toBeVisible();
      
      await testPage.evaluate(() => {
        try {
          for (let i = 0; i < 1000; i++) {
            const largeData = Array(10000).fill(`data${i}`).join('');
            localStorage.setItem(`key${i}`, largeData);
          }
        } catch (e) {
          console.log('Storage limit reached:', e);
        }
      });
      
      await mainMenuPage.navigateToAccountView();
      await expect(testPage.locator('body')).toBeVisible();
      
      await testPage.evaluate(() => {
        const button = document.querySelector('button');
        if (button) {
          for (let i = 0; i < 10000; i++) {
            button.addEventListener('click', () => console.log(`Handler ${i}`));
          }
        }
      });
      
      const firstButton = testPage.locator('button').first();
      if (await firstButton.isVisible()) {
        await firstButton.click();
      }
      
      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });

    test('should handle extreme form validation scenarios', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      const extremeInputs = [
        Array(100000).fill('A').join(''), // Very long string
        Array(1000).fill('🚀💰🏦').join(''), // Unicode characters
        Array(500).fill('<script>alert("test")</script>').join(''), // Repeated XSS attempts
        Array(1000).fill('SELECT * FROM users WHERE 1=1; --').join(''), // Repeated SQL injection
        '\u0000'.repeat(1000), // Null bytes
        Array(1000).fill('\n\r\t').join(''), // Control characters
      ];
      
      for (const extremeInput of extremeInputs) {
        const accountInput = testPage.locator('input[name="accountNumber"]');
        if (await accountInput.isVisible()) {
          try {
            await accountInput.fill(extremeInput);
            await accountInput.blur();
            
            await expect(testPage.locator('body')).toBeVisible();
            
            const errorMessage = testPage.locator('.error-message, .alert-danger');
            if (await errorMessage.isVisible()) {
              const errorText = await errorMessage.textContent();
              expect(errorText?.length).toBeGreaterThan(0);
            }
            
            await accountInput.clear();
          } catch (error) {
            await expect(testPage.locator('body')).toBeVisible();
          }
        }
      }
      
      await context.close();
    });
  });
});
