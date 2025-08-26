import { test, expect } from './setup';
import { loginAsRegularUser, TEST_ACCOUNTS } from './utils/auth';

test.describe('Form Validation', () => {
  test.describe('Account Search Validation', () => {
    test.beforeEach(async ({ page }) => {
      await loginAsRegularUser(page);
      await page.click('text=View Account');
    });

    test('should validate empty account ID', async ({ page }) => {
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID cannot be empty')).toBeVisible();
    });

    test('should validate account ID length', async ({ page }) => {
      await page.fill('input[name="accountId"]', '123');
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID must be exactly 11 digits')).toBeVisible();
    });

    test('should validate account ID format (non-numeric)', async ({ page }) => {
      await page.fill('input[name="accountId"]', 'abcdefghijk');
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account ID must be exactly 11 digits')).toBeVisible();
    });

    test('should accept valid account ID format', async ({ page }) => {
      await page.fill('input[name="accountId"]', TEST_ACCOUNTS.valid);
      await page.click('button:has-text("Search")');
      await expect(page.locator('text=Account Information')).toBeVisible();
    });
  });

  test.describe('Login Form Validation', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto('/login');
    });

    test('should validate empty fields', async ({ page }) => {
      await page.click('button[type="submit"]');
      await expect(page.locator('text=User ID cannot be empty')).toBeVisible();
      await expect(page.locator('text=Password cannot be empty')).toBeVisible();
    });

    test('should validate field lengths', async ({ page }) => {
      await page.fill('input[name="userId"]', '123');
      await page.fill('input[name="password"]', '456');
      await page.click('button[type="submit"]');
      await expect(page.locator('text=User ID must be exactly 8 characters')).toBeVisible();
      await expect(page.locator('text=Password must be exactly 8 characters')).toBeVisible();
    });

    test('should validate maximum field lengths', async ({ page }) => {
      await page.fill('input[name="userId"]', '123456789');
      await page.fill('input[name="password"]', '123456789');
      await page.click('button[type="submit"]');
      await expect(page.locator('text=User ID must be exactly 8 characters')).toBeVisible();
      await expect(page.locator('text=Password must be exactly 8 characters')).toBeVisible();
    });
  });
});
