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

  test.describe('Multi-User Concurrent Session Testing', () => {
    test('should handle multiple admin users performing concurrent operations', async ({ browser }) => {
      const context1 = await browser.newContext();
      const context2 = await browser.newContext();
      const page1 = await context1.newPage();
      const page2 = await context2.newPage();

      const loginPage1 = new LoginPage(page1);
      const loginPage2 = new LoginPage(page2);
      const adminMenuPage1 = new AdminMenuPage(page1);
      const adminMenuPage2 = new AdminMenuPage(page2);

      await Promise.all([
        loginPage1.goto(),
        loginPage2.goto()
      ]);

      await Promise.all([
        loginPage1.login('ADMIN001', 'admin123'),
        loginPage2.login('ADMIN001', 'admin123')
      ]);

      await Promise.all([
        adminMenuPage1.navigateToUserManagement(),
        adminMenuPage2.navigateToUserManagement()
      ]);

      const createUserPromise = (async () => {
        await page1.click('button:has-text("Add User")');
        await page1.fill('input[name="userId"]', 'CONC0001');
        await page1.fill('input[name="firstName"]', 'Concurrent');
        await page1.fill('input[name="lastName"]', 'User1');
        await page1.check('input[name="userType"][value="U"]');
        await page1.fill('input[name="password"]', 'conc123');
        await page1.click('button[type="submit"]');
        return page1.locator('.bg-green-50').textContent();
      })();

      const viewUsersPromise = (async () => {
        await page2.click('button:has-text("View Users")');
        await page2.waitForSelector('table');
        return page2.locator('table tbody tr').count();
      })();

      const [createResult, userCount] = await Promise.all([createUserPromise, viewUsersPromise]);

      expect(createResult).toContain('User created successfully');
      expect(userCount).toBeGreaterThan(0);

      await context1.close();
      await context2.close();
    });

    test('should handle session conflicts and race conditions gracefully', async ({ browser }) => {
      const contexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);

      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));

      await Promise.all(loginPages.map(loginPage => loginPage.goto()));
      
      const loginPromises = loginPages.map((loginPage, index) => 
        loginPage.login('USER0001', 'user1234')
      );

      await Promise.all(loginPromises);

      for (const page of pages) {
        await expect(page).toHaveURL('/menu');
        await expect(page.locator('h2')).toContainText('Select an Option');
      }

      const navigationPromises = pages.map((page, index) => {
        const mainMenuPage = new MainMenuPage(page);
        switch (index) {
          case 0: return mainMenuPage.navigateToAccountView();
          case 1: return mainMenuPage.navigateToTransactions();
          case 2: return mainMenuPage.navigateToCards();
        }
      });

      await Promise.all(navigationPromises);

      await expect(pages[0]).toHaveURL('/account-view');
      await expect(pages[1]).toHaveURL('/transactions');
      await expect(pages[2]).toHaveURL('/cards');

      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });

  test.describe('Advanced State Management and Data Persistence', () => {
    test('should maintain complex form state across navigation and errors', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');

      const formData = {
        userId: 'STATE001',
        firstName: 'Complex',
        lastName: 'StateTest',
        userType: 'A',
        password: 'Complex123!',
        confirmPassword: 'Complex123!',
        email: 'complex@test.com',
        department: 'IT',
        role: 'Manager'
      };

      for (const [field, value] of Object.entries(formData)) {
        if (field === 'userType') {
          await page.check(`input[name="${field}"][value="${value}"]`);
        } else {
          await page.fill(`input[name="${field}"]`, value);
        }
      }

      await page.route('**/api/admin/users', async (route) => {
        await route.abort('failed');
      });

      await page.click('button[type="submit"]');
      await expect(page.locator('.error-message')).toBeVisible();

      for (const [field, value] of Object.entries(formData)) {
        if (field === 'userType') {
          await expect(page.locator(`input[name="${field}"][value="${value}"]`)).toBeChecked();
        } else if (field !== 'password' && field !== 'confirmPassword') {
          await expect(page.locator(`input[name="${field}"]`)).toHaveValue(value);
        }
      }

      await adminMenuPage.navigateToReports();
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');

      await expect(page.locator('input[name="userId"]')).toHaveValue('');
      await expect(page.locator('input[name="firstName"]')).toHaveValue('');
    });

    test('should handle complex data filtering and sorting with state persistence', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');

      await page.route('**/api/transactions**', async (route) => {
        const url = new URL(route.request().url());
        const sortBy = url.searchParams.get('sortBy') || 'date';
        const sortOrder = url.searchParams.get('sortOrder') || 'desc';
        const category = url.searchParams.get('category');
        const minAmount = url.searchParams.get('minAmount');
        const maxAmount = url.searchParams.get('maxAmount');

        let transactions = Array.from({ length: 200 }, (_, i) => ({
          id: `TXN${String(i + 1).padStart(6, '0')}`,
          amount: Math.random() * 2000 + 10,
          merchant: `Merchant ${i + 1}`,
          date: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
          category: ['dining', 'shopping', 'gas', 'groceries', 'entertainment'][Math.floor(Math.random() * 5)],
          status: ['completed', 'pending', 'failed'][Math.floor(Math.random() * 3)]
        }));

        if (category) {
          transactions = transactions.filter(t => t.category === category);
        }
        if (minAmount) {
          transactions = transactions.filter(t => t.amount >= parseFloat(minAmount));
        }
        if (maxAmount) {
          transactions = transactions.filter(t => t.amount <= parseFloat(maxAmount));
        }

        transactions.sort((a, b) => {
          let aVal = a[sortBy];
          let bVal = b[sortBy];
          
          if (sortBy === 'amount') {
            aVal = parseFloat(aVal);
            bVal = parseFloat(bVal);
          } else if (sortBy === 'date') {
            aVal = new Date(aVal);
            bVal = new Date(bVal);
          }

          if (sortOrder === 'asc') {
            return aVal > bVal ? 1 : -1;
          } else {
            return aVal < bVal ? 1 : -1;
          }
        });

        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            content: transactions.slice(0, 50),
            totalElements: transactions.length,
            page: 0,
            size: 50,
            totalPages: Math.ceil(transactions.length / 50)
          })
        });
      });

      await mainMenuPage.navigateToTransactions();

      await page.selectOption('select[name="category"]', 'dining');
      await page.fill('input[name="minAmount"]', '50');
      await page.fill('input[name="maxAmount"]', '500');
      await page.click('button:has-text("Apply Filters")');

      await page.click('th:has-text("Amount")');
      await page.waitForTimeout(500);

      await mainMenuPage.navigateToAccountView();
      await mainMenuPage.navigateToTransactions();

      await expect(page.locator('select[name="category"]')).toHaveValue('dining');
      await expect(page.locator('input[name="minAmount"]')).toHaveValue('50');
      await expect(page.locator('input[name="maxAmount"]')).toHaveValue('500');
    });
  });

  test.describe('Advanced Error Recovery and Resilience Testing', () => {
    test('should recover from cascading API failures with intelligent retry', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');

      let authAttempts = 0;
      let accountAttempts = 0;
      let transactionAttempts = 0;

      await page.route('**/api/auth/**', async (route) => {
        authAttempts++;
        if (authAttempts <= 2) {
          await route.fulfill({
            status: 503,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Service temporarily unavailable' })
          });
        } else {
          await route.continue();
        }
      });

      await page.route('**/api/accounts/**', async (route) => {
        accountAttempts++;
        if (accountAttempts === 1) {
          await route.abort('timedout');
        } else if (accountAttempts === 2) {
          await route.fulfill({
            status: 429,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Rate limit exceeded', retryAfter: 1 })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              acctId: 12345678901,
              acctActiveStatus: 'Y',
              acctCurrBal: 1500.00,
              customerFirstName: 'Alice',
              customerLastName: 'Johnson'
            })
          });
        }
      });

      await page.route('**/api/transactions/**', async (route) => {
        transactionAttempts++;
        if (transactionAttempts <= 3) {
          await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Database connection failed' })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: [{
                tranId: 'T001',
                cardNum: '4111111111111111',
                tranDesc: 'Test Transaction',
                tranAmt: 100.00
              }],
              totalElements: 1
            })
          });
        }
      });

      await mainMenuPage.navigateToAccountView();
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');

      await expect(page.locator('.error-message')).toContainText('timed out', { timeout: 10000 });
      
      await page.click('button:has-text("Retry")');
      await expect(page.locator('.error-message')).toContainText('Rate limit');

      await page.waitForTimeout(1500); // Wait for rate limit
      await page.click('button:has-text("Retry")');
      await expect(page.locator('.account-details')).toBeVisible({ timeout: 10000 });

      await mainMenuPage.navigateToTransactions();
      await expect(page.locator('.error-message')).toContainText('Database connection failed');

      for (let i = 0; i < 3; i++) {
        await page.click('button:has-text("Retry")');
        await page.waitForTimeout(500);
      }

      await expect(page.locator('.transaction-list')).toBeVisible({ timeout: 10000 });
    });

    test('should handle complex validation chains with dependent field validation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      await page.click('button:has-text("Add User")');

      await page.fill('input[name="userId"]', 'ADMIN999');
      await page.check('input[name="userType"][value="A"]');
      
      await page.fill('input[name="password"]', 'weak');
      await page.locator('input[name="password"]').blur();
      await expect(page.locator('.text-red-600')).toContainText('Admin password must contain uppercase, lowercase, and numbers');

      await page.fill('input[name="password"]', 'StrongAdmin123');
      await page.locator('input[name="password"]').blur();
      await expect(page.locator('.text-red-600')).not.toBeVisible();

      await page.check('input[name="userType"][value="U"]');
      await page.fill('input[name="password"]', 'simple123');
      await page.locator('input[name="password"]').blur();
      await expect(page.locator('.text-red-600')).not.toBeVisible();

      await page.fill('input[name="email"]', 'invalid-email');
      await page.locator('input[name="email"]').blur();
      await expect(page.locator('.text-red-600')).toContainText('Invalid email format');

      await page.fill('input[name="email"]', 'user@external.com');
      await page.locator('input[name="email"]').blur();
      await expect(page.locator('.text-red-600')).toContainText('Only company domain emails allowed');

      await page.fill('input[name="email"]', 'user@company.com');
      await page.locator('input[name="email"]').blur();
      await expect(page.locator('.text-red-600')).not.toBeVisible();

      await page.check('input[name="requiresApproval"]');
      await expect(page.locator('input[name="approverUserId"]')).toBeVisible();
      await expect(page.locator('input[name="approverUserId"]')).toHaveAttribute('required');

      await page.uncheck('input[name="requiresApproval"]');
      await expect(page.locator('input[name="approverUserId"]')).not.toBeVisible();
    });
  });

  test.describe('Advanced Multi-Step Integration Workflows', () => {
    test('should handle complex end-to-end customer onboarding workflow', async ({ page, browser }) => {
      const adminContext = await browser.newContext();
      const adminPage = await adminContext.newPage();
      
      const loginPage = new LoginPage(adminPage);
      const adminMenuPage = new AdminMenuPage(adminPage);

      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenuPage.navigateToUserManagement();
      
      const newUserId = `USER${Date.now().toString().slice(-4)}`;
      await adminPage.fill('input[name="userId"]', newUserId);
      await adminPage.fill('input[name="firstName"]', 'Test');
      await adminPage.fill('input[name="lastName"]', 'Customer');
      await adminPage.selectOption('select[name="userType"]', 'U');
      await adminPage.fill('input[name="password"]', 'newpass123');
      await adminPage.click('button:has-text("Add User")');
      
      await expect(adminPage.locator('.success-message')).toBeVisible();
      
      const customerContext = await browser.newContext();
      const customerPage = await customerContext.newPage();
      const customerLoginPage = new LoginPage(customerPage);
      const customerMenuPage = new MainMenuPage(customerPage);
      
      await customerLoginPage.goto();
      await customerLoginPage.login(newUserId, 'newpass123');
      
      await customerMenuPage.verifyMainMenuVisible();
      
      await customerMenuPage.navigateToAccountView();
      
      const accountForm = customerPage.locator('form');
      if (await accountForm.isVisible()) {
        await customerPage.fill('input[name="phoneNumber"]', '555-0123');
        await customerPage.fill('input[name="email"]', 'test@example.com');
        await customerPage.click('button:has-text("Update")');
      }
      
      await customerMenuPage.navigateToTransactions();
      await expect(customerPage.locator('body')).toBeVisible();
      
      await adminMenuPage.navigateToUserManagement();
      await adminPage.fill('input[name="searchUserId"]', newUserId);
      await adminPage.click('button:has-text("Search")');
      
      const deleteButton = adminPage.locator(`button:has-text("Delete"):near(:text("${newUserId}"))`);
      if (await deleteButton.isVisible()) {
        await deleteButton.click();
        await adminPage.click('button:has-text("Confirm")');
      }
      
      await adminContext.close();
      await customerContext.close();
    });

    test('should handle complex transaction approval workflow', async ({ page, browser }) => {
      const contexts = await Promise.all([
        browser.newContext(), // Customer
        browser.newContext(), // Admin/Approver
        browser.newContext()  // Auditor
      ]);
      
      const [customerPage, adminPage, auditorPage] = await Promise.all(
        contexts.map(ctx => ctx.newPage())
      );
      
      const customerLogin = new LoginPage(customerPage);
      const customerMenu = new MainMenuPage(customerPage);
      
      await customerLogin.goto();
      await customerLogin.login('USER0001', 'user1234');
      
      await customerMenu.navigateToTransactions();
      
      await customerPage.route('**/api/transactions**', async (route) => {
        if (route.request().method() === 'POST') {
          await route.fulfill({
            status: 202,
            contentType: 'application/json',
            body: JSON.stringify({
              id: 'TXN12345678',
              status: 'PENDING_APPROVAL',
              amount: 10000,
              requiresApproval: true
            })
          });
        } else {
          await route.continue();
        }
      });
      
      const transactionForm = customerPage.locator('form');
      if (await transactionForm.isVisible()) {
        await customerPage.fill('input[name="amount"]', '10000');
        await customerPage.fill('input[name="description"]', 'Large purchase requiring approval');
        await customerPage.click('button:has-text("Submit")');
        
        await expect(customerPage.locator('text=pending approval')).toBeVisible();
      }
      
      const adminLogin = new LoginPage(adminPage);
      const adminMenu = new AdminMenuPage(adminPage);
      
      await adminLogin.goto();
      await adminLogin.login('ADMIN001', 'admin123');
      
      await adminMenu.navigateToUserManagement();
      
      const approvalButton = adminPage.locator('button:has-text("Approve")');
      if (await approvalButton.isVisible()) {
        await approvalButton.click();
        await adminPage.fill('textarea[name="approvalNotes"]', 'Approved after verification');
        await adminPage.click('button:has-text("Confirm Approval")');
      }
      
      await customerMenu.navigateToTransactions();
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });

  test.describe('Advanced Error Recovery and Resilience Testing', () => {
    test('should recover from cascading system failures with intelligent retry', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      let authAttempts = 0;
      let transactionAttempts = 0;
      
      await testPage.route('**/api/auth/login**', async (route) => {
        authAttempts++;
        if (authAttempts <= 2) {
          await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Internal server error' })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              token: 'mock-jwt-token',
              user: { userId: 'USER0001', userType: 'U' }
            })
          });
        }
      });
      
      await testPage.route('**/api/transactions**', async (route) => {
        transactionAttempts++;
        if (transactionAttempts <= 1) {
          await route.fulfill({
            status: 503,
            contentType: 'application/json',
            body: JSON.stringify({ error: 'Service temporarily unavailable' })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: [
                { id: 'TXN001', amount: 100, merchant: 'Test Store' }
              ]
            })
          });
        }
      });
      
      await loginPage.goto();
      
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.waitForTimeout(1000);
      
      await loginPage.login('USER0001', 'user1234');
      await testPage.waitForTimeout(1000);
      
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.verifyMainMenuVisible();
      
      await mainMenuPage.navigateToTransactions();
      await testPage.waitForTimeout(1000);
      
      await testPage.reload();
      await expect(testPage.locator('body')).toBeVisible();
      
      await context.close();
    });

    test('should handle advanced validation chains with dependent field validation', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenuPage.navigateToAccountView();
      
      const validationScenarios = [
        {
          field: 'accountNumber',
          value: '123', // Too short
          expectedError: 'Account number must be at least 8 characters'
        },
        {
          field: 'accountNumber', 
          value: '12345678901234567890', // Too long
          expectedError: 'Account number must not exceed 15 characters'
        },
        {
          field: 'accountNumber',
          value: 'ABC12345678', // Invalid format
          expectedError: 'Account number must contain only digits'
        },
        {
          field: 'phoneNumber',
          value: '123', // Invalid phone
          expectedError: 'Please enter a valid phone number'
        }
      ];
      
      for (const scenario of validationScenarios) {
        const fieldInput = testPage.locator(`input[name="${scenario.field}"]`);
        
        if (await fieldInput.isVisible()) {
          await fieldInput.clear();
          await fieldInput.fill(scenario.value);
          await fieldInput.blur(); // Trigger validation
          
          const errorMessage = testPage.locator(`.error-message:near(input[name="${scenario.field}"])`);
          if (await errorMessage.isVisible()) {
            const errorText = await errorMessage.textContent();
            expect(errorText?.toLowerCase()).toContain('invalid');
          }
        }
      }
      
      const accountInput = testPage.locator('input[name="accountNumber"]');
      if (await accountInput.isVisible()) {
        await accountInput.clear();
        await accountInput.fill('12345678901');
        await accountInput.blur();
        
        const errorMessage = testPage.locator('.error-message:near(input[name="accountNumber"])');
        await expect(errorMessage).not.toBeVisible();
      }
      
      await context.close();
    });
  });

  test.describe('Advanced Performance and Scalability Testing', () => {
    test('should handle high-frequency user interactions without performance degradation', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      const baselineStart = Date.now();
      await mainMenuPage.navigateToAccountView();
      const baselineTime = Date.now() - baselineStart;
      
      const navigationCycles = 10;
      const navigationTimes: number[] = [];
      
      for (let i = 0; i < navigationCycles; i++) {
        const cycleStart = Date.now();
        
        await mainMenuPage.navigateToTransactions();
        await mainMenuPage.navigateToCards();
        await mainMenuPage.navigateToAccountView();
        
        const cycleTime = Date.now() - cycleStart;
        navigationTimes.push(cycleTime);
        
        await testPage.waitForTimeout(100);
      }
      
      const averageTime = navigationTimes.reduce((a, b) => a + b, 0) / navigationTimes.length;
      const maxTime = Math.max(...navigationTimes);
      
      expect(averageTime).toBeLessThan(baselineTime * 3); // Should not be 3x slower
      expect(maxTime).toBeLessThan(baselineTime * 5); // No single cycle should be 5x slower
      
      await mainMenuPage.navigateToAccountView();
      
      const rapidFormStart = Date.now();
      
      for (let i = 0; i < 20; i++) {
        const accountInput = testPage.locator('input[name="accountNumber"]');
        if (await accountInput.isVisible()) {
          await accountInput.fill(`1234567890${i}`);
          await accountInput.clear();
        }
      }
      
      const rapidFormTime = Date.now() - rapidFormStart;
      expect(rapidFormTime).toBeLessThan(5000); // Should complete within 5 seconds
      
      await context.close();
    });

    test('should maintain responsiveness under memory pressure', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await testPage.route('**/api/**', async (route) => {
        const url = route.request().url();
        
        if (url.includes('transactions')) {
          const largeDataset = Array.from({ length: 15000 }, (_, i) => ({
            id: `TXN${String(i + 1).padStart(8, '0')}`,
            amount: Math.random() * 10000,
            merchant: `Merchant ${i + 1} - ${Array(50).fill('data').join(' ')}`, // Large text
            date: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
            category: 'dining',
            status: 'completed',
            metadata: {
              location: `Location ${i}`,
              details: Array(100).fill(`Detail ${i}`).join(' '), // More large text
              tags: Array(20).fill(`tag${i}`),
              history: Array(10).fill({ timestamp: Date.now(), action: `action${i}` })
            }
          }));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: largeDataset,
              totalElements: largeDataset.length
            })
          });
        } else {
          await route.continue();
        }
      });
      
      const memoryTestStart = Date.now();
      
      for (let i = 0; i < 3; i++) {
        await mainMenuPage.navigateToTransactions();
        await testPage.waitForTimeout(1000); // Allow data to load
        
        for (let j = 0; j < 10; j++) {
          await testPage.evaluate(() => window.scrollBy(0, 500));
          await testPage.waitForTimeout(100);
        }
        
        await testPage.evaluate(() => window.scrollTo(0, 0));
      }
      
      const memoryTestTime = Date.now() - memoryTestStart;
      
      const responsiveTestStart = Date.now();
      await mainMenuPage.navigateToAccountView();
      const responsiveTestTime = Date.now() - responsiveTestStart;
      
      expect(memoryTestTime).toBeLessThan(30000); // Should complete within 30 seconds
      expect(responsiveTestTime).toBeLessThan(5000); // Navigation should remain fast
      
      await context.close();
    });
  });
});
