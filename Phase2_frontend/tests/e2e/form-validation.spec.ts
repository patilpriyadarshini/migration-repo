import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Form Validation Scenarios', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('Login Form Validation', () => {
    test('should validate required fields on login', async ({ page }) => {
      await loginPage.goto();
      await loginPage.clickSignIn();
      
      await expect(page.locator('.text-red-600')).toHaveCount(2);
      await expect(page.locator('input[name="userId"] + .text-red-600')).toContainText('User ID is required');
      await expect(page.locator('input[name="password"] + .text-red-600')).toContainText('Password is required');
    });

    test('should validate user ID format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.fillUserId('abc');
      await loginPage.clickSignIn();
      
      await expect(page.locator('input[name="userId"] + .text-red-600')).toContainText('User ID must be 8 characters');
    });

    test('should validate password minimum length', async ({ page }) => {
      await loginPage.goto();
      await loginPage.fillUserId('ADMIN001');
      await loginPage.fillPassword('123');
      await loginPage.clickSignIn();
      
      await expect(page.locator('input[name="password"] + .text-red-600')).toContainText('Password must be at least 6 characters');
    });

    test('should show validation errors in real-time', async ({ page }) => {
      await loginPage.goto();
      
      await loginPage.fillUserId('ab');
      await page.locator('input[name="password"]').focus();
      await expect(page.locator('input[name="userId"] + .text-red-600')).toContainText('User ID must be 8 characters');
      
      await loginPage.fillPassword('12');
      await page.locator('input[name="userId"]').focus();
      await expect(page.locator('input[name="password"] + .text-red-600')).toContainText('Password must be at least 6 characters');
    });

    test('should clear validation errors when fields are corrected', async ({ page }) => {
      await loginPage.goto();
      await loginPage.clickSignIn();
      
      await expect(page.locator('.text-red-600')).toHaveCount(2);
      
      await loginPage.fillUserId('ADMIN001');
      await loginPage.fillPassword('admin123');
      
      await expect(page.locator('.text-red-600')).toHaveCount(0);
    });
  });

  test.describe('Account Form Validation', () => {
    test('should validate account number format in account view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToAccountView();
      
      await page.fill('input[name="accountNumber"]', '123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toContainText('Account number must be 11 digits');
    });

    test('should validate required fields in account update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(4);
      await expect(page.locator('input[name="accountNumber"] + .validation-error')).toContainText('Account number is required');
      await expect(page.locator('input[name="customerName"] + .validation-error')).toContainText('Customer name is required');
      await expect(page.locator('input[name="customerAddress"] + .validation-error')).toContainText('Customer address is required');
      await expect(page.locator('input[name="customerPhone"] + .validation-error')).toContainText('Phone number is required');
    });

    test('should validate phone number format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      await page.fill('input[name="customerPhone"]', '123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="customerPhone"] + .validation-error')).toContainText('Invalid phone number format');
    });

    test('should validate name length limits', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/update');
      
      const longName = 'A'.repeat(101);
      await page.fill('input[name="customerName"]', longName);
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="customerName"] + .validation-error')).toContainText('Name cannot exceed 100 characters');
    });
  });

  test.describe('Transaction Form Validation', () => {
    test('should validate required fields in add transaction', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(5);
      await expect(page.locator('input[name="cardNumber"] + .validation-error')).toContainText('Card number is required');
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount is required');
      await expect(page.locator('select[name="transactionType"] + .validation-error')).toContainText('Transaction type is required');
      await expect(page.locator('input[name="merchantName"] + .validation-error')).toContainText('Merchant name is required');
      await expect(page.locator('input[name="merchantCategory"] + .validation-error')).toContainText('Merchant category is required');
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

    test('should validate maximum transaction amount', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="amount"]', '100000.00');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount cannot exceed $50,000');
    });

    test('should validate decimal places in amount', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="amount"]', '123.456');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="amount"] + .validation-error')).toContainText('Amount can have maximum 2 decimal places');
    });
  });

  test.describe('Bill Payment Form Validation', () => {
    test('should validate required fields in bill payment', async ({ page }) => {
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

    test('should validate payment date is not in the past', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="paymentDate"]', '2020-01-01');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="paymentDate"] + .validation-error')).toContainText('Payment date cannot be in the past');
    });

    test('should validate payment date is not too far in future', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      const futureDate = new Date();
      futureDate.setFullYear(futureDate.getFullYear() + 2);
      await page.fill('input[name="paymentDate"]', futureDate.toISOString().split('T')[0]);
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="paymentDate"] + .validation-error')).toContainText('Payment date cannot be more than 1 year in the future');
    });

    test('should validate payee ID format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToBillPayment();
      
      await page.fill('input[name="payeeId"]', 'abc');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="payeeId"] + .validation-error')).toContainText('Invalid payee ID format');
    });
  });

  test.describe('Admin User Form Validation', () => {
    test('should validate required fields in add user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(6);
      await expect(page.locator('input[name="userId"] + .validation-error')).toContainText('User ID is required');
      await expect(page.locator('input[name="firstName"] + .validation-error')).toContainText('First name is required');
      await expect(page.locator('input[name="lastName"] + .validation-error')).toContainText('Last name is required');
      await expect(page.locator('select[name="userType"] + .validation-error')).toContainText('User type is required');
      await expect(page.locator('input[name="password"] + .validation-error')).toContainText('Password is required');
      await expect(page.locator('input[name="confirmPassword"] + .validation-error')).toContainText('Password confirmation is required');
    });

    test('should validate user ID format in add user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="userId"]', 'abc');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="userId"] + .validation-error')).toContainText('User ID must be 8 characters');
    });

    test('should validate password confirmation match', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'different123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="confirmPassword"] + .validation-error')).toContainText('Passwords do not match');
    });

    test('should validate password strength', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="password"]', '123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="password"] + .validation-error')).toContainText('Password must be at least 6 characters');
    });

    test('should validate user type selection', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="userId"]', 'NEWUSER1');
      await page.fill('input[name="firstName"]', 'John');
      await page.fill('input[name="lastName"]', 'Doe');
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'password123');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('select[name="userType"] + .validation-error')).toContainText('User type is required');
    });
  });

  test.describe('Card Form Validation', () => {
    test('should validate card limit in card update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '-1000');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="cardLimit"] + .validation-error')).toContainText('Card limit must be positive');
    });

    test('should validate maximum card limit', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '1000000');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="cardLimit"] + .validation-error')).toContainText('Card limit cannot exceed $100,000');
    });

    test('should validate card status selection', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.selectOption('select[name="cardStatus"]', '');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('select[name="cardStatus"] + .validation-error')).toContainText('Card status is required');
    });
  });

  test.describe('Report Form Validation', () => {
    test('should validate date range in custom reports', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-03-31');
      await page.fill('input[name="endDate"]', '2024-01-01');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toContainText('End date must be after start date');
    });

    test('should validate maximum date range', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2020-01-01');
      await page.fill('input[name="endDate"]', '2024-12-31');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toContainText('Date range cannot exceed 2 years');
    });

    test('should validate required date fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(2);
      await expect(page.locator('input[name="startDate"] + .validation-error')).toContainText('Start date is required');
      await expect(page.locator('input[name="endDate"] + .validation-error')).toContainText('End date is required');
    });
  });

  test.describe('Cross-Field Validation', () => {
    test('should validate account ownership in transactions', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/add');
      
      await page.fill('input[name="cardNumber"]', '9999999999999999');
      await page.fill('input[name="amount"]', '100.00');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.fill('input[name="merchantName"]', 'Test Merchant');
      await page.fill('input[name="merchantCategory"]', 'RETAIL');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toContainText('Card does not belong to current user');
    });

    test('should validate sufficient balance for transactions', async ({ page }) => {
      await page.route('**/api/cards/validate', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Insufficient available credit' }) })
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
      
      await expect(page.locator('.validation-error')).toContainText('Insufficient available credit');
    });

    test('should validate account access permissions', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.fill('input[name="accountNumber"]', '99999999999');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toContainText('Access denied to this account');
    });
  });
});
