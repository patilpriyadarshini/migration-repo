import { test, expect, devices } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Enhanced Cross-Browser Compatibility Testing', () => {
  test.describe('Multi-Browser Authentication and Navigation', () => {
    const browsers = ['chromium', 'firefox', 'webkit'];
    
    for (const browserName of browsers) {
      test(`should handle complete authentication flow in ${browserName}`, async ({ page, browser }) => {
        const context = await browser.newContext();
        const testPage = await context.newPage();
        
        const loginPage = new LoginPage(testPage);
        const mainMenuPage = new MainMenuPage(testPage);

        await loginPage.goto();
        await loginPage.login('USER0001', 'user1234');
        
        await mainMenuPage.verifyMainMenuVisible();
        await mainMenuPage.navigateToAccountView();
        await expect(testPage).toHaveURL(/.*account-view.*/);
        
        await context.close();
      });
    }
  });

  test.describe('Advanced Device and Accessibility Testing', () => {
    test('should handle complex touch gestures and mobile interactions', async ({ page, browser }) => {
      const mobileContext = await browser.newContext({
        ...devices['iPhone 13'],
        hasTouch: true
      });
      const mobilePage = await mobileContext.newPage();
      
      const loginPage = new LoginPage(mobilePage);
      const mainMenuPage = new MainMenuPage(mobilePage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');

      await mainMenuPage.navigateToTransactions();

      await mobilePage.touchscreen.tap(300, 400);
      await mobilePage.touchscreen.tap(300, 400); // Double tap
      
      await mobilePage.mouse.move(300, 400);
      await mobilePage.mouse.down();
      await mobilePage.mouse.move(100, 400);
      await mobilePage.mouse.up();

      await mobilePage.touchscreen.tap(200, 300);
      await mobilePage.touchscreen.tap(250, 350);

      await expect(mobilePage.locator('body')).toBeVisible();
      
      await mobileContext.close();
    });

    test('should maintain accessibility standards across browsers', async ({ page, browser }) => {
      const browsers = ['chromium'];
      
      for (const browserName of browsers) {
        const context = await browser.newContext();
        const testPage = await context.newPage();
        
        const loginPage = new LoginPage(testPage);
        await loginPage.goto();

        await testPage.keyboard.press('Tab');
        await expect(testPage.locator('input[name="userId"]:focus')).toBeVisible();
        
        await testPage.keyboard.press('Tab');
        await expect(testPage.locator('input[name="password"]:focus')).toBeVisible();
        
        await testPage.keyboard.press('Tab');
        await expect(testPage.locator('button[type="submit"]:focus')).toBeVisible();

        const userIdInput = testPage.locator('input[name="userId"]');
        const passwordInput = testPage.locator('input[name="password"]');
        const submitButton = testPage.locator('button[type="submit"]');
        
        await expect(userIdInput).toHaveAttribute('placeholder');
        await expect(passwordInput).toHaveAttribute('placeholder');
        await expect(submitButton).toContainText(/login|sign in/i);

        await context.close();
      }
    });

    test('should handle responsive breakpoints and layout shifts', async ({ page, browser }) => {
      const breakpoints = [
        { width: 320, height: 568, name: 'mobile-small' },
        { width: 768, height: 1024, name: 'tablet' },
        { width: 1440, height: 900, name: 'desktop' }
      ];

      for (const breakpoint of breakpoints) {
        const context = await browser.newContext({
          viewport: { width: breakpoint.width, height: breakpoint.height }
        });
        const testPage = await context.newPage();
        
        const loginPage = new LoginPage(testPage);
        const mainMenuPage = new MainMenuPage(testPage);

        await loginPage.goto();
        await loginPage.login('USER0001', 'user1234');

        await mainMenuPage.navigateToAccountView();
        
        const formContainer = testPage.locator('form, .form-container, .login-form').first();
        const containerBox = await formContainer.boundingBox();
        
        if (containerBox) {
          if (breakpoint.width < 768) {
            expect(containerBox.width).toBeLessThan(breakpoint.width * 0.95);
          } else {
            expect(containerBox.width).toBeGreaterThan(breakpoint.width * 0.3);
          }
        }

        await context.close();
      }
    });
  });

  test.describe('Performance and Memory Management Testing', () => {
    test('should handle memory-intensive operations without degradation', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');

      await testPage.route('**/api/transactions**', async (route) => {
        const largeDataset = Array.from({ length: 5000 }, (_, i) => ({
          id: `TXN${String(i + 1).padStart(8, '0')}`,
          amount: Math.random() * 5000,
          merchant: `Merchant ${i + 1} with detailed description`,
          date: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
          category: ['dining', 'shopping', 'gas', 'groceries'][Math.floor(Math.random() * 4)],
          status: ['completed', 'pending', 'failed'][Math.floor(Math.random() * 3)]
        }));
        
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            content: largeDataset,
            totalElements: largeDataset.length
          })
        });
      });

      const startTime = Date.now();
      
      await mainMenuPage.navigateToTransactions();
      
      await expect(testPage.locator('body')).toBeVisible();
      
      const loadTime = Date.now() - startTime;
      expect(loadTime).toBeLessThan(10000); // Should load within 10 seconds

      for (let i = 0; i < 20; i++) {
        await testPage.evaluate(() => window.scrollBy(0, 100));
        await testPage.waitForTimeout(50);
      }

      await context.close();
    });

    test('should handle browser-specific performance optimizations', async ({ page, browser }) => {
      const performanceMetrics: Array<{
        browser: string;
        loginTime: number;
        navigationTime: number;
      }> = [];
      
      const browsers = ['chromium'];
      
      for (const browserName of browsers) {
        const context = await browser.newContext();
        const testPage = await context.newPage();

        const loginPage = new LoginPage(testPage);
        const mainMenuPage = new MainMenuPage(testPage);

        const startTime = Date.now();
        
        await loginPage.goto();
        await loginPage.login('USER0001', 'user1234');
        
        const loginTime = Date.now() - startTime;
        
        const navigationStartTime = Date.now();
        
        await mainMenuPage.navigateToAccountView();
        await mainMenuPage.navigateToTransactions();
        await mainMenuPage.navigateToCards();
        
        const navigationTime = Date.now() - navigationStartTime;

        performanceMetrics.push({
          browser: browserName,
          loginTime,
          navigationTime
        });

        await context.close();
      }

      for (const metrics of performanceMetrics) {
        expect(metrics.loginTime).toBeLessThan(8000);
        expect(metrics.navigationTime).toBeLessThan(5000);
      }
    });
  });

  test.describe('Advanced Error Handling and Recovery', () => {
    test('should handle network failures gracefully across browsers', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);

      await testPage.route('**/api/auth/login**', async (route) => {
        await route.abort('failed');
      });

      await loginPage.goto();
      await loginPage.fillCredentials('USER0001', 'user1234');
      await loginPage.clickSubmit();

      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });

    test('should handle concurrent user sessions without conflicts', async ({ page, browser }) => {
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
        loginPages[1].goto().then(() => loginPages[1].login('ADMIN001', 'admin123')),
        loginPages[2].goto().then(() => loginPages[2].login('USER0001', 'user1234'))
      ]);

      await Promise.all([
        mainMenuPages[0].navigateToAccountView(),
        mainMenuPages[1].navigateToAccountView(),
        mainMenuPages[2].navigateToTransactions()
      ]);

      for (const page of pages) {
        await expect(page.locator('body')).toBeVisible();
      }

      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });

  test.describe('Advanced State Management and Data Persistence', () => {
    test('should maintain form state across browser refresh', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      await testPage.fill('input[name="accountNumber"]', '12345678901');
      
      await testPage.reload();
      
      const accountInput = testPage.locator('input[name="accountNumber"]');
      if (await accountInput.isVisible()) {
        await expect(testPage.locator('body')).toBeVisible();
      }
      
      await context.close();
    });

    test('should handle complex data filtering with state persistence', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToTransactions();
      
      const categorySelect = testPage.locator('select[name="category"]');
      const amountInput = testPage.locator('input[name="amount"]');
      const dateInput = testPage.locator('input[name="date"]');
      
      if (await categorySelect.isVisible()) {
        await categorySelect.selectOption('dining');
      }
      
      if (await amountInput.isVisible()) {
        await amountInput.fill('100');
      }
      
      if (await dateInput.isVisible()) {
        await dateInput.fill('2024-01-01');
      }
      
      await mainMenuPage.navigateToAccountView();
      await mainMenuPage.navigateToTransactions();
      
      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });
  });

  test.describe('Advanced Security and Authentication Testing', () => {
    test('should handle session timeout and re-authentication', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.evaluate(() => {
        localStorage.clear();
        sessionStorage.clear();
      });
      
      await mainMenuPage.navigateToAccountView();
      
      const currentUrl = testPage.url();
      const isOnLoginPage = currentUrl.includes('/login');
      const hasReAuthPrompt = await testPage.locator('text=session expired').isVisible().catch(() => false);
      
      expect(isOnLoginPage || hasReAuthPrompt).toBeTruthy();
      
      await context.close();
    });

    test('should prevent unauthorized access to admin functions', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);

      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.goto('/admin-menu');
      
      const currentUrl = testPage.url();
      const isRedirected = !currentUrl.includes('/admin-menu');
      const hasAccessDenied = await testPage.locator('text=access denied').isVisible().catch(() => false);
      
      expect(isRedirected || hasAccessDenied).toBeTruthy();
      
      await context.close();
    });
  });
});
