import { test, expect } from '@playwright/test';

test('has title', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle(/carddemo-frontend/);
});

test('login page loads', async ({ page }) => {
  await page.goto('/login');
  await expect(page.locator('h2')).toContainText('CardDemo System');
  await expect(page.locator('input[name="userId"]')).toBeVisible();
  await expect(page.locator('input[name="password"]')).toBeVisible();
});

test('can navigate to login page', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveURL('/login');
});

test('login form has submit button', async ({ page }) => {
  await page.goto('/login');
  await expect(page.locator('button[type="submit"]')).toBeVisible();
  await expect(page.locator('button[type="submit"]')).toContainText('Sign In');
});

test('login form validation shows errors for empty fields', async ({ page }) => {
  await page.goto('/login');
  await page.click('button[type="submit"]');
  
  await expect(page.locator('.text-red-600')).toHaveCount(2);
});

test('login form accepts input', async ({ page }) => {
  await page.goto('/login');
  
  await page.fill('input[name="userId"]', 'testuser');
  await page.fill('input[name="password"]', 'testpass');
  
  await expect(page.locator('input[name="userId"]')).toHaveValue('testuser');
  await expect(page.locator('input[name="password"]')).toHaveValue('testpass');
});
