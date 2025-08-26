import { test, expect } from '@playwright/test';
import { loginAsRegularUser, expectToBeOnLoginPage } from './utils/auth';

test.describe('Protected Routes', () => {
  test('should redirect unauthenticated users to login', async ({ page }) => {
    await page.goto('/menu');
    await expectToBeOnLoginPage(page);
    
    await page.goto('/accounts/view');
    await expectToBeOnLoginPage(page);
    
    await page.goto('/cards');
    await expectToBeOnLoginPage(page);
    
    await page.goto('/transactions');
    await expectToBeOnLoginPage(page);
  });

  test('should redirect regular users away from admin routes', async ({ page }) => {
    await loginAsRegularUser(page);
    
    await page.goto('/admin-menu');
    await expect(page).toHaveURL('/menu');
    
    await page.goto('/admin/users');
    await expect(page).toHaveURL('/menu');
    
    await page.goto('/admin/users/add');
    await expect(page).toHaveURL('/menu');
  });

  test('should redirect to login for unknown routes', async ({ page }) => {
    await page.goto('/unknown-route');
    await expectToBeOnLoginPage(page);
    
    await page.goto('/invalid/path');
    await expectToBeOnLoginPage(page);
  });

  test('should allow access to protected routes after login', async ({ page }) => {
    await loginAsRegularUser(page);
    
    await page.goto('/accounts/view');
    await expect(page).toHaveURL('/accounts/view');
    
    await page.goto('/cards');
    await expect(page).toHaveURL('/cards');
    
    await page.goto('/transactions');
    await expect(page).toHaveURL('/transactions');
  });
});
