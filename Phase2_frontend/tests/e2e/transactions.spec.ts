import { test, expect } from '@playwright/test';
import { loginAsRegularUser } from './utils/auth';

test.describe('Transaction Management', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsRegularUser(page);
  });

  test('should navigate to transaction list', async ({ page }) => {
    await page.click('text=Transaction List');
    await expect(page).toHaveURL('/transactions');
  });

  test('should navigate to transaction view', async ({ page }) => {
    await page.click('text=Transaction View');
    await expect(page).toHaveURL('/transactions/view');
  });

  test('should navigate to transaction add', async ({ page }) => {
    await page.click('text=Transaction Add');
    await expect(page).toHaveURL('/transactions/add');
  });

  test('should display transaction list page elements', async ({ page }) => {
    await page.click('text=Transaction List');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display transaction view page elements', async ({ page }) => {
    await page.click('text=Transaction View');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display transaction add page elements', async ({ page }) => {
    await page.click('text=Transaction Add');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });
});
