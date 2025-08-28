import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';

test.describe('Protected Routes Access Control', () => {
  let loginPage: LoginPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
  });

  test.describe('Unauthenticated Access', () => {
    test('should redirect to login for protected routes', async ({ page }) => {
      const protectedRoutes = [
        '/menu',
        '/admin-menu',
        '/accounts/view',
        '/accounts/update',
        '/cards',
        '/cards/detail',
        '/cards/update',
        '/transactions',
        '/transactions/view',
        '/transactions/add',
        '/bill-payment',
        '/reports',
        '/reports/monthly',
        '/reports/yearly',
        '/reports/custom',
        '/admin/users',
        '/admin/users/add',
        '/admin/users/update',
        '/admin/users/delete'
      ];

      for (const route of protectedRoutes) {
        await page.goto(route);
        await expect(page).toHaveURL('/login');
      }
    });

    test('should allow access to public routes', async ({ page }) => {
      await page.goto('/login');
      await expect(page).toHaveURL('/login');
      await expect(page.locator('h2')).toContainText('CardDemo System');
    });

    test('should redirect root to login when not authenticated', async ({ page }) => {
      await page.goto('/');
      await expect(page).toHaveURL('/login');
    });

    test('should handle invalid routes gracefully', async ({ page }) => {
      await page.goto('/invalid-route');
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('Regular User Access Control', () => {
    test('should allow regular user access to user routes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      const allowedRoutes = [
        '/menu',
        '/accounts/view',
        '/accounts/update',
        '/cards',
        '/cards/detail',
        '/cards/update',
        '/transactions',
        '/transactions/view',
        '/transactions/add',
        '/bill-payment',
        '/reports',
        '/reports/monthly',
        '/reports/yearly',
        '/reports/custom'
      ];

      for (const route of allowedRoutes) {
        await page.goto(route);
        await expect(page).not.toHaveURL('/login');
        await expect(page).toHaveURL(route);
      }
    });

    test('should deny regular user access to admin routes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      const deniedRoutes = [
        '/admin-menu',
        '/admin/users',
        '/admin/users/add',
        '/admin/users/update',
        '/admin/users/delete',
        '/reports/system',
        '/reports/user-activity',
        '/reports/audit'
      ];

      for (const route of deniedRoutes) {
        await page.goto(route);
        await expect(page).toHaveURL('/menu');
      }
    });

    test('should redirect regular user to menu after login', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
    });

    test('should maintain regular user session across page refreshes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.reload();
      await expect(page).toHaveURL('/accounts/view');
      await expect(page.locator('h2')).toContainText('Account View');
    });

    test('should handle session expiration for regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.evaluate(() => {
        localStorage.removeItem('userId');
        localStorage.removeItem('userType');
      });
      
      await page.reload();
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('Admin User Access Control', () => {
    test('should allow admin access to all routes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      const adminRoutes = [
        '/admin-menu',
        '/admin/users',
        '/admin/users/add',
        '/admin/users/update',
        '/admin/users/delete',
        '/accounts/view',
        '/accounts/update',
        '/cards',
        '/cards/detail',
        '/cards/update',
        '/transactions',
        '/transactions/view',
        '/transactions/add',
        '/bill-payment',
        '/reports',
        '/reports/monthly',
        '/reports/yearly',
        '/reports/custom',
        '/reports/system',
        '/reports/user-activity'
      ];

      for (const route of adminRoutes) {
        await page.goto(route);
        await expect(page).not.toHaveURL('/login');
        await expect(page).toHaveURL(route);
      }
    });

    test('should redirect admin to admin-menu after login', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should maintain admin session across page refreshes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users');
      
      await page.reload();
      await expect(page).toHaveURL('/admin/users');
      await expect(page.locator('h2')).toContainText('User Management');
    });

    test('should handle session expiration for admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users');
      
      await page.evaluate(() => {
        localStorage.removeItem('userId');
        localStorage.removeItem('userType');
      });
      
      await page.reload();
      await expect(page).toHaveURL('/login');
    });

    test('should allow admin to access regular user routes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await page.goto('/menu');
      await expect(page).toHaveURL('/menu');
      
      await page.goto('/accounts/view');
      await expect(page).toHaveURL('/accounts/view');
    });
  });

  test.describe('Route Parameter Validation', () => {
    test('should validate card ID parameter', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/cards/detail?cardId=invalid');
      await expect(page.locator('.error-message')).toContainText('Invalid card ID');
    });

    test('should validate transaction ID parameter', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/transactions/view?transactionId=invalid');
      await expect(page.locator('.error-message')).toContainText('Invalid transaction ID');
    });

    test('should validate user ID parameter for admin routes', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await page.goto('/admin/users/update?userId=invalid');
      await expect(page.locator('.error-message')).toContainText('Invalid user ID format');
    });

    test('should handle missing required parameters', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/cards/detail');
      await expect(page.locator('.error-message')).toContainText('Card ID is required');
    });
  });

  test.describe('Deep Link Protection', () => {
    test('should preserve intended route after login', async ({ page }) => {
      await page.goto('/accounts/view');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/accounts/view');
    });

    test('should redirect to appropriate menu if intended route is forbidden', async ({ page }) => {
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
    });

    test('should handle deep links with parameters', async ({ page }) => {
      await page.goto('/cards/detail?cardId=1');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/cards/detail?cardId=1');
    });

    test('should preserve query parameters after authentication', async ({ page }) => {
      await page.goto('/reports/custom?startDate=2024-01-01&endDate=2024-01-31');
      await expect(page).toHaveURL('/login');
      
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/reports/custom?startDate=2024-01-01&endDate=2024-01-31');
    });
  });

  test.describe('Cross-User Access Control', () => {
    test('should prevent access to other users data', async ({ page }) => {
      await page.route('**/api/accounts/**', route => {
        if (route.request().url().includes('99999999999')) {
          route.fulfill({ status: 403, body: JSON.stringify({ message: 'Access denied' }) });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.fill('input[name="accountNumber"]', '99999999999');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.error-message')).toContainText('Access denied');
    });

    test('should validate card ownership', async ({ page }) => {
      await page.route('**/api/cards/**', route => {
        if (route.request().url().includes('cardId=999')) {
          route.fulfill({ status: 403, body: JSON.stringify({ message: 'Card not found or access denied' }) });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/cards/detail?cardId=999');
      
      await expect(page.locator('.error-message')).toContainText('Card not found or access denied');
    });

    test('should validate transaction ownership', async ({ page }) => {
      await page.route('**/api/transactions/**', route => {
        if (route.request().url().includes('transactionId=999')) {
          route.fulfill({ status: 403, body: JSON.stringify({ message: 'Transaction not found or access denied' }) });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/transactions/view?transactionId=999');
      
      await expect(page.locator('.error-message')).toContainText('Transaction not found or access denied');
    });
  });

  test.describe('Session Management', () => {
    test('should handle concurrent sessions', async ({ page, context }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await expect(page).toHaveURL('/menu');
      
      const secondPage = await context.newPage();
      await secondPage.goto('/accounts/view');
      await expect(secondPage).toHaveURL('/accounts/view');
      
      await page.click('button:has-text("Logout")');
      await expect(page).toHaveURL('/login');
      
      await secondPage.reload();
      await expect(secondPage).toHaveURL('/login');
    });

    test('should handle invalid session tokens', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.evaluate(() => {
        localStorage.setItem('userId', 'INVALID');
      });
      
      await page.reload();
      await expect(page).toHaveURL('/login');
    });

    test('should handle session timeout', async ({ page }) => {
      await page.route('**/api/**', route => {
        if (route.request().headers()['authorization']) {
          route.fulfill({ status: 401, body: JSON.stringify({ message: 'Session expired' }) });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/accounts/view');
      
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button[type="submit"]');
      
      await expect(page).toHaveURL('/login');
      await expect(page.locator('.error-message')).toContainText('Session expired');
    });
  });
});
