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
  });
});
