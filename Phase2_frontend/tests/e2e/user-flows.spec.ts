import { test, expect } from '@playwright/test';
import { TEST_USERS, TEST_ACCOUNTS } from './utils/auth';

test.describe('Complete User Flows', () => {
  test('should complete full account lookup flow', async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[name="userId"]', TEST_USERS.regular.userId);
    await page.fill('input[name="password"]', TEST_USERS.regular.password);
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/menu');

    await page.click('text=View Account');
    await expect(page).toHaveURL('/accounts/view');

    await page.fill('input[name="accountId"]', TEST_ACCOUNTS.valid);
    await page.click('button:has-text("Search")');
    await expect(page.locator('text=Account Information')).toBeVisible();
    await expect(page.locator('text=Alice Johnson')).toBeVisible();

    await page.click('button:has-text("Back to Menu")');
    await expect(page).toHaveURL('/menu');

    await page.click('text=Logout');
    await expect(page).toHaveURL('/login');
  });

  test('should complete admin user management flow', async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[name="userId"]', TEST_USERS.admin.userId);
    await page.fill('input[name="password"]', TEST_USERS.admin.password);
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/admin-menu');

    await page.click('text=User List');
    await expect(page).toHaveURL('/admin/users');

    await page.click('text=Logout');
    await expect(page).toHaveURL('/login');
  });

  test('should handle navigation between different sections', async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[name="userId"]', TEST_USERS.regular.userId);
    await page.fill('input[name="password"]', TEST_USERS.regular.password);
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/menu');

    await page.click('text=Card Listing');
    await expect(page).toHaveURL('/cards');

    await page.goBack();
    await expect(page).toHaveURL('/menu');

    await page.click('text=Transaction List');
    await expect(page).toHaveURL('/transactions');

    await page.goBack();
    await expect(page).toHaveURL('/menu');

    await page.click('text=Bill Payment');
    await expect(page).toHaveURL('/bill-payment');
  });
});
