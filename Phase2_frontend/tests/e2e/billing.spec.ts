import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';

test.describe('Bill Payment Workflows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
  });

  test.describe('Bill Payment Operations', () => {
    test('should display bill payment form for regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await expect(page).toHaveURL('/bill-payment');
      await expect(page.locator('h2')).toContainText('Bill Payment');
      await expect(page.locator('form')).toBeVisible();
    });

    test('should display bill payment form for admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/bill-payment');
      
      await expect(page).toHaveURL('/bill-payment');
      await expect(page.locator('h2')).toContainText('Bill Payment');
    });

    test('should show required form fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await expect(page.locator('input[name="accountNumber"]')).toBeVisible();
      await expect(page.locator('input[name="payeeId"]')).toBeVisible();
      await expect(page.locator('input[name="amount"]')).toBeVisible();
      await expect(page.locator('input[name="paymentDate"]')).toBeVisible();
      await expect(page.locator('textarea[name="description"]')).toBeVisible();
      await expect(page.locator('button[type="submit"]')).toBeVisible();
    });

    test('should process bill payment successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'ELECTRIC001');
      await page.fill('input[name="amount"]', '125.50');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      await page.fill('textarea[name="description"]', 'Monthly electric bill payment');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('Payment processed successfully');
    });

    test('should validate required fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(4);
      await expect(page.locator('input[name="accountNumber"] + .validation-error')).toContainText('Account number is required');
      await expect(page.locator('input[name="payeeId"] + .validation-error')).toContainText('Payee ID is required');
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount is required');
      await expect(page.locator('input[name="paymentDate"] + .validation-error')).toContainText('Payment date is required');
    });

    test('should validate account number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="accountNumber"] + .validation-error')).toContainText('Account number must be 11 digits');
    });

    test('should validate payment amount', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="amount"]', '-50.00');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount must be positive');
    });

    test('should validate future payment date', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="paymentDate"]', '2020-01-01');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="paymentDate"] + .validation-error')).toContainText('Payment date cannot be in the past');
    });

    test('should handle insufficient funds error', async ({ page }) => {
      await page.route('**/api/bill-payment', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Insufficient funds' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'ELECTRIC001');
      await page.fill('input[name="amount"]', '10000.00');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Insufficient funds');
    });

    test('should handle invalid payee error', async ({ page }) => {
      await page.route('**/api/bill-payment', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Invalid payee ID' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'INVALID001');
      await page.fill('input[name="amount"]', '100.00');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Invalid payee ID');
    });

    test('should clear form on reset', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'ELECTRIC001');
      await page.fill('input[name="amount"]', '125.50');
      
      await page.click('button:has-text("Reset")');
      
      await expect(page.locator('input[name="accountNumber"]')).toHaveValue('');
      await expect(page.locator('input[name="payeeId"]')).toHaveValue('');
      await expect(page.locator('input[name="amount"]')).toHaveValue('');
    });

    test('should navigate back to menu', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should handle network errors gracefully', async ({ page }) => {
      await page.route('**/api/bill-payment', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'ELECTRIC001');
      await page.fill('input[name="amount"]', '125.50');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Network error');
    });
  });

  test.describe('Bill Payment History', () => {
    test('should display payment history', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/bill-payment/history');
      
      await expect(page).toHaveURL('/bill-payment/history');
      await expect(page.locator('h2')).toContainText('Payment History');
      await expect(page.locator('.payment-item').first()).toBeVisible();
    });

    test('should show payment details', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/bill-payment/history');
      
      const firstPayment = page.locator('.payment-item').first();
      await expect(firstPayment.locator('.payment-date')).toBeVisible();
      await expect(firstPayment.locator('.payee-name')).toBeVisible();
      await expect(firstPayment.locator('.payment-amount')).toBeVisible();
      await expect(firstPayment.locator('.payment-status')).toBeVisible();
    });

    test('should filter payments by date range', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/bill-payment/history');
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-01-31');
      await page.click('button:has-text("Filter")');
      
      const payments = page.locator('.payment-item');
      await expect(payments.first()).toBeVisible();
    });

    test('should handle empty payment history', async ({ page }) => {
      await page.route('**/api/bill-payment/history', route => 
        route.fulfill({ status: 200, body: JSON.stringify({ payments: [], total: 0 }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/bill-payment/history');
      
      await expect(page.locator('.empty-state')).toBeVisible();
      await expect(page.locator('.empty-state')).toContainText('No payments found');
    });
  });
});
