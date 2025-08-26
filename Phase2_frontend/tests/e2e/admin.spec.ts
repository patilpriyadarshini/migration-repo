import { test, expect } from './setup';
import { loginAsAdmin } from './utils/auth';

test.describe('Admin Functionality', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsAdmin(page);
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

  test('should display user list page elements', async ({ page }) => {
    await page.click('text=User List');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display user add page elements', async ({ page }) => {
    await page.click('text=User Add');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display user update page elements', async ({ page }) => {
    await page.click('text=User Update');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display user delete page elements', async ({ page }) => {
    await page.click('text=User Delete');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });
});
