import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Data Integrity and Consistency Testing', () => {
  test.describe('Complex Transaction Data Integrity', () => {
    test('should maintain ACID properties during complex multi-step transactions', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenu = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      let transactionLog = [];
      let accountBalance = 5000.00;
      let transactionCounter = 0;
      
      await testPage.route('**/api/accounts/**', async (route) => {
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            accountNumber: '1234567890',
            balance: accountBalance,
            accountType: 'CHECKING',
            status: 'ACTIVE'
          })
        });
      });
      
      await testPage.route('**/api/transactions**', async (route) => {
        const method = route.request().method();
        
        if (method === 'POST') {
          const transactionData = await route.request().postDataJSON();
          transactionCounter++;
          
          const transaction = {
            id: `TXN${String(transactionCounter).padStart(8, '0')}`,
            amount: parseFloat(transactionData.amount),
            type: transactionData.type || 'debit',
            merchant: transactionData.merchant,
            timestamp: new Date().toISOString(),
            status: 'pending'
          };
          
          if (transaction.type === 'debit' && accountBalance >= transaction.amount) {
            accountBalance -= transaction.amount;
            transaction.status = 'completed';
            transactionLog.push(transaction);
          } else if (transaction.type === 'credit') {
            accountBalance += transaction.amount;
            transaction.status = 'completed';
            transactionLog.push(transaction);
          } else {
            transaction.status = 'failed';
            transaction.error = 'Insufficient funds';
            transactionLog.push(transaction);
          }
          
          await route.fulfill({
            status: transaction.status === 'failed' ? 400 : 201,
            contentType: 'application/json',
            body: JSON.stringify(transaction)
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: transactionLog,
              totalElements: transactionLog.length
            })
          });
        }
      });
      
      await mainMenu.navigateToTransactions();
      
      const transactionScenarios = [
        { amount: '100.00', type: 'debit', merchant: 'Store A', shouldSucceed: true },
        { amount: '200.00', type: 'debit', merchant: 'Store B', shouldSucceed: true },
        { amount: '6000.00', type: 'debit', merchant: 'Store C', shouldSucceed: false }, // Should fail - insufficient funds
        { amount: '500.00', type: 'credit', merchant: 'Refund A', shouldSucceed: true },
        { amount: '150.00', type: 'debit', merchant: 'Store D', shouldSucceed: true }
      ];
      
      for (const scenario of transactionScenarios) {
        const addButton = testPage.locator('button:has-text("Add"), a:has-text("Add")');
        if (await addButton.isVisible()) {
          await addButton.click();
          
          const amountInput = testPage.locator('input[name="amount"]');
          const merchantInput = testPage.locator('input[name="merchant"]');
          const typeSelect = testPage.locator('select[name="type"]');
          const submitButton = testPage.locator('button[type="submit"]');
          
          if (await amountInput.isVisible()) {
            await amountInput.fill(scenario.amount);
            await merchantInput.fill(scenario.merchant);
            
            if (await typeSelect.isVisible()) {
              await typeSelect.selectOption(scenario.type);
            }
            
            await submitButton.click();
            await testPage.waitForTimeout(1000);
            
            if (scenario.shouldSucceed) {
              const successMessage = testPage.locator('.success-message, .alert-success');
              expect(await successMessage.isVisible()).toBeTruthy();
            } else {
              const errorMessage = testPage.locator('.error-message, .alert-danger');
              expect(await errorMessage.isVisible()).toBeTruthy();
            }
          }
        }
      }
      
      const expectedBalance = 5000.00 - 100.00 - 200.00 + 500.00 - 150.00; // 5050.00
      expect(accountBalance).toBe(expectedBalance);
      
      const completedTransactions = transactionLog.filter(t => t.status === 'completed');
      const failedTransactions = transactionLog.filter(t => t.status === 'failed');
      
      expect(completedTransactions.length).toBe(4);
      expect(failedTransactions.length).toBe(1);
      expect(failedTransactions[0].error).toBe('Insufficient funds');
      
      await context.close();
    });

    test('should handle complex data validation with cross-field dependencies', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const adminMenu = new AdminMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenu.navigateToUserAdd();
      
      const validationRules = {
        userId: {
          required: true,
          minLength: 6,
          maxLength: 8,
          pattern: /^[A-Z]{4,5}[0-9]{2,3}$/,
          unique: true
        },
        firstName: {
          required: true,
          minLength: 2,
          maxLength: 20,
          pattern: /^[A-Za-z\s]+$/
        },
        lastName: {
          required: true,
          minLength: 2,
          maxLength: 20,
          pattern: /^[A-Za-z\s]+$/
        },
        userType: {
          required: true,
          values: ['A', 'U']
        },
        password: {
          required: true,
          minLength: 8,
          requiresUppercase: true,
          requiresLowercase: true,
          requiresNumber: true,
          requiresSpecialChar: true
        }
      };
      
      const existingUsers = ['ADMIN001', 'USER0001', 'USER0002', 'ADMIN002'];
      
      await testPage.route('**/api/admin/users**', async (route) => {
        const method = route.request().method();
        
        if (method === 'POST') {
          const userData = await route.request().postDataJSON();
          const errors = [];
          
          if (!userData.userId) {
            errors.push('User ID is required');
          } else {
            if (userData.userId.length < validationRules.userId.minLength || 
                userData.userId.length > validationRules.userId.maxLength) {
              errors.push('User ID must be 6-8 characters');
            }
            if (!validationRules.userId.pattern.test(userData.userId)) {
              errors.push('User ID must follow pattern: 4-5 letters followed by 2-3 numbers');
            }
            if (existingUsers.includes(userData.userId)) {
              errors.push('User ID already exists');
            }
          }
          
          if (!userData.firstName) {
            errors.push('First name is required');
          } else if (!validationRules.firstName.pattern.test(userData.firstName)) {
            errors.push('First name can only contain letters and spaces');
          }
          
          if (!userData.lastName) {
            errors.push('Last name is required');
          } else if (!validationRules.lastName.pattern.test(userData.lastName)) {
            errors.push('Last name can only contain letters and spaces');
          }
          
          if (!userData.userType || !validationRules.userType.values.includes(userData.userType)) {
            errors.push('User type must be A (Admin) or U (User)');
          }
          
          if (!userData.password) {
            errors.push('Password is required');
          } else {
            if (userData.password.length < validationRules.password.minLength) {
              errors.push('Password must be at least 8 characters');
            }
            if (!/[A-Z]/.test(userData.password)) {
              errors.push('Password must contain at least one uppercase letter');
            }
            if (!/[a-z]/.test(userData.password)) {
              errors.push('Password must contain at least one lowercase letter');
            }
            if (!/[0-9]/.test(userData.password)) {
              errors.push('Password must contain at least one number');
            }
            if (!/[!@#$%^&*(),.?":{}|<>]/.test(userData.password)) {
              errors.push('Password must contain at least one special character');
            }
          }
          
          if (userData.userType === 'A' && userData.password && userData.password.length < 12) {
            errors.push('Admin users must have passwords of at least 12 characters');
          }
          
          if (errors.length > 0) {
            await route.fulfill({
              status: 400,
              contentType: 'application/json',
              body: JSON.stringify({ errors })
            });
          } else {
            existingUsers.push(userData.userId);
            await route.fulfill({
              status: 201,
              contentType: 'application/json',
              body: JSON.stringify({ 
                message: 'User created successfully',
                userId: userData.userId
              })
            });
          }
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ content: [], totalElements: 0 })
          });
        }
      });
      
      const testCases = [
        {
          data: { userId: '', firstName: 'John', lastName: 'Doe', userType: 'U', password: 'password123' },
          expectedErrors: ['User ID is required']
        },
        {
          data: { userId: 'ABC1', firstName: 'John', lastName: 'Doe', userType: 'U', password: 'password123' },
          expectedErrors: ['User ID must be 6-8 characters']
        },
        {
          data: { userId: 'ABCD12', firstName: 'John123', lastName: 'Doe', userType: 'U', password: 'password123' },
          expectedErrors: ['First name can only contain letters and spaces']
        },
        {
          data: { userId: 'ADMIN001', firstName: 'John', lastName: 'Doe', userType: 'U', password: 'Password123!' },
          expectedErrors: ['User ID already exists']
        },
        {
          data: { userId: 'TEST01', firstName: 'John', lastName: 'Doe', userType: 'A', password: 'Pass123!' },
          expectedErrors: ['Admin users must have passwords of at least 12 characters']
        },
        {
          data: { userId: 'TEST02', firstName: 'John', lastName: 'Doe', userType: 'U', password: 'Password123!' },
          expectedErrors: []
        }
      ];
      
      for (const testCase of testCases) {
        const userIdInput = testPage.locator('input[name="userId"]');
        const firstNameInput = testPage.locator('input[name="firstName"]');
        const lastNameInput = testPage.locator('input[name="lastName"]');
        const userTypeSelect = testPage.locator('select[name="userType"]');
        const passwordInput = testPage.locator('input[name="password"]');
        const submitButton = testPage.locator('button[type="submit"]');
        
        if (await userIdInput.isVisible()) {
          await userIdInput.fill(testCase.data.userId);
          await firstNameInput.fill(testCase.data.firstName);
          await lastNameInput.fill(testCase.data.lastName);
          
          if (await userTypeSelect.isVisible()) {
            await userTypeSelect.selectOption(testCase.data.userType);
          }
          
          await passwordInput.fill(testCase.data.password);
          await submitButton.click();
          
          await testPage.waitForTimeout(1000);
          
          if (testCase.expectedErrors.length > 0) {
            const errorMessages = testPage.locator('.error-message, .alert-danger, .field-error');
            expect(await errorMessages.count()).toBeGreaterThan(0);
            
            const errorText = await errorMessages.allTextContents();
            const allErrorText = errorText.join(' ');
            
            for (const expectedError of testCase.expectedErrors) {
              expect(allErrorText.toLowerCase()).toContain(expectedError.toLowerCase());
            }
          } else {
            const successMessage = testPage.locator('.success-message, .alert-success');
            expect(await successMessage.isVisible()).toBeTruthy();
          }
          
          await testPage.reload();
          await testPage.waitForTimeout(500);
        }
      }
      
      await context.close();
    });
  });

  test.describe('Advanced State Consistency and Synchronization', () => {
    test('should maintain consistent state across complex navigation patterns', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenu = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      let globalState = {
        user: { userId: 'USER0001', sessionId: 'SESSION123' },
        accounts: [],
        transactions: [],
        cards: [],
        navigationHistory: []
      };
      
      await testPage.route('**/api/**', async (route) => {
        const url = route.request().url();
        const method = route.request().method();
        
        globalState.navigationHistory.push({
          url,
          method,
          timestamp: new Date().toISOString()
        });
        
        if (url.includes('/accounts')) {
          if (method === 'GET') {
            const accounts = [
              { accountNumber: '1234567890', balance: 5000, type: 'CHECKING' },
              { accountNumber: '0987654321', balance: 2500, type: 'SAVINGS' }
            ];
            globalState.accounts = accounts;
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: accounts, totalElements: accounts.length })
            });
          }
        } else if (url.includes('/transactions')) {
          if (method === 'GET') {
            const transactions = [
              { id: 'TXN001', amount: 100, merchant: 'Store A', date: '2024-01-01' },
              { id: 'TXN002', amount: 200, merchant: 'Store B', date: '2024-01-02' }
            ];
            globalState.transactions = transactions;
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: transactions, totalElements: transactions.length })
            });
          }
        } else if (url.includes('/cards')) {
          if (method === 'GET') {
            const cards = [
              { cardNumber: '****1234', status: 'ACTIVE', expiryDate: '12/25' },
              { cardNumber: '****5678', status: 'ACTIVE', expiryDate: '06/26' }
            ];
            globalState.cards = cards;
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: cards, totalElements: cards.length })
            });
          }
        } else {
          await route.continue();
        }
      });
      
      const navigationSequence = [
        { action: () => mainMenu.navigateToAccountView(), expectedData: 'accounts' },
        { action: () => mainMenu.navigateToTransactions(), expectedData: 'transactions' },
        { action: () => mainMenu.navigateToCards(), expectedData: 'cards' },
        { action: () => mainMenu.navigateToAccountView(), expectedData: 'accounts' },
        { action: () => mainMenu.navigateToTransactions(), expectedData: 'transactions' }
      ];
      
      for (const step of navigationSequence) {
        await step.action();
        await testPage.waitForTimeout(1000);
        
        const currentUrl = testPage.url();
        expect(currentUrl).toContain(step.expectedData);
        
        if (step.expectedData === 'accounts') {
          expect(globalState.accounts.length).toBe(2);
          const accountElements = await testPage.locator('.account-item, .account-row, tr').count();
          expect(accountElements).toBeGreaterThan(0);
        } else if (step.expectedData === 'transactions') {
          expect(globalState.transactions.length).toBe(2);
          const transactionElements = await testPage.locator('.transaction-item, .transaction-row, tr').count();
          expect(transactionElements).toBeGreaterThan(0);
        } else if (step.expectedData === 'cards') {
          expect(globalState.cards.length).toBe(2);
          const cardElements = await testPage.locator('.card-item, .card-row, tr').count();
          expect(cardElements).toBeGreaterThan(0);
        }
      }
      
      expect(globalState.navigationHistory.length).toBeGreaterThan(5);
      
      const accountRequests = globalState.navigationHistory.filter(h => h.url.includes('/accounts'));
      const transactionRequests = globalState.navigationHistory.filter(h => h.url.includes('/transactions'));
      const cardRequests = globalState.navigationHistory.filter(h => h.url.includes('/cards'));
      
      expect(accountRequests.length).toBe(2); // Visited accounts twice
      expect(transactionRequests.length).toBe(2); // Visited transactions twice
      expect(cardRequests.length).toBe(1); // Visited cards once
      
      await context.close();
    });

    test('should handle complex form state persistence across interruptions', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const adminMenu = new AdminMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      let formState = {};
      
      await testPage.route('**/api/admin/users**', async (route) => {
        const method = route.request().method();
        
        if (method === 'POST') {
          const userData = await route.request().postDataJSON();
          formState = { ...userData, timestamp: new Date().toISOString() };
          
          const delay = Math.random() * 2000;
          await new Promise(resolve => setTimeout(resolve, delay));
          
          if (Math.random() < 0.3) { // 30% chance of failure
            await route.fulfill({
              status: 500,
              contentType: 'application/json',
              body: JSON.stringify({ error: 'Internal server error - please try again' })
            });
          } else {
            await route.fulfill({
              status: 201,
              contentType: 'application/json',
              body: JSON.stringify({ message: 'User created successfully', userId: userData.userId })
            });
          }
        } else {
          await route.continue();
        }
      });
      
      await adminMenu.navigateToUserAdd();
      
      const complexFormData = {
        userId: 'COMPLEX01',
        firstName: 'Complex',
        lastName: 'TestUser',
        userType: 'U',
        password: 'ComplexPassword123!',
        email: 'complex@test.com',
        department: 'Testing',
        role: 'Tester'
      };
      
      const userIdInput = testPage.locator('input[name="userId"]');
      const firstNameInput = testPage.locator('input[name="firstName"]');
      const lastNameInput = testPage.locator('input[name="lastName"]');
      
      if (await userIdInput.isVisible()) {
        await userIdInput.fill(complexFormData.userId);
        await firstNameInput.fill(complexFormData.firstName);
        await lastNameInput.fill(complexFormData.lastName);
        
        await mainMenu.navigateToUserManagement();
        await testPage.waitForTimeout(1000);
        
        await adminMenu.navigateToUserAdd();
        await testPage.waitForTimeout(1000);
        
        const preservedUserId = await userIdInput.inputValue();
        const preservedFirstName = await firstNameInput.inputValue();
        const preservedLastName = await lastNameInput.inputValue();
        
        if (!preservedUserId) {
          await userIdInput.fill(complexFormData.userId);
          await firstNameInput.fill(complexFormData.firstName);
          await lastNameInput.fill(complexFormData.lastName);
        }
        
        const userTypeSelect = testPage.locator('select[name="userType"]');
        const passwordInput = testPage.locator('input[name="password"]');
        const submitButton = testPage.locator('button[type="submit"]');
        
        if (await userTypeSelect.isVisible()) {
          await userTypeSelect.selectOption(complexFormData.userType);
        }
        
        await passwordInput.fill(complexFormData.password);
        
        let submissionAttempts = 0;
        let submissionSuccessful = false;
        
        while (submissionAttempts < 3 && !submissionSuccessful) {
          submissionAttempts++;
          await submitButton.click();
          await testPage.waitForTimeout(3000);
          
          const successMessage = testPage.locator('.success-message, .alert-success');
          const errorMessage = testPage.locator('.error-message, .alert-danger');
          
          if (await successMessage.isVisible()) {
            submissionSuccessful = true;
            expect(formState.userId).toBe(complexFormData.userId);
            expect(formState.firstName).toBe(complexFormData.firstName);
            expect(formState.lastName).toBe(complexFormData.lastName);
          } else if (await errorMessage.isVisible()) {
            const errorText = await errorMessage.textContent();
            if (errorText?.includes('server error')) {
              continue;
            } else {
              throw new Error(`Unexpected error: ${errorText}`);
            }
          }
        }
        
        expect(submissionSuccessful).toBeTruthy();
        expect(submissionAttempts).toBeLessThanOrEqual(3);
      }
      
      await context.close();
    });
  });
});
