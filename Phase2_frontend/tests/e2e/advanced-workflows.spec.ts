import { test, expect } from './setup';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';
import { AccountViewPage } from './page-objects/AccountViewPage';

test.describe('Advanced Complex Workflows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;
  let accountViewPage: AccountViewPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
    accountViewPage = new AccountViewPage(page);
  });

  test.describe('Multi-Step Complex User Journeys', () => {
    test('should complete complex admin workflow with multiple operations', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      await adminMenuPage.verifyAdminMenuVisible();

      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Add User")');
      await expect(page).toHaveURL('/admin/users/add');
      
      await page.fill('input[name="userId"]', 'TEST0001');
      await page.fill('input[name="firstName"]', 'Test');
      await page.fill('input[name="lastName"]', 'User');
      await page.check('input[name="userType"][value="U"]');
      await page.fill('input[name="password"]', 'test1234');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.bg-green-50')).toContainText('User created successfully');

      await adminMenuPage.navigateToUpdateUser();
      await expect(page).toHaveURL('/admin/users/update');
      
      await page.fill('input[name="userId"]', 'TEST0001');
      await page.click('button:has-text("Search")');
      await expect(page.locator('.user-details')).toBeVisible();
      
      await page.fill('input[name="firstName"]', 'Updated Test');
      await page.fill('input[name="lastName"]', 'Updated User');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.bg-green-50')).toContainText('User updated successfully');

      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      await expect(page.locator('text=Updated Test')).toBeVisible();
      await expect(page.locator('text=Updated User')).toBeVisible();

      await adminMenuPage.logout();
      await expect(page).toHaveURL('/login');
      
      const sessionData = await page.evaluate(() => ({
        userId: localStorage.getItem('userId'),
        userType: localStorage.getItem('userType')
      }));
      
      expect(sessionData.userId).toBeNull();
      expect(sessionData.userType).toBeNull();
    });

    test('should handle complex regular user workflow with error recovery', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');

      await mainMenuPage.navigateToAccountView();
      
      await page.route('**/api/accounts/**', route => {
        if (Math.random() < 0.3) {
          route.abort('failed');
        } else {
          route.continue();
        }
      });

      let retryCount = 0;
      const maxRetries = 3;
      
      while (retryCount < maxRetries) {
        await page.fill('input[name="accountNumber"]', '12345678901');
        await page.click('button[type="submit"]');
        
        try {
          await expect(page.locator('.account-details')).toBeVisible({ timeout: 5000 });
          break;
        } catch (error) {
          retryCount++;
          if (retryCount < maxRetries) {
            await page.click('button:has-text("Retry")');
            await page.waitForTimeout(1000);
          }
        }
      }

      await mainMenuPage.navigateToTransactions();
      await expect(page).toHaveURL('/transactions/list');
      
      await page.selectOption('select[name="transactionType"]', 'purchase');
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-12-31');
      await page.fill('input[name="minAmount"]', '100.00');
      await page.fill('input[name="maxAmount"]', '1000.00');
      
      await page.click('button:has-text("Apply Filters")');
      await expect(page.locator('h2')).toContainText('Transaction List');

      await page.click('button:has-text("Add Transaction")');
      await expect(page).toHaveURL('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '4111111111111111');
      await page.fill('input[name="amount"]', '250.75');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.selectOption('select[name="category"]', 'dining');
      await page.fill('textarea[name="description"]', 'Complex transaction test with detailed description');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.success-message')).toContainText('Transaction added successfully');

      await mainMenuPage.navigateToBillPayment();
      await page.fill('input[name="payeeAccount"]', '9876543210');
      await page.fill('input[name="amount"]', '150.00');
      await page.fill('input[name="paymentDate"]', '2024-12-31');
      await page.fill('textarea[name="memo"]', 'Monthly payment - complex workflow test');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.payment-confirmation')).toBeVisible();
      await expect(page.locator('.confirmation-number')).toBeVisible();
    });

    test('should handle sequential complex user operations', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      
      await page.click('button:has-text("Add User")');
      await expect(page).toHaveURL('/admin/users/add');
      
      await page.fill('input[name="userId"]', 'SEQ0001');
      await page.fill('input[name="firstName"]', 'Sequential');
      await page.fill('input[name="lastName"]', 'Test');
      await page.check('input[name="userType"][value="U"]');
      await page.fill('input[name="password"]', 'seq1234');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.bg-green-50')).toContainText('User created successfully');
      
      await page.getByRole('button', { name: 'Logout' }).click();
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      await page.getByRole('button', { name: '1 View Account' }).click();
      await expect(page).toHaveURL(/.*account-view.*/);
      await expect(page.locator('h2')).toContainText('Account Details');
      
      await page.goBack();
      await page.getByRole('button', { name: '3 Transaction List' }).click();
      await expect(page).toHaveURL(/.*transactions.*/);
      await expect(page.locator('h2')).toContainText('Transaction List');
    });
  });

  test.describe('Advanced Data Validation and Edge Cases', () => {
    test('should handle complex form validation with dynamic rules', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');

      await page.fill('input[name="userId"]', 'A');
      await page.locator('input[name="userId"]').blur();
      await expect(page.locator('.text-red-600')).toContainText('User ID must be exactly 8 characters');

      await page.fill('input[name="userId"]', 'VALID001');
      await page.fill('input[name="firstName"]', 'Valid');
      await page.fill('input[name="lastName"]', 'User');
      await page.check('input[name="userType"][value="U"]');
      await page.fill('input[name="password"]', 'valid123');
      
      await page.click('button[type="submit"]');
      await expect(page.locator('.bg-green-50')).toContainText('User created successfully');
      
      await adminMenuPage.navigateToUserManagement();
      await expect(page).toHaveURL('/admin/users');
      await expect(page.locator('h2')).toContainText('User Management');
    });

    test('should handle large dataset operations with pagination and filtering', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.route('**/api/transactions/**', async (route) => {
        const url = route.request().url();
        if (url.includes('list')) {
          const largeDataset = Array.from({ length: 1000 }, (_, i) => ({
            id: `TXN${String(i + 1).padStart(6, '0')}`,
            amount: Math.random() * 1000,
            merchant: `Merchant ${i + 1}`,
            date: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
            category: ['dining', 'shopping', 'gas', 'groceries'][Math.floor(Math.random() * 4)],
            status: ['completed', 'pending', 'failed'][Math.floor(Math.random() * 3)]
          }));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              transactions: largeDataset.slice(0, 50), // First page
              totalCount: 1000,
              currentPage: 1,
              totalPages: 20
            })
          });
        } else {
          await route.continue();
        }
      });

      await mainMenuPage.navigateToTransactions();
      
      await expect(page.locator('[data-testid="transaction-item"]')).toHaveCount(1);
      await expect(page.locator('h2')).toContainText('Transaction List');
      
      await page.selectOption('select[name="category"]', 'dining');
      await page.fill('input[name="minAmount"]', '100');
      await page.fill('input[name="maxAmount"]', '500');
      await page.selectOption('select[name="status"]', 'completed');
      
      await page.click('button:has-text("Apply Filters")');
      await expect(page.locator('.filter-results')).toContainText('Filtered results');
      
      await page.click('th:has-text("Amount")');
      await expect(page.locator('.sort-indicator')).toBeVisible();
      
      await page.click('button:has-text("Next")');
      await expect(page.locator('.pagination-info')).toContainText('51-100 of 1000');
      
      await page.fill('input[name="pageNumber"]', '10');
      await page.press('input[name="pageNumber"]', 'Enter');
      await expect(page.locator('.pagination-info')).toContainText('451-500 of 1000');
    });

    test('should handle complex API error scenarios with retry mechanisms', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      let requestCount = 0;
      
      await page.route('**/api/accounts/**', async (route) => {
        requestCount++;
        
        if (requestCount === 1) {
          await new Promise(resolve => setTimeout(resolve, 10000));
          await route.abort('timedout');
        } else if (requestCount === 2) {
          await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({
              error: 'Internal server error',
              message: 'Database connection failed'
            })
          });
        } else if (requestCount === 3) {
          await route.fulfill({
            status: 429,
            contentType: 'application/json',
            body: JSON.stringify({
              error: 'Too many requests',
              retryAfter: 2
            })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              acctId: 12345678901,
              acctActiveStatus: 'Y',
              acctCurrBal: 1500.00,
              acctCreditLimit: 5000.00,
              customerFirstName: 'Alice',
              customerLastName: 'Johnson'
            })
          });
        }
      });

      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toContainText('Request timed out', { timeout: 15000 });
      await expect(page.locator('button:has-text("Retry")')).toBeVisible();
      
      await page.click('button:has-text("Retry")');
      await expect(page.locator('.error-message')).toContainText('Server error occurred');
      
      await page.click('button:has-text("Retry")');
      await expect(page.locator('.error-message')).toContainText('Too many requests');
      await expect(page.locator('.retry-countdown')).toBeVisible();
      
      await page.waitForTimeout(3000);
      
      await expect(page.locator('.account-details')).toBeVisible({ timeout: 10000 });
      await expect(page.locator('.account-balance')).toContainText('$1,500.00');
    });
  });

  test.describe('Performance and Load Testing Scenarios', () => {
    test('should handle rapid user interactions without race conditions', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      
      const rapidActions = Array.from({ length: 10 }, (_, i) => async () => {
        await page.fill('input[name="userId"]', `RAPID${String(i).padStart(3, '0')}`);
        await page.fill('input[name="firstName"]', `User${i}`);
        await page.fill('input[name="lastName"]', `Test${i}`);
        await page.selectOption('select[name="userType"]', i % 2 === 0 ? 'A' : 'U');
        await page.fill('input[name="password"]', `pass${i}123`);
        await page.fill('input[name="confirmPassword"]', `pass${i}123`);
      });
      
      await Promise.all(rapidActions.map(action => action()));
      
      const finalUserId = await page.inputValue('input[name="userId"]');
      const finalFirstName = await page.inputValue('input[name="firstName"]');
      
      expect(finalUserId).toMatch(/^RAPID\d{3}$/);
      expect(finalFirstName).toMatch(/^User\d$/);
      
      const navigationPromises = [
        adminMenuPage.navigateToAccountView(),
        page.waitForTimeout(100),
        adminMenuPage.navigateToReports(),
        page.waitForTimeout(100),
        adminMenuPage.navigateToUserManagement()
      ];
      
      await Promise.all(navigationPromises);
      await expect(page).toHaveURL('/admin/users');
    });

    test('should maintain performance with complex DOM manipulations', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.route('**/api/transactions/**', async (route) => {
        const complexData = Array.from({ length: 500 }, (_, i) => ({
          id: `TXN${i}`,
          amount: Math.random() * 1000,
          merchant: `Complex Merchant Name ${i} with Very Long Description`,
          date: new Date().toISOString(),
          category: 'dining',
          status: 'completed',
          details: {
            authCode: `AUTH${i}`,
            processingTime: Math.random() * 1000,
            fees: Math.random() * 10,
            rewards: Math.random() * 5
          }
        }));
        
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({ transactions: complexData })
        });
      });

      const startTime = Date.now();
      
      await mainMenuPage.navigateToTransactions();
      await expect(page.locator('h2')).toContainText('Transaction List');
      
      const loadTime = Date.now() - startTime;
      expect(loadTime).toBeLessThan(5000); // Should load within 5 seconds
      
      const scrollStartTime = Date.now();
      
      for (let i = 0; i < 10; i++) {
        await page.evaluate(() => window.scrollBy(0, 500));
        await page.waitForTimeout(100);
      }
      
      const scrollTime = Date.now() - scrollStartTime;
      expect(scrollTime).toBeLessThan(3000); // Scrolling should be smooth
      
      const filterStartTime = Date.now();
      
      await page.selectOption('select[name="category"]', 'dining');
      await page.fill('input[name="minAmount"]', '100');
      await page.click('button:has-text("Apply Filters")');
      
      await expect(page.locator('.filtered-results')).toBeVisible();
      
      const filterTime = Date.now() - filterStartTime;
      expect(filterTime).toBeLessThan(2000); // Filtering should be fast
    });
  });
});
