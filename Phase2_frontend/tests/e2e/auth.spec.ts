import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { TEST_USERS } from './utils/auth';

test.describe('Authentication', () => {
  let loginPage: LoginPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    await loginPage.goto();
  });

  test('should display login form', async ({ page }) => {
    await expect(loginPage.title).toContainText('CardDemo System');
    await expect(page.locator('text=Sign in to your account')).toBeVisible();
    await expect(loginPage.userIdInput).toBeVisible();
    await expect(loginPage.passwordInput).toBeVisible();
    await expect(loginPage.submitButton).toBeVisible();
  });

  test('should validate required fields', async ({ page }) => {
    await loginPage.submitButton.click();
    
    await expect(page.locator('text=User ID cannot be empty')).toBeVisible();
    await expect(page.locator('text=Password cannot be empty')).toBeVisible();
  });

  test('should validate field length requirements', async ({ page }) => {
    await loginPage.login('123', '456');
    
    await expect(page.locator('text=User ID must be exactly 8 characters')).toBeVisible();
    await expect(page.locator('text=Password must be exactly 8 characters')).toBeVisible();
  });

  test('should login admin user successfully', async ({ page }) => {
    await loginPage.login(TEST_USERS.admin.userId, TEST_USERS.admin.password);
    
    await expect(page).toHaveURL('/admin-menu');
    await expect(page.locator('text=Admin Menu')).toBeVisible();
    await expect(page.locator(`text=Admin: ${TEST_USERS.admin.userId}`)).toBeVisible();
  });

  test('should login regular user successfully', async ({ page }) => {
    await loginPage.login(TEST_USERS.regular.userId, TEST_USERS.regular.password);
    
    await expect(page).toHaveURL('/menu');
    await expect(page.locator('text=Main Menu')).toBeVisible();
    await expect(page.locator(`text=User: ${TEST_USERS.regular.userId}`)).toBeVisible();
  });

  test('should display error for invalid credentials', async ({ page }) => {
    await loginPage.login('INVALID1', 'invalid1');
    
    await expect(page.locator('text=Invalid credentials')).toBeVisible();
  });

  test('should logout successfully', async ({ page }) => {
    await loginPage.login(TEST_USERS.regular.userId, TEST_USERS.regular.password);
    await expect(page).toHaveURL('/menu');
    
    await page.click('text=Logout');
    await expect(page).toHaveURL('/login');
  });
});
