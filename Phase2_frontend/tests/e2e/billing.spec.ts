import { test, expect } from '@playwright/test';
import { loginAsRegularUser } from './utils/auth';

test.describe('Bill Payment', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsRegularUser(page);
  });

  test('should navigate to bill payment', async ({ page }) => {
    await page.click('text=Bill Payment');
    await expect(page).toHaveURL('/bill-payment');
  });

  test('should display bill payment page elements', async ({ page }) => {
    await page.click('text=Bill Payment');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });
});
