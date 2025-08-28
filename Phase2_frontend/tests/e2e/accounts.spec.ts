import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { AccountViewPage } from './page-objects/AccountViewPage';
import { MainMenuPage } from './page-objects/MainMenuPage';

test.describe('Account Management Workflows', () => {
  let loginPage: LoginPage;
  let accountViewPage: AccountViewPage;
  let mainMenuPage: MainMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    accountViewPage = new AccountViewPage(page);
    mainMenuPage = new MainMenuPage(page);
  });

  test.describe('Account View Operations', () => {
    test('should view account details as regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await expect(page).toHaveURL('/accounts/view');
      await accountViewPage.verifyAccountViewVisible();
    });

    test('should view account details as admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/accounts/view');
      
      await expect(page).toHaveURL('/accounts/view');
      await accountViewPage.verifyAccountViewVisible();
    });

    test('should search for account by account number', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.verifyAccountDetailsVisible();
    });

    test('should handle invalid account number search', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('99999999999');
      await accountViewPage.verifyErrorMessage('Account not found');
    });

    test('should validate account number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('123');
      await accountViewPage.verifyValidationError('Account number must be 11 digits');
    });

    test('should display account balance and details', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.verifyAccountBalance();
      await accountViewPage.verifyAccountHolderName();
      await accountViewPage.verifyAccountStatus();
    });
  });

  test.describe('Account Update Operations', () => {
    test('should navigate to account update from account view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.clickUpdateAccount();
      await expect(page).toHaveURL('/accounts/update');
    });

    test('should update account information successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="customerName"]', 'Updated Name');
      await page.fill('input[name="customerAddress"]', 'Updated Address');
      await page.fill('input[name="customerPhone"]', '555-0123');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.success-message')).toBeVisible();
    });

    test('should validate required fields in account update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.text-red-600')).toHaveCount(4);
    });

    test('should handle account update API errors', async ({ page }) => {
      await page.route('**/api/accounts/**', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.fill('input[name="customerName"]', 'Test Name');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
    });

    test('should navigate back to menu from account update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.click('text=Back to Menu');
      await expect(page).toHaveURL('/menu');
    });
  });

  test.describe('Account Navigation Flows', () => {
    test('should navigate between account view and update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.clickUpdateAccount();
      await expect(page).toHaveURL('/accounts/update');
      
      await page.click('text=Back');
      await expect(page).toHaveURL('/accounts/view');
    });

    test('should maintain account context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      const accountNumber = await page.inputValue('input[name="accountNumber"]');
      
      await accountViewPage.clickUpdateAccount();
      const updateAccountNumber = await page.inputValue('input[name="accountNumber"]');
      
      expect(accountNumber).toBe(updateAccountNumber);
    });

    test('should handle direct URL access to account pages', async ({ page }) => {
      await page.goto('/accounts/view');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
    });
  });

  test.describe('Account Error Handling', () => {
    test('should handle network timeouts gracefully', async ({ page }) => {
      await page.route('**/api/accounts/**', route => {
        return new Promise(() => {});
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await expect(page.locator('.loading-spinner')).toBeVisible();
    });

    test('should handle server errors with user-friendly messages', async ({ page }) => {
      await page.route('**/api/accounts/**', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.verifyErrorMessage('Unable to retrieve account information');
    });

    test('should clear previous results on new search', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await accountViewPage.searchAccount('12345678901');
      await accountViewPage.verifyAccountDetailsVisible();
      
      await accountViewPage.searchAccount('99999999999');
      await accountViewPage.verifyErrorMessage('Account not found');
      await accountViewPage.verifyAccountDetailsNotVisible();
    });
  });
});
