import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';

test.describe('Card Management Workflows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
  });

  test.describe('Card List Operations', () => {
    test('should display card list for regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await expect(page).toHaveURL('/cards');
      await expect(page.locator('h2')).toContainText('Card List');
      const cardItems = page.locator('.card-item');
      await expect(cardItems.first()).toBeVisible();
    });

    test('should display card list for admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/cards');
      
      await expect(page).toHaveURL('/cards');
      await expect(page.locator('h2')).toContainText('Card List');
    });

    test('should show card details in list view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      const firstCard = page.locator('.card-item').first();
      await expect(firstCard.locator('.card-number')).toBeVisible();
      await expect(firstCard.locator('.card-type')).toBeVisible();
      await expect(firstCard.locator('.card-status')).toBeVisible();
      await expect(firstCard.locator('.expiry-date')).toBeVisible();
    });

    test('should filter cards by status', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.selectOption('select[name="statusFilter"]', 'ACTIVE');
      await page.click('button:has-text("Filter")');
      
      const activeCards = page.locator('.card-item[data-status="ACTIVE"]');
      await expect(activeCards.first()).toBeVisible();
    });

    test('should search cards by card number', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.fill('input[name="cardSearch"]', '4000123456789012');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.card-item')).toHaveCount(1);
      await expect(page.locator('.card-number')).toContainText('4000123456789012');
    });

    test('should handle empty search results', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.fill('input[name="cardSearch"]', '9999999999999999');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.no-results')).toBeVisible();
      await expect(page.locator('.no-results')).toContainText('No cards found');
    });
  });

  test.describe('Card Detail Operations', () => {
    test('should view card details from card list', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.click('.card-item:first-child .view-details');
      await expect(page).toHaveURL('/cards/detail');
      await expect(page.locator('h2')).toContainText('Card Details');
    });

    test('should display comprehensive card information', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=1');
      
      await expect(page.locator('.card-number')).toBeVisible();
      await expect(page.locator('.card-holder-name')).toBeVisible();
      await expect(page.locator('.expiry-date')).toBeVisible();
      await expect(page.locator('.card-limit')).toBeVisible();
      await expect(page.locator('.available-credit')).toBeVisible();
      await expect(page.locator('.card-status')).toBeVisible();
    });

    test('should show card transaction history', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=1');
      
      await expect(page.locator('.transaction-history')).toBeVisible();
      await expect(page.locator('.transaction-item').first()).toBeVisible();
    });

    test('should navigate to card update from detail view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=1');
      
      await page.click('button:has-text("Update Card")');
      await expect(page).toHaveURL('/cards/update');
    });

    test('should handle invalid card ID', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=999');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Card not found');
    });
  });

  test.describe('Card Update Operations', () => {
    test('should update card information successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '5000');
      await page.selectOption('select[name="cardStatus"]', 'ACTIVE');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('Card updated successfully');
    });

    test('should validate card limit input', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '-1000');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Card limit must be positive');
    });

    test('should handle card update API errors', async ({ page }) => {
      await page.route('**/api/cards/**', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Invalid card data' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '3000');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Invalid card data');
    });

    test('should prevent unauthorized card updates', async ({ page }) => {
      await page.route('**/api/cards/**', route => 
        route.fulfill({ status: 403, body: JSON.stringify({ message: 'Unauthorized' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      await page.fill('input[name="cardLimit"]', '3000');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unauthorized');
    });

    test('should reset form on cancel', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/update?cardId=1');
      
      const originalLimit = await page.inputValue('input[name="cardLimit"]');
      await page.fill('input[name="cardLimit"]', '9999');
      await page.click('button:has-text("Cancel")');
      
      const resetLimit = await page.inputValue('input[name="cardLimit"]');
      expect(resetLimit).toBe(originalLimit);
    });
  });

  test.describe('Card Navigation Flows', () => {
    test('should navigate back to card list from detail view', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=1');
      
      await page.click('button:has-text("Back to Cards")');
      await expect(page).toHaveURL('/cards');
    });

    test('should navigate back to menu from card list', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should maintain card context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.click('.card-item:first-child .view-details');
      const cardNumber = await page.textContent('.card-number');
      
      await page.click('button:has-text("Update Card")');
      const updateCardNumber = await page.textContent('.card-number');
      
      expect(cardNumber).toBe(updateCardNumber);
    });

    test('should handle role-based navigation correctly', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/cards');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });
  });

  test.describe('Card Error Handling', () => {
    test('should handle card loading errors', async ({ page }) => {
      await page.route('**/api/cards', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to load cards');
    });

    test('should handle network timeouts', async ({ page }) => {
      await page.route('**/api/cards', route => {
        return new Promise(() => {});
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
    });

    test('should retry failed requests', async ({ page }) => {
      let requestCount = 0;
      await page.route('**/api/cards', route => {
        requestCount++;
        if (requestCount === 1) {
          route.fulfill({ status: 500, body: 'Server Error' });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToCards();
      
      await page.click('button:has-text("Retry")');
      const cardItems = page.locator('.card-item');
      await expect(cardItems.first()).toBeVisible();
    });
  });
});
