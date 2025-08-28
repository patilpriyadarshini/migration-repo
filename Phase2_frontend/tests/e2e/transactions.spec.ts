import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';

test.describe('Transaction Management Workflows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
  });

  test.describe('Transaction List Operations', () => {
    test('should display transaction list for regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page).toHaveURL('/transactions');
      await expect(page.locator('h2')).toContainText('Transaction List');
      const transactionItems = page.locator('.transaction-item');
      await expect(transactionItems.first()).toBeVisible();
    });

    test('should display transaction list for admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/transactions');
      
      await expect(page).toHaveURL('/transactions');
      await expect(page.locator('h2')).toContainText('Transaction List');
    });

    test('should show transaction details in list view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      const firstTransaction = page.locator('.transaction-item').first();
      await expect(firstTransaction.locator('.transaction-id')).toBeVisible();
      await expect(firstTransaction.locator('.transaction-date')).toBeVisible();
      await expect(firstTransaction.locator('.transaction-amount')).toBeVisible();
      await expect(firstTransaction.locator('.transaction-type')).toBeVisible();
      await expect(firstTransaction.locator('.merchant-name')).toBeVisible();
    });

    test('should filter transactions by date range', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-01-31');
      await page.click('button:has-text("Filter")');
      
      const transactions = page.locator('.transaction-item');
      await expect(transactions.first()).toBeVisible();
    });

    test('should filter transactions by type', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.click('button:has-text("Filter")');
      
      const purchaseTransactions = page.locator('.transaction-item[data-type="PURCHASE"]');
      await expect(purchaseTransactions.first()).toBeVisible();
    });

    test('should search transactions by merchant', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.fill('input[name="merchantSearch"]', 'Amazon');
      await page.click('button:has-text("Search")');
      
      const amazonTransactions = page.locator('.transaction-item:has-text("Amazon")');
      await expect(amazonTransactions.first()).toBeVisible();
    });

    test('should sort transactions by date', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.selectOption('select[name="sortBy"]', 'date-desc');
      await page.click('button:has-text("Sort")');
      
      const firstDate = await page.textContent('.transaction-item:first-child .transaction-date');
      const secondDate = await page.textContent('.transaction-item:nth-child(2) .transaction-date');
      
      expect(new Date(firstDate!).getTime()).toBeGreaterThanOrEqual(new Date(secondDate!).getTime());
    });

    test('should paginate transaction results', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.pagination')).toBeVisible();
      await page.click('.pagination .next-page');
      
      await expect(page.locator('.current-page')).toContainText('2');
    });
  });

  test.describe('Transaction Detail Operations', () => {
    test('should view transaction details from list', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('.transaction-item:first-child .view-details');
      await expect(page).toHaveURL('/transactions/view');
      await expect(page.locator('h2')).toContainText('Transaction Details');
    });

    test('should display comprehensive transaction information', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=1');
      
      await expect(page.locator('.transaction-id')).toBeVisible();
      await expect(page.locator('.transaction-date')).toBeVisible();
      await expect(page.locator('.transaction-amount')).toBeVisible();
      await expect(page.locator('.transaction-type')).toBeVisible();
      await expect(page.locator('.merchant-name')).toBeVisible();
      await expect(page.locator('.merchant-category')).toBeVisible();
      await expect(page.locator('.card-number')).toBeVisible();
      await expect(page.locator('.authorization-code')).toBeVisible();
    });

    test('should show transaction status and processing details', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=1');
      
      await expect(page.locator('.transaction-status')).toBeVisible();
      await expect(page.locator('.processing-date')).toBeVisible();
      await expect(page.locator('.settlement-date')).toBeVisible();
    });

    test('should handle invalid transaction ID', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=999');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Transaction not found');
    });

    test('should allow printing transaction receipt', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=1');
      
      await page.click('button:has-text("Print Receipt")');
      await expect(page.locator('.print-preview')).toBeVisible();
    });
  });

  test.describe('Transaction Add Operations', () => {
    test('should navigate to add transaction form', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('button:has-text("Add Transaction")');
      await expect(page).toHaveURL('/transactions/add');
      await expect(page.locator('h2')).toContainText('Add Transaction');
    });

    test('should add new transaction successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '150.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      await page.fill('textarea[name="description"]', 'Test transaction');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('Transaction added successfully');
    });

    test('should validate required fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(5);
      await expect(page.locator('input[name="cardNumber"] + .validation-error')).toContainText('Card number is required');
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount is required');
    });

    test('should validate card number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="cardNumber"] + .validation-error')).toContainText('Invalid card number format');
    });

    test('should validate transaction amount', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="amount"]', '-50.00');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount must be positive');
    });

    test('should handle transaction creation API errors', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Insufficient funds' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '5000.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Insufficient funds');
    });

    test('should clear form on reset', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4000123456789012');
      await page.fill('input[name="amount"]', '150.00');
      await page.click('button:has-text("Reset")');
      
      await expect(page.locator('input[name="cardNumber"]')).toHaveValue('');
      await expect(page.locator('input[name="amount"]')).toHaveValue('');
    });
  });

  test.describe('Transaction Navigation Flows', () => {
    test('should navigate back to transaction list from detail view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=1');
      
      await page.click('button:has-text("Back to Transactions")');
      await expect(page).toHaveURL('/transactions');
    });

    test('should navigate back to menu from transaction list', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should handle role-based navigation correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/transactions');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should maintain transaction context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('.transaction-item:first-child .view-details');
      const transactionId = await page.textContent('.transaction-id');
      
      await page.click('button:has-text("Back to Transactions")');
      await page.click('.transaction-item:first-child .view-details');
      const sameTransactionId = await page.textContent('.transaction-id');
      
      expect(transactionId).toBe(sameTransactionId);
    });
  });

  test.describe('Transaction Error Handling', () => {
    test('should handle transaction loading errors', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to load transactions');
    });

    test('should handle network timeouts', async ({ page }) => {
      await page.route('**/api/transactions', route => {
        return new Promise(() => {});
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
    });

    test('should handle empty transaction list', async ({ page }) => {
      await page.route('**/api/transactions', route => 
        route.fulfill({ status: 200, body: JSON.stringify({ transactions: [], total: 0 }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('.empty-state')).toBeVisible();
      await expect(page.locator('.empty-state')).toContainText('No transactions found');
    });

    test('should retry failed requests', async ({ page }) => {
      let requestCount = 0;
      await page.route('**/api/transactions', route => {
        requestCount++;
        if (requestCount === 1) {
          route.fulfill({ status: 500, body: 'Server Error' });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToTransactions();
      
      await page.click('button:has-text("Retry")');
      const transactionItems = page.locator('.transaction-item');
      await expect(transactionItems.first()).toBeVisible();
    });
  });
});
