import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Navigation Flows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('Regular User Navigation', () => {
    test('should navigate through main menu options', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await expect(page).toHaveURL('/menu');
      await mainMenuPage.verifyMainMenuVisible();
      
      await mainMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToTransactions();
      await expect(page).toHaveURL('/transactions');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate through account management flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await page.click('button:has-text("Update Account")');
      await expect(page).toHaveURL('/accounts/update');
      
      await page.click('button:has-text("Back")');
      await expect(page).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate through card management flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
      
      await page.click('.card-item:first-child .view-details');
      await expect(page).toHaveURL('/cards/detail');
      
      await page.click('button:has-text("Update Card")');
      await expect(page).toHaveURL('/cards/update');
      
      await page.click('button:has-text("Back")');
      await expect(page).toHaveURL('/cards/detail');
      
      await page.click('button:has-text("Back to Cards")');
      await expect(page).toHaveURL('/cards');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate through transaction management flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToTransactions();
      await expect(page).toHaveURL('/transactions');
      
      await page.click('.transaction-item:first-child .view-details');
      await expect(page).toHaveURL('/transactions/view');
      
      await page.click('button:has-text("Back to Transactions")');
      await expect(page).toHaveURL('/transactions');
      
      await page.click('button:has-text("Add Transaction")');
      await expect(page).toHaveURL('/transactions/add');
      
      await page.click('button:has-text("Back")');
      await expect(page).toHaveURL('/transactions');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate through bill payment flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToBillPayment();
      await expect(page).toHaveURL('/bill-payment');
      
      await page.click('button:has-text("Payment History")');
      await expect(page).toHaveURL('/bill-payment/history');
      
      await page.click('button:has-text("Back to Bill Payment")');
      await expect(page).toHaveURL('/bill-payment');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate through reports flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToReports();
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("Monthly Report")');
      await expect(page).toHaveURL('/reports/monthly');
      
      await page.click('button:has-text("Back to Reports")');
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("Yearly Report")');
      await expect(page).toHaveURL('/reports/yearly');
      
      await page.click('button:has-text("Back to Reports")');
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("Custom Report")');
      await expect(page).toHaveURL('/reports/custom');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });
  });

  test.describe('Admin User Navigation', () => {
    test('should navigate through admin menu options', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await expect(page).toHaveURL('/admin-menu');
      await adminMenuPage.verifyAdminMenuVisible();
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Back to Admin Menu")');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should navigate through user management flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Add User")');
      await expect(page).toHaveURL('/admin/users/add');
      
      await page.click('button:has-text("Back to User Management")');
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Update User")');
      await expect(page).toHaveURL('/admin/users/update');
      
      await page.click('button:has-text("Back to User Management")');
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Delete User")');
      await expect(page).toHaveURL('/admin/users/delete');
      
      await page.click('button:has-text("Back to Admin Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should navigate between admin and regular user features', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
    });

    test('should navigate through admin reports flow', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToReports();
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("System Report")');
      await expect(page).toHaveURL('/reports/system');
      
      await page.click('button:has-text("Back to Reports")');
      await expect(page).toHaveURL('/reports');
      
      await page.click('button:has-text("User Activity Report")');
      await expect(page).toHaveURL('/reports/user-activity');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });
  });

  test.describe('Browser Navigation', () => {
    test('should handle browser back button correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      await expect(page).toHaveURL('/accounts/view');
      
      await mainMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
      
      await mainMenuPage.navigateToTransactions();
      await expect(page).toHaveURL('/transactions');
      
      await page.goBack();
      await expect(page).toHaveURL('/cards');
      
      await page.goBack();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.goBack();
      await expect(page).toHaveURL('/menu');
    });

    test('should handle browser forward button correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      await mainMenuPage.navigateToCards();
      await page.goBack();
      await expect(page).toHaveURL('/accounts/view');
      
      await page.goForward();
      await expect(page).toHaveURL('/cards');
    });

    test('should handle page refresh correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.reload();
      await expect(page).toHaveURL('/accounts/view');
      await expect(page.locator('h2')).toContainText('Account View');
    });

    test('should handle direct URL access', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/cards');
      await expect(page).toHaveURL('/cards');
      
      await page.goto('/transactions');
      await expect(page).toHaveURL('/transactions');
      
      await page.goto('/bill-payment');
      await expect(page).toHaveURL('/bill-payment');
    });

    test('should handle URL parameters correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/cards/detail?cardId=1');
      await expect(page).toHaveURL('/cards/detail?cardId=1');
      
      await page.goto('/transactions/view?transactionId=1');
      await expect(page).toHaveURL('/transactions/view?transactionId=1');
      
      await page.goto('/reports/custom?startDate=2024-01-01&endDate=2024-01-31');
      await expect(page).toHaveURL('/reports/custom?startDate=2024-01-01&endDate=2024-01-31');
    });
  });

  test.describe('Navigation State Management', () => {
    test('should maintain form state during navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToAccountView();
      
      await expect(page.locator('input[name="accountNumber"]')).toHaveValue('');
    });

    test('should maintain search results during navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.fill('input[name="merchantSearch"]', 'Amazon');
      await page.click('button:has-text("Search")');
      
      await page.click('.transaction-item:first-child .view-details');
      await expect(page).toHaveURL('/transactions/view');
      
      await page.click('button:has-text("Back to Transactions")');
      await expect(page).toHaveURL('/transactions');
      
      await expect(page.locator('input[name="merchantSearch"]')).toHaveValue('Amazon');
    });

    test('should handle navigation with unsaved changes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="customerName"]', 'Updated Name');
      
      await page.click('button:has-text("Back to Menu")');
      
      await expect(page.locator('.confirmation-dialog')).toBeVisible();
      await expect(page.locator('.confirmation-dialog')).toContainText('You have unsaved changes');
      
      await page.click('button:has-text("Cancel")');
      await expect(page).toHaveURL('/accounts/update');
      
      await page.click('button:has-text("Back to Menu")');
      await page.click('button:has-text("Leave")');
      await expect(page).toHaveURL('/menu');
    });

    test('should preserve user context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToCards();
      
      await page.click('button:has-text("Back to Menu")');
      await mainMenuPage.navigateToTransactions();
      
      const userId = await page.evaluate(() => localStorage.getItem('userId'));
      const userType = await page.evaluate(() => localStorage.getItem('userType'));
      
      expect(userId).toBe('USER0001');
      expect(userType).toBe('U');
    });
  });

  test.describe('Navigation Error Handling', () => {
    test('should handle navigation to non-existent pages', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/non-existent-page');
      await expect(page).toHaveURL('/menu'); // Should redirect to menu
    });

    test('should handle navigation with invalid parameters', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/cards/detail?cardId=invalid');
      await expect(page.locator('.error-message')).toContainText('Invalid card ID');
    });

    test('should handle navigation during loading states', async ({ page }) => {
      await page.route('**/api/accounts/**', route => {
        setTimeout(() => route.continue(), 2000);
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should handle navigation after session expiry', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.evaluate(() => {
        localStorage.removeItem('userId');
        localStorage.removeItem('userType');
      });
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('Breadcrumb Navigation', () => {
    test('should show correct breadcrumbs for nested pages', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToCards();
      await page.click('.card-item:first-child .view-details');
      await page.click('button:has-text("Update Card")');
      
      await expect(page.locator('.breadcrumb')).toContainText('Menu > Cards > Card Detail > Update Card');
    });

    test('should allow navigation via breadcrumbs', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToCards();
      await page.click('.card-item:first-child .view-details');
      await page.click('button:has-text("Update Card")');
      
      await page.click('.breadcrumb a:has-text("Cards")');
      await expect(page).toHaveURL('/cards');
      
      await page.click('.breadcrumb a:has-text("Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should show different breadcrumbs for admin users', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');
      
      await expect(page.locator('.breadcrumb')).toContainText('Admin Menu > User Management > Add User');
    });
  });
});
