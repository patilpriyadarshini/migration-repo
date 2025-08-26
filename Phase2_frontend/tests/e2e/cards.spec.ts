import { test, expect } from './setup';
import { loginAsRegularUser } from './utils/auth';

test.describe('Card Management', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsRegularUser(page);
  });

  test('should navigate to card listing', async ({ page }) => {
    await page.click('text=Card Listing');
    await expect(page).toHaveURL('/cards');
  });

  test('should navigate to card details', async ({ page }) => {
    await page.click('text=Card Details');
    await expect(page).toHaveURL('/cards/detail');
  });

  test('should navigate to card update', async ({ page }) => {
    await page.click('text=Card Update');
    await expect(page).toHaveURL('/cards/update');
  });

  test('should display card listing page elements', async ({ page }) => {
    await page.click('text=Card Listing');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display card details page elements', async ({ page }) => {
    await page.click('text=Card Details');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });

  test('should display card update page elements', async ({ page }) => {
    await page.click('text=Card Update');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });
});
