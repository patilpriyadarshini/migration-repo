import { test, expect } from '@playwright/test';
import { loginAsRegularUser, TEST_ACCOUNTS } from './utils/auth';

test.describe('Error Handling', () => {
  test.describe('Account Not Found Errors', () => {
    test.beforeEach(async ({ page }) => {
      await loginAsRegularUser(page);
      await page.click('text=View Account');
    });

    test('should display error for non-existent account', async ({ page }) => {
      await page.fill('input[name="accountId"]', TEST_ACCOUNTS.invalid);
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID NOT found')).toBeVisible();
    });

    test('should clear previous errors on new search', async ({ page }) => {
      await page.fill('input[name="accountId"]', TEST_ACCOUNTS.invalid);
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID NOT found')).toBeVisible();

      await page.fill('input[name="accountId"]', TEST_ACCOUNTS.valid);
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID NOT found')).not.toBeVisible();
      await expect(page.locator('text=Account Information')).toBeVisible();
    });
  });

  test.describe('Authentication Errors', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto('/login');
    });

    test('should display error for invalid credentials', async ({ page }) => {
      await page.fill('input[name="userId"]', 'INVALID1');
      await page.fill('input[name="password"]', 'invalid1');
      await page.click('button[type="submit"]');
      await expect(page.locator('text=Invalid credentials')).toBeVisible();
    });

    test('should clear error on successful login attempt', async ({ page }) => {
      await page.fill('input[name="userId"]', 'INVALID1');
      await page.fill('input[name="password"]', 'invalid1');
      await page.click('button[type="submit"]');
      await expect(page.locator('text=Invalid credentials')).toBeVisible();

      await page.fill('input[name="userId"]', 'USER0001');
      await page.fill('input[name="password"]', 'user1234');
      await page.click('button[type="submit"]');
      await expect(page).toHaveURL('/menu');
    });
  });
});
