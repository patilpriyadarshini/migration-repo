import { test, expect } from './setup';
import { loginAsRegularUser } from './utils/auth';

test.describe('Report Generation', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsRegularUser(page);
  });

  test('should navigate to reports', async ({ page }) => {
    await page.click('text=Report Generation');
    await expect(page).toHaveURL('/reports');
  });

  test('should display reports page elements', async ({ page }) => {
    await page.click('text=Report Generation');
    await expect(page.locator('text=CardDemo')).toBeVisible();
  });
});
