import { test, expect } from './setup';
import { loginAsAdmin, loginAsRegularUser } from './utils/auth';

test.describe('Navigation', () => {
  test.describe('Regular User Navigation', () => {
    test.beforeEach(async ({ page }) => {
      await loginAsRegularUser(page);
    });

    test('should display main menu options', async ({ page }) => {
      await expect(page.locator('text=Main Menu')).toBeVisible();
      await expect(page.locator('text=View Account')).toBeVisible();
      await expect(page.locator('text=View Account (Update)')).toBeVisible();
      await expect(page.locator('text=Card Listing')).toBeVisible();
      await expect(page.locator('text=Card Details')).toBeVisible();
      await expect(page.locator('text=Card Update')).toBeVisible();
      await expect(page.locator('text=Transaction List')).toBeVisible();
      await expect(page.locator('text=Transaction View')).toBeVisible();
      await expect(page.locator('text=Transaction Add')).toBeVisible();
      await expect(page.locator('text=Bill Payment')).toBeVisible();
      await expect(page.locator('text=Report Generation')).toBeVisible();
    });

    test('should navigate to account view', async ({ page }) => {
      await page.click('text=View Account');
      await expect(page).toHaveURL('/accounts/view');
      await expect(page.locator('text=Account View')).toBeVisible();
    });

    test('should navigate to account update', async ({ page }) => {
      await page.click('text=View Account (Update)');
      await expect(page).toHaveURL('/accounts/update');
    });

    test('should navigate to card listing', async ({ page }) => {
      await page.click('text=Card Listing');
      await expect(page).toHaveURL('/cards');
    });

    test('should navigate to transactions', async ({ page }) => {
      await page.click('text=Transaction List');
      await expect(page).toHaveURL('/transactions');
    });

    test('should navigate to bill payment', async ({ page }) => {
      await page.click('text=Bill Payment');
      await expect(page).toHaveURL('/bill-payment');
    });

    test('should navigate to reports', async ({ page }) => {
      await page.click('text=Report Generation');
      await expect(page).toHaveURL('/reports');
    });
  });

  test.describe('Admin User Navigation', () => {
    test.beforeEach(async ({ page }) => {
      await loginAsAdmin(page);
    });

    test('should display admin menu options', async ({ page }) => {
      await expect(page.locator('text=Admin Menu')).toBeVisible();
      await expect(page.locator('text=User List')).toBeVisible();
      await expect(page.locator('text=User Add')).toBeVisible();
      await expect(page.locator('text=User Update')).toBeVisible();
      await expect(page.locator('text=User Delete')).toBeVisible();
    });

    test('should navigate to user list', async ({ page }) => {
      await page.click('text=User List');
      await expect(page).toHaveURL('/admin/users');
    });

    test('should navigate to user add', async ({ page }) => {
      await page.click('text=User Add');
      await expect(page).toHaveURL('/admin/users/add');
    });

    test('should navigate to user update', async ({ page }) => {
      await page.click('text=User Update');
      await expect(page).toHaveURL('/admin/users/update');
    });

    test('should navigate to user delete', async ({ page }) => {
      await page.click('text=User Delete');
      await expect(page).toHaveURL('/admin/users/delete');
    });
  });
});
