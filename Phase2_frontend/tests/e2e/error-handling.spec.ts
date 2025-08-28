import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Error Handling Scenarios', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('Network Error Handling', () => {
    test('should handle login API network errors', async ({ page }) => {
      await page.route('**/api/auth/login', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Network error occurred');
      await expect(page).toHaveURL('/login');
    });

    test('should handle account API network errors', async ({ page }) => {
      await page.route('**/api/accounts/**', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to retrieve account information');
    });

    test('should handle card API network errors', async ({ page }) => {
      await page.route('**/api/cards', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to load cards');
    });

    test('should handle transaction API network errors', async ({ page }) => {
      await page.route('**/api/transactions', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to load transactions');
    });

    test('should handle bill payment API network errors', async ({ page }) => {
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
      await expect(page.locator('.error-message')).toContainText('Network error occurred');
    });

    test('should handle report API network errors', async ({ page }) => {
      await page.route('**/api/reports/**', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to generate report');
    });
  });

  test.describe('Server Error Handling', () => {
    test('should handle 500 internal server errors', async ({ page }) => {
      await page.route('**/api/accounts/**', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Server error occurred');
    });

    test('should handle 400 bad request errors', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Invalid transaction data' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '150.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Invalid transaction data');
    });

    test('should handle 401 unauthorized errors', async ({ page }) => {
      await page.route('**/api/admin/users', route => 
        route.fulfill({ status: 401, body: JSON.stringify({ message: 'Unauthorized access' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await expect(page).toHaveURL('/login');
      await expect(page.locator('.error-message')).toContainText('Session expired');
    });

    test('should handle 403 forbidden errors', async ({ page }) => {
      await page.route('**/api/cards/**', route => 
        route.fulfill({ status: 403, body: JSON.stringify({ message: 'Access denied' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=999');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Access denied');
    });

    test('should handle 404 not found errors', async ({ page }) => {
      await page.route('**/api/accounts/**', route => 
        route.fulfill({ status: 404, body: JSON.stringify({ message: 'Account not found' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '99999999999');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Account not found');
    });
  });

  test.describe('Timeout Handling', () => {
    test('should handle API request timeouts', async ({ page }) => {
      await page.route('**/api/accounts/**', route => {
        return new Promise(() => {}); // Never resolves
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
      
      await page.waitForTimeout(30000);
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Request timed out');
    });

    test('should show loading states during long operations', async ({ page }) => {
      await page.route('**/api/reports/**', route => {
        setTimeout(() => route.continue(), 2000);
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
      await expect(page.locator('button:has-text("Generate Report")')).toBeDisabled();
      
      await page.waitForTimeout(3000);
      await expect(page.locator('.loading-spinner')).not.toBeVisible();
      await expect(page.locator('button:has-text("Generate Report")')).toBeEnabled();
    });

    test('should allow canceling long-running operations', async ({ page }) => {
      await page.route('**/api/reports/**', route => {
        return new Promise(() => {}); // Never resolves
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      await expect(page.locator('.loading-spinner')).toBeVisible();
      
      await page.click('button:has-text("Cancel")');
      await expect(page.locator('.loading-spinner')).not.toBeVisible();
      await expect(page.locator('button:has-text("Generate Report")')).toBeEnabled();
    });
  });

  test.describe('Data Validation Errors', () => {
    test('should handle invalid account number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', 'INVALID');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Account number must be 11 digits');
    });

    test('should handle invalid card number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', 'INVALID');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Invalid card number format');
    });

    test('should handle invalid amount format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="amount"]', 'INVALID');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Invalid amount format');
    });

    test('should handle invalid date format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="paymentDate"]', 'INVALID');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Invalid date format');
    });
  });

  test.describe('Business Logic Errors', () => {
    test('should handle insufficient funds error', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Insufficient funds' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '10000.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Insufficient funds');
    });

    test('should handle card expired error', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Card has expired' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '100.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Card has expired');
    });

    test('should handle duplicate user ID error', async ({ page }) => {
      await page.route('**/api/admin/users', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'User ID already exists' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="userId"]', 'ADMIN001');
      await page.fill('input[name="firstName"]', 'John');
      await page.fill('input[name="lastName"]', 'Doe');
      await page.selectOption('select[name="userType"]', 'U');
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'password123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('User ID already exists');
    });

    test('should handle account locked error', async ({ page }) => {
      await page.route('**/api/auth/login', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Account is locked' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Account is locked');
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('Error Recovery', () => {
    test('should allow retry after network error', async ({ page }) => {
      let requestCount = 0;
      await page.route('**/api/accounts/**', route => {
        requestCount++;
        if (requestCount === 1) {
          route.abort();
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await page.click('button:has-text("Retry")');
      
      await expect(page.locator('.account-details')).toBeVisible();
    });

    test('should clear errors when navigating away', async ({ page }) => {
      await page.route('**/api/accounts/**', route => route.abort());
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToAccountView();
      await expect(page.locator('.error-message')).not.toBeVisible();
    });

    test('should clear errors when form is reset', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', 'INVALID');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      
      await page.click('button:has-text("Reset")');
      await expect(page.locator('.validation-error')).not.toBeVisible();
      await expect(page.locator('input[name="accountNumber"]')).toHaveValue('');
    });

    test('should show appropriate error messages for different error types', async ({ page }) => {
      const errorScenarios = [
        { status: 400, message: 'Bad Request', expectedText: 'Invalid request' },
        { status: 401, message: 'Unauthorized', expectedText: 'Session expired' },
        { status: 403, message: 'Forbidden', expectedText: 'Access denied' },
        { status: 404, message: 'Not Found', expectedText: 'Resource not found' },
        { status: 500, message: 'Internal Server Error', expectedText: 'Server error occurred' }
      ];
      
      for (const scenario of errorScenarios) {
        await page.route('**/api/accounts/**', route => 
          route.fulfill({ status: scenario.status, body: scenario.message })
        );
        
        await loginPage.goto();
        await loginPage.login('USER0001', 'user1234');
        await mainMenuPage.navigateToAccountView();
        
        await page.fill('input[name="accountNumber"]', '12345678901');
        await page.click('button[type="submit"]');
        
        if (scenario.status === 401) {
          await expect(page).toHaveURL('/login');
        } else {
          await expect(page.locator('.error-message')).toContainText(scenario.expectedText);
        }
        
        await page.unroute('**/api/accounts/**');
      }
    });
  });

  test.describe('Global Error Handling', () => {
    test('should handle JavaScript runtime errors gracefully', async ({ page }) => {
      await page.addInitScript(() => {
        window.addEventListener('error', (event) => {
          console.error('Runtime error:', event.error);
          document.body.innerHTML += '<div class="global-error">An unexpected error occurred</div>';
        });
      });
      
      await loginPage.goto();
      await page.evaluate(() => {
        throw new Error('Simulated runtime error');
      });
      
      await expect(page.locator('.global-error')).toBeVisible();
    });

    test('should handle unhandled promise rejections', async ({ page }) => {
      await page.addInitScript(() => {
        window.addEventListener('unhandledrejection', (event) => {
          console.error('Unhandled promise rejection:', event.reason);
          document.body.innerHTML += '<div class="promise-error">Promise rejection occurred</div>';
        });
      });
      
      await loginPage.goto();
      await page.evaluate(() => {
        Promise.reject(new Error('Simulated promise rejection'));
      });
      
      await expect(page.locator('.promise-error')).toBeVisible();
    });

    test('should maintain application state after errors', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.route('**/api/accounts/**', route => route.abort());
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      await expect(page.locator('.error-message')).toBeVisible();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await mainMenuPage.navigateToCards();
      await expect(page).toHaveURL('/cards');
    });
  });
});
