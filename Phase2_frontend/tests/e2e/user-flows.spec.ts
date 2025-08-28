import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('End-to-End User Flows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('Complete Regular User Journey', () => {
    test('should complete full account management workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Update Account")');
      await expect(page).toHaveURL('/accounts/update');
      
      await page.fill('input[name="customerName"]', 'Updated Customer Name');
      await page.fill('input[name="customerAddress"]', '123 Updated Street');
      await page.fill('input[name="customerPhone"]', '555-0123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('Account updated successfully');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should complete full card management workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToCards();
      await expect(page.locator('.card-item').first()).toBeVisible();
      
      await page.click('.card-item:first-child .view-details');
      await expect(page).toHaveURL('/cards/detail');
      await expect(page.locator('.card-details')).toBeVisible();
      
      await page.click('button:has-text("Update Card")');
      await expect(page).toHaveURL('/cards/update');
      
      await page.fill('input[name="cardLimit"]', '5000');
      await page.selectOption('select[name="cardStatus"]', 'ACTIVE');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Cards")');
      await expect(page).toHaveURL('/cards');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should complete full transaction workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToTransactions();
      await expect(page.locator('.transaction-item').first()).toBeVisible();
      
      await page.click('.transaction-item:first-child .view-details');
      await expect(page).toHaveURL('/transactions/view');
      await expect(page.locator('.transaction-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Transactions")');
      await expect(page).toHaveURL('/transactions');
      
      await page.click('button:has-text("Add Transaction")');
      await expect(page).toHaveURL('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '150.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      await page.fill('textarea[name="description"]', 'Test transaction');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should complete full bill payment workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToBillPayment();
      await expect(page).toHaveURL('/bill-payment');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="payeeId"]', 'ELECTRIC001');
      await page.fill('input[name="amount"]', '125.50');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      await page.fill('textarea[name="description"]', 'Monthly electric bill');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Payment History")');
      await expect(page).toHaveURL('/bill-payment/history');
      await expect(page.locator('.payment-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should complete full reporting workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToReports();
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("Monthly Report")');
      await expect(page).toHaveURL('/reports/monthly');
      
      await page.selectOption('select[name="month"]', '01');
      await page.selectOption('select[name="year"]', '2024');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-content')).toBeVisible();
      
      const downloadPromise = page.waitForEvent('download');
      await page.click('button:has-text("Export PDF")');
      const download = await downloadPromise;
      expect(download.suggestedFilename()).toContain('monthly-report');
      
      await page.click('button:has-text("Custom Report")');
      await expect(page).toHaveURL('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-03-31');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-content')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should handle complete user session from login to logout', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToCards();
      await expect(page.locator('.card-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToTransactions();
      await expect(page.locator('.transaction-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await page.click('button:has-text("Logout")');
      await expect(page).toHaveURL('/login');
      
      const userId = await page.evaluate(() => localStorage.getItem('userId'));
      const userType = await page.evaluate(() => localStorage.getItem('userType'));
      expect(userId).toBeNull();
      expect(userType).toBeNull();
    });
  });

  test.describe('Complete Admin User Journey', () => {
    test('should complete full admin user management workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      await expect(page.locator('.user-item').first()).toBeVisible();
      
      await page.click('button:has-text("Add User")');
      await expect(page).toHaveURL('/admin/users/add');
      
      await page.fill('input[name="userId"]', 'NEWUSER1');
      await page.fill('input[name="firstName"]', 'John');
      await page.fill('input[name="lastName"]', 'Doe');
      await page.selectOption('select[name="userType"]', 'U');
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'password123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Update User")');
      await expect(page).toHaveURL('/admin/users/update');
      
      await page.fill('input[name="searchUserId"]', 'NEWUSER1');
      await page.click('button:has-text("Search")');
      await expect(page.locator('.user-details')).toBeVisible();
      
      await page.fill('input[name="firstName"]', 'Jane');
      await page.click('button[type="submit"]');
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Admin Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should complete admin access to regular user features', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToCards();
      await expect(page.locator('.card-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToTransactions();
      await expect(page.locator('.transaction-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should complete admin reporting workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToReports();
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("System Report")');
      await expect(page).toHaveURL('/reports/system');
      await expect(page.locator('.system-metrics')).toBeVisible();
      
      await page.click('button:has-text("User Activity Report")');
      await expect(page).toHaveURL('/reports/user-activity');
      await expect(page.locator('.activity-summary')).toBeVisible();
      
      await page.click('button:has-text("Monthly Report")');
      await expect(page).toHaveURL('/reports/monthly');
      await page.click('button:has-text("Generate Report")');
      await expect(page.locator('.report-content')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should handle complete admin session with mixed operations', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page.locator('.user-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Admin Menu")');
      
      await adminMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await adminMenuPage.navigateToCards();
      await expect(page.locator('.card-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await adminMenuPage.navigateToReports();
      await page.click('button:has-text("System Report")');
      await expect(page.locator('.system-metrics')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await page.click('button:has-text("Logout")');
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('Cross-User Type Workflows', () => {
    test('should handle user type switching scenarios', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await page.click('button:has-text("Logout")');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page.locator('.user-item').first()).toBeVisible();
      
      await page.click('button:has-text("Back to Admin Menu")');
      await adminMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await page.click('button:has-text("Logout")');
      await expect(page).toHaveURL('/login');
    });

    test('should prevent regular user from accessing admin features', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/admin-menu');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/admin/users/add');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/reports/system');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
    });

    test('should maintain proper context for each user type', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      let userId = await page.evaluate(() => localStorage.getItem('userId'));
      let userType = await page.evaluate(() => localStorage.getItem('userType'));
      expect(userId).toBe('USER0001');
      expect(userType).toBe('U');
      
      await page.click('button:has-text("Logout")');
      
      await loginPage.login('ADMIN001', 'admin123');
      
      userId = await page.evaluate(() => localStorage.getItem('userId'));
      userType = await page.evaluate(() => localStorage.getItem('userType'));
      expect(userId).toBe('ADMIN001');
      expect(userType).toBe('A');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Back to Admin Menu")');
      await adminMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
    });
  });

  test.describe('Error Recovery Workflows', () => {
    test('should recover from network errors during user workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.route('**/api/accounts/**', route => route.abort());
      
      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      
      await page.unroute('**/api/accounts/**');
      await page.click('button:has-text("Retry")');
      
      await expect(page.locator('.account-details')).toBeVisible();
      
      await page.click('button:has-text("Update Account")');
      await expect(page).toHaveURL('/accounts/update');
    });

    test('should handle session expiry during workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.route('**/api/accounts/**', route => 
        route.fulfill({ status: 401, body: JSON.stringify({ message: 'Session expired' }) })
      );
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/accounts/view'); // Should preserve intended route
      
      await page.unroute('**/api/accounts/**');
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.account-details')).toBeVisible();
    });

    test('should handle validation errors during complex workflow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      await page.click('button:has-text("Add Transaction")');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.validation-error')).toHaveCount(5);
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await expect(page.locator('input[name="cardNumber"] + .validation-error')).not.toBeVisible();
      
      await page.fill('input[name="amount"]', '150.00');
      await expect(page.locator('input[name="amount"] + .validation-error')).not.toBeVisible();
      
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.success-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Transactions")');
      await expect(page).toHaveURL('/transactions');
    });
  });

  test.describe('Performance and Load Workflows', () => {
    test('should handle large data sets in user workflow', async ({ page }) => {
      await page.route('**/api/transactions', route => {
        const largeDataSet = {
          transactions: Array.from({ length: 1000 }, (_, i) => ({
            id: i + 1,
            date: '2024-01-01',
            amount: 100.00,
            type: 'PURCHASE',
            merchant: `Merchant ${i + 1}`,
            status: 'COMPLETED'
          })),
          total: 1000
        };
        route.fulfill({ status: 200, body: JSON.stringify(largeDataSet) });
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.transaction-item').first()).toBeVisible();
      await expect(page.locator('.pagination')).toBeVisible();
      
      await page.click('.pagination .next-page');
      await expect(page.locator('.current-page')).toContainText('2');
      
      await page.fill('input[name="merchantSearch"]', 'Merchant 500');
      await page.click('button:has-text("Search")');
      await expect(page.locator('.transaction-item')).toHaveCount(1);
    });

    test('should handle slow API responses during workflow', async ({ page }) => {
      await page.route('**/api/accounts/**', route => {
        setTimeout(() => route.continue(), 3000);
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
      await expect(page.locator('button[type="submit"]')).toBeDisabled();
      
      await expect(page.locator('.account-details')).toBeVisible({ timeout: 10000 });
      await expect(page.locator('.loading-spinner')).not.toBeVisible();
      
      await page.click('button:has-text("Update Account")');
      await expect(page).toHaveURL('/accounts/update');
    });
  });
});
