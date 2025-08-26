import { test, expect } from './setup';
import { loginAsRegularUser, loginAsAdmin, TEST_ACCOUNTS } from './utils/auth';
import { AccountViewPage } from './page-objects/AccountViewPage';

test.describe('Account Management', () => {
  let accountPage: AccountViewPage;

  test.beforeEach(async ({ page }) => {
    await loginAsRegularUser(page);
    await page.click('text=View Account');
    accountPage = new AccountViewPage(page);
  });

  test('should display account search form', async ({ page }) => {
    await expect(page.locator('text=Account View')).toBeVisible();
    await expect(page.locator('text=Account Search')).toBeVisible();
    await expect(accountPage.accountIdInput).toBeVisible();
    await expect(accountPage.searchButton).toBeVisible();
  });

  test('should validate account ID format', async ({ page }) => {
    await accountPage.searchAccount('123');
    
    await expect(page.locator('text=Account ID must be exactly 11 digits')).toBeVisible();
  });

  test('should display account information for valid account ID', async ({ page }) => {
    await accountPage.searchAccount(TEST_ACCOUNTS.valid);
    
    await expect(page.locator('text=Account Information')).toBeVisible();
    await expect(page.locator('text=Alice')).toBeVisible();
    await expect(page.locator('text=Johnson')).toBeVisible();
    await expect(page.locator('text=1500.00')).toBeVisible();
    await expect(page.locator('text=Active')).toBeVisible();
  });

  test('should display error message for invalid account ID', async ({ page }) => {
    await accountPage.searchAccount(TEST_ACCOUNTS.invalid);
    
    await expect(page.locator('text=Account ID NOT found')).toBeVisible();
  });

  test('should navigate back to correct menu based on user type', async ({ page }) => {
    await accountPage.goBack();
    await expect(page).toHaveURL('/menu');
  });

  test('should navigate back to admin menu for admin user', async ({ page }) => {
    await loginAsAdmin(page);
    await page.click('text=View Account');
    accountPage = new AccountViewPage(page);
    
    await accountPage.goBack();
    await expect(page).toHaveURL('/admin-menu');
  });

  test('should navigate to account update with account data', async ({ page }) => {
    await accountPage.searchAccount(TEST_ACCOUNTS.valid);
    await expect(page.locator('text=Account Information')).toBeVisible();
    
    await accountPage.updateAccount();
    await expect(page).toHaveURL('/accounts/update');
  });
});
