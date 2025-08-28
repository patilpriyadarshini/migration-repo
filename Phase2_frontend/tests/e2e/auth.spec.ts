import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Authentication Flows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
    await loginPage.goto();
  });

  test.describe('Admin User Authentication', () => {
    test('should login successfully as admin and navigate to admin menu', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      await adminMenuPage.verifyAdminMenuVisible();
    });

    test('should have admin privileges and access admin-only features', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.verifyUserManagementVisible();
      await adminMenuPage.verifyAllAdminOptionsVisible();
    });

    test('should maintain admin session after page refresh', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await page.reload();
      await expect(page).toHaveURL('/admin-menu');
      await adminMenuPage.verifyAdminMenuVisible();
    });
  });

  test.describe('Regular User Authentication', () => {
    test('should login successfully as regular user and navigate to main menu', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      await mainMenuPage.verifyMainMenuVisible();
    });

    test('should not have access to admin features', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/admin-menu');
      await expect(page).toHaveURL('/menu');
    });

    test('should maintain regular user session after page refresh', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await page.reload();
      await expect(page).toHaveURL('/menu');
      await mainMenuPage.verifyMainMenuVisible();
    });
  });

  test.describe('Authentication Failures', () => {
    test('should show error for invalid credentials', async ({ page }) => {
      await loginPage.login('INVALID', 'invalid');
      await loginPage.verifyErrorMessage('Login failed');
      await expect(page).toHaveURL('/login');
    });

    test('should show error for empty credentials', async ({ page }) => {
      await loginPage.clickSignIn();
      await loginPage.verifyValidationErrors();
      await expect(page).toHaveURL('/login');
    });

    test('should show error for partial credentials', async ({ page }) => {
      await loginPage.fillUserId('ADMIN001');
      await loginPage.clickSignIn();
      await loginPage.verifyPasswordValidationError();
      await expect(page).toHaveURL('/login');
    });

    test('should handle network errors gracefully', async ({ page }) => {
      await page.route('**/api/auth/login', route => route.abort());
      await loginPage.login('ADMIN001', 'admin123');
      await loginPage.verifyErrorMessage('Login failed');
    });
  });

  test.describe('Logout Functionality', () => {
    test('should logout admin user successfully', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.logout();
      await expect(page).toHaveURL('/login');
      await loginPage.verifyLoginFormVisible();
    });

    test('should logout regular user successfully', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.logout();
      await expect(page).toHaveURL('/login');
      await loginPage.verifyLoginFormVisible();
    });

    test('should clear session data on logout', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.logout();
      
      const userId = await page.evaluate(() => localStorage.getItem('userId'));
      const userType = await page.evaluate(() => localStorage.getItem('userType'));
      
      expect(userId).toBeNull();
      expect(userType).toBeNull();
    });
  });

  test.describe('Role-Based Routing', () => {
    test('should redirect admin to admin-menu after login', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should redirect regular user to menu after login', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
    });

    test('should prevent regular user from accessing admin routes', async ({ page }) => {
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/admin/users/add');
      await expect(page).toHaveURL('/menu');
    });

    test('should allow admin to access all routes', async ({ page }) => {
      await loginPage.login('ADMIN001', 'admin123');
      
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/admin/users');
      
      await page.goto('/accounts/view');
      await expect(page).toHaveURL('/accounts/view');
    });
  });
});
