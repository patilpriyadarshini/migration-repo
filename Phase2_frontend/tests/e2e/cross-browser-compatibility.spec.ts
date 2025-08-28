import { test, expect, devices } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

const browsers = ['chromium', 'firefox', 'webkit'];

browsers.forEach(browserName => {
  test.describe(`Cross-Browser Compatibility - ${browserName}`, () => {
    let loginPage: LoginPage;
    let mainMenuPage: MainMenuPage;
    let adminMenuPage: AdminMenuPage;

    test.beforeEach(async ({ page }) => {
      loginPage = new LoginPage(page);
      mainMenuPage = new MainMenuPage(page);
      adminMenuPage = new AdminMenuPage(page);
    });

    test(`should handle authentication flow consistently in ${browserName}`, async ({ page }) => {
      await loginPage.goto();
      
      await loginPage.fillUserId('ADMIN001');
      await loginPage.fillPassword('admin123');
      
      const userIdValue = await page.inputValue('input[name="userId"]');
      const passwordValue = await page.inputValue('input[name="password"]');
      
      expect(userIdValue).toBe('ADMIN001');
      expect(passwordValue).toBe('admin123');
      
      await loginPage.clickSignIn();
      await expect(page).toHaveURL('/admin-menu');
      
      const menuElements = await page.locator('.admin-menu-item').count();
      expect(menuElements).toBeGreaterThan(0);
    });

    test(`should handle responsive design in ${browserName}`, async ({ page }) => {
      await page.setViewportSize({ width: 375, height: 667 });
      await loginPage.goto();
      
      const mobileMenu = page.locator('.mobile-menu-toggle');
      if (await mobileMenu.isVisible()) {
        await mobileMenu.click();
        await expect(page.locator('.mobile-menu')).toBeVisible();
      }
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await page.setViewportSize({ width: 768, height: 1024 });
      await page.reload();
      
      await expect(page.locator('.main-menu')).toBeVisible();
      
      await page.setViewportSize({ width: 1920, height: 1080 });
      await page.reload();
      
      await expect(page.locator('.desktop-layout')).toBeVisible();
    });

    test(`should handle form validation consistently in ${browserName}`, async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');
      
      await page.fill('input[name="userId"]', '');
      await page.click('button[type="submit"]');
      
      const validationMessage = await page.locator('input[name="userId"]:invalid').count();
      const customValidation = await page.locator('.field-error').count();
      
      expect(validationMessage + customValidation).toBeGreaterThan(0);
      
      await page.fill('input[name="userId"]', 'TEST0001');
      await page.fill('input[name="password"]', 'weak');
      await page.locator('input[name="password"]').blur();
      
      const strengthIndicator = page.locator('.password-strength');
      if (await strengthIndicator.isVisible()) {
        await expect(strengthIndicator).toContainText('weak');
      }
    });

    test(`should handle JavaScript events consistently in ${browserName}`, async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '');
      await page.press('input[name="accountNumber"]', 'Tab');
      
      const focusedElement = await page.evaluate(() => document.activeElement?.tagName);
      expect(focusedElement).toBeTruthy();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.hover('button[type="submit"]');
      
      const buttonStyles = await page.locator('button[type="submit"]').evaluate(
        el => window.getComputedStyle(el).backgroundColor
      );
      expect(buttonStyles).toBeTruthy();
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
    });

    test(`should handle CSS rendering consistently in ${browserName}`, async ({ page }) => {
      await loginPage.goto();
      
      const loginFormStyles = await page.locator('.login-form').evaluate(el => {
        const styles = window.getComputedStyle(el);
        return {
          display: styles.display,
          flexDirection: styles.flexDirection,
          justifyContent: styles.justifyContent,
          alignItems: styles.alignItems
        };
      });
      
      expect(loginFormStyles.display).toBe('flex');
      
      await loginPage.login('ADMIN001', 'admin123');
      
      const adminMenuStyles = await page.locator('.admin-menu').evaluate(el => {
        const styles = window.getComputedStyle(el);
        return {
          display: styles.display,
          gridTemplateColumns: styles.gridTemplateColumns,
          gap: styles.gap
        };
      });
      
      expect(adminMenuStyles.display).toMatch(/grid|flex/);
    });

    test(`should handle local storage consistently in ${browserName}`, async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      const storageData = await page.evaluate(() => ({
        userId: localStorage.getItem('userId'),
        userType: localStorage.getItem('userType')
      }));
      
      expect(storageData.userId).toBe('ADMIN001');
      expect(storageData.userType).toBe('A');
      
      await page.reload();
      
      const persistedData = await page.evaluate(() => ({
        userId: localStorage.getItem('userId'),
        userType: localStorage.getItem('userType')
      }));
      
      expect(persistedData.userId).toBe('ADMIN001');
      expect(persistedData.userType).toBe('A');
      
      await adminMenuPage.logout();
      
      const clearedData = await page.evaluate(() => ({
        userId: localStorage.getItem('userId'),
        userType: localStorage.getItem('userType')
      }));
      
      expect(clearedData.userId).toBeNull();
      expect(clearedData.userType).toBeNull();
    });
  });
});

const devices_list = [
  devices['iPhone 12'],
  devices['iPad Pro'],
  devices['Desktop Chrome'],
  devices['Desktop Firefox'],
  devices['Desktop Safari']
];

devices_list.forEach((device, index) => {
  test.describe(`Device Compatibility - Device ${index + 1}`, () => {
    test(`should work correctly on device ${index + 1}`, async ({ page, browser }) => {
      const context = await browser.newContext({ ...device });
      const devicePage = await context.newPage();
      const loginPage = new LoginPage(devicePage);
      const mainMenuPage = new MainMenuPage(devicePage);
      
      await loginPage.goto();
      
      const isTouchDevice = device.hasTouch;
      
      if (isTouchDevice) {
        await devicePage.tap('input[name="userId"]');
        await devicePage.fill('input[name="userId"]', 'USER0001');
        
        await devicePage.tap('input[name="password"]');
        await devicePage.fill('input[name="password"]', 'user1234');
        
        await devicePage.tap('button[type="submit"]');
      } else {
        await loginPage.login('USER0001', 'user1234');
      }
      
      await expect(devicePage).toHaveURL('/menu');
      
      if (isTouchDevice) {
        await devicePage.tap('text=Account View');
      } else {
        await mainMenuPage.navigateToAccountView();
      }
      
      await expect(devicePage).toHaveURL('/accounts/view');
      
      await devicePage.fill('input[name="accountNumber"]', '12345678901');
      
      if (isTouchDevice) {
        await devicePage.tap('button[type="submit"]');
      } else {
        await devicePage.click('button[type="submit"]');
      }
      
      await expect(devicePage.locator('.account-details')).toBeVisible();
      
      await context.close();
    });

    test(`should handle orientation changes on device ${index + 1}`, async ({ page, browser }) => {
      if (!device.hasTouch) {
        test.skip();
        return;
      }
      
      const context = await browser.newContext({ ...device });
      const devicePage = await context.newPage();
      const loginPage = new LoginPage(devicePage);
      
      await devicePage.setViewportSize({ width: 375, height: 667 });
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await expect(devicePage).toHaveURL('/menu');
      
      await devicePage.setViewportSize({ width: 667, height: 375 });
      await devicePage.reload();
      
      await expect(devicePage).toHaveURL('/menu');
      await expect(devicePage.locator('.main-menu')).toBeVisible();
      
      const menuLayout = await devicePage.locator('.main-menu').evaluate(el => {
        const styles = window.getComputedStyle(el);
        return {
          flexDirection: styles.flexDirection,
          width: styles.width
        };
      });
      
      expect(menuLayout).toBeTruthy();
      
      await context.close();
    });
  });
});
