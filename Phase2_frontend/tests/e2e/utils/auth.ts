import { Page, expect } from '@playwright/test';

export const TEST_USERS = {
  admin: { userId: 'ADMIN001', password: 'admin123', userType: 'A' },
  regular: { userId: 'USER0001', password: 'user1234', userType: 'U' }
};

export const TEST_ACCOUNTS = {
  valid: '12345678901',
  invalid: '99999999999'
};

export async function loginAsAdmin(page: Page) {
  await page.goto('/login');
  await page.fill('input[name="userId"]', TEST_USERS.admin.userId);
  await page.fill('input[name="password"]', TEST_USERS.admin.password);
  await page.click('button[type="submit"]');
  
  await page.waitForURL('/admin-menu', { timeout: 10000 }).catch(async () => {
    const errorElement = await page.locator('.text-red-600').first();
    if (await errorElement.isVisible()) {
      const errorText = await errorElement.textContent();
      throw new Error(`Login failed: ${errorText}`);
    }
    throw new Error('Login failed: Unknown error');
  });
}

export async function loginAsRegularUser(page: Page) {
  await page.goto('/login');
  await page.fill('input[name="userId"]', TEST_USERS.regular.userId);
  await page.fill('input[name="password"]', TEST_USERS.regular.password);
  await page.click('button[type="submit"]');
  
  await page.waitForURL('/menu', { timeout: 10000 }).catch(async () => {
    const errorElement = await page.locator('.text-red-600').first();
    if (await errorElement.isVisible()) {
      const errorText = await errorElement.textContent();
      throw new Error(`Login failed: ${errorText}`);
    }
    throw new Error('Login failed: Unknown error');
  });
}

export async function logout(page: Page) {
  await page.click('text=Logout');
  await expect(page).toHaveURL('/login');
}

export async function expectToBeOnLoginPage(page: Page) {
  await expect(page).toHaveURL('/login');
  await expect(page.locator('h2')).toContainText('CardDemo System');
}
