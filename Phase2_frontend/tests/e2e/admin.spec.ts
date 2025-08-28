import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Admin User Management Workflows', () => {
  let loginPage: LoginPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('User Management Access Control', () => {
    test('should allow admin to access user management', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await expect(page).toHaveURL('/admin/users');
      await expect(page.locator('h2')).toContainText('User Management');
    });

    test('should prevent regular user from accessing user management', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/menu');
    });

    test('should redirect unauthenticated users to login', async ({ page }) => {
      await page.goto('/admin/users');
      await expect(page).toHaveURL('/login');
    });
  });

  test.describe('User List Operations', () => {
    test('should display user list for admin', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await expect(page.locator('.user-list')).toBeVisible();
      await expect(page.locator('.user-item').first()).toBeVisible();
    });

    test('should show user details in list', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      const firstUser = page.locator('.user-item').first();
      await expect(firstUser.locator('.user-id')).toBeVisible();
      await expect(firstUser.locator('.user-name')).toBeVisible();
      await expect(firstUser.locator('.user-type')).toBeVisible();
      await expect(firstUser.locator('.user-status')).toBeVisible();
    });

    test('should filter users by type', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.selectOption('select[name="userTypeFilter"]', 'A');
      await page.click('button:has-text("Filter")');
      
      const adminUsers = page.locator('.user-item[data-type="A"]');
      await expect(adminUsers.first()).toBeVisible();
    });

    test('should search users by ID', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.fill('input[name="userSearch"]', 'ADMIN001');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.user-item')).toHaveCount(1);
      await expect(page.locator('.user-id')).toContainText('ADMIN001');
    });

    test('should handle empty search results', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.fill('input[name="userSearch"]', 'NONEXISTENT');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.no-results')).toBeVisible();
      await expect(page.locator('.no-results')).toContainText('No users found');
    });
  });

  test.describe('Add User Operations', () => {
    test('should navigate to add user form', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await expect(page).toHaveURL('/admin/users/add');
      await expect(page.locator('h2')).toContainText('Add User');
    });

    test('should display add user form fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await expect(page.locator('input[name="userId"]')).toBeVisible();
      await expect(page.locator('input[name="firstName"]')).toBeVisible();
      await expect(page.locator('input[name="lastName"]')).toBeVisible();
      await expect(page.locator('select[name="userType"]')).toBeVisible();
      await expect(page.locator('input[name="password"]')).toBeVisible();
      await expect(page.locator('input[name="confirmPassword"]')).toBeVisible();
      await expect(page.locator('button[type="submit"]')).toBeVisible();
    });

    test('should add new user successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="userId"]', 'NEWUSER001');
      await page.fill('input[name="firstName"]', 'John');
      await page.fill('input[name="lastName"]', 'Doe');
      await page.selectOption('select[name="userType"]', 'U');
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'password123');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('User added successfully');
    });

    test('should validate required fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(6);
      await expect(page.locator('input[name="userId"] + .validation-error')).toContainText('User ID is required');
      await expect(page.locator('input[name="firstName"] + .validation-error')).toContainText('First name is required');
    });

    test('should validate user ID format', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="userId"]', 'abc');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="userId"] + .validation-error')).toContainText('User ID must be 8 characters');
    });

    test('should validate password confirmation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.fill('input[name="password"]', 'password123');
      await page.fill('input[name="confirmPassword"]', 'different123');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('input[name="confirmPassword"] + .validation-error')).toContainText('Passwords do not match');
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
  });

  test.describe('Update User Operations', () => {
    test('should navigate to update user form', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUpdateUser();
      
      await expect(page).toHaveURL('/admin/users/update');
      await expect(page.locator('h2')).toContainText('Update User');
    });

    test('should search for user to update', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUpdateUser();
      
      await page.fill('input[name="searchUserId"]', 'USER0001');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.user-details')).toBeVisible();
      await expect(page.locator('input[name="firstName"]')).toHaveValue('John');
    });

    test('should update user information successfully', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/update?userId=USER0001');
      
      await page.fill('input[name="firstName"]', 'Jane');
      await page.fill('input[name="lastName"]', 'Smith');
      await page.selectOption('select[name="userType"]', 'A');
      
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('User updated successfully');
    });

    test('should handle user not found error', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUpdateUser();
      
      await page.fill('input[name="searchUserId"]', 'NOTFOUND');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('User not found');
    });

    test('should validate update form fields', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/update?userId=USER0001');
      
      await page.fill('input[name="firstName"]', '');
      await page.fill('input[name="lastName"]', '');
      await page.click('button[type="submit"]');
      
      await expect(page.locator('.validation-error')).toHaveCount(2);
    });
  });

  test.describe('Delete User Operations', () => {
    test('should navigate to delete user form', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToDeleteUser();
      
      await expect(page).toHaveURL('/admin/users/delete');
      await expect(page.locator('h2')).toContainText('Delete User');
    });

    test('should search for user to delete', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToDeleteUser();
      
      await page.fill('input[name="searchUserId"]', 'USER0001');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.user-details')).toBeVisible();
      await expect(page.locator('.delete-confirmation')).toBeVisible();
    });

    test('should require confirmation for user deletion', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/delete?userId=USER0001');
      
      await page.click('button:has-text("Delete User")');
      
      await expect(page.locator('.confirmation-dialog')).toBeVisible();
      await expect(page.locator('.confirmation-dialog')).toContainText('Are you sure you want to delete this user?');
    });

    test('should delete user successfully with confirmation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/delete?userId=USER0001');
      
      await page.click('button:has-text("Delete User")');
      await page.click('button:has-text("Confirm Delete")');
      
      await expect(page.locator('.success-message')).toBeVisible();
      await expect(page.locator('.success-message')).toContainText('User deleted successfully');
    });

    test('should cancel user deletion', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/delete?userId=USER0001');
      
      await page.click('button:has-text("Delete User")');
      await page.click('button:has-text("Cancel")');
      
      await expect(page.locator('.confirmation-dialog')).not.toBeVisible();
      await expect(page.locator('.user-details')).toBeVisible();
    });

    test('should prevent deletion of current admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToDeleteUser();
      
      await page.fill('input[name="searchUserId"]', 'ADMIN001');
      await page.click('button:has-text("Search")');
      
      await expect(page.locator('.warning-message')).toBeVisible();
      await expect(page.locator('.warning-message')).toContainText('Cannot delete current user');
      await expect(page.locator('button:has-text("Delete User")')).toBeDisabled();
    });

    test('should handle user deletion errors', async ({ page }) => {
      await page.route('**/api/admin/users/**', route => 
        route.fulfill({ status: 400, body: JSON.stringify({ message: 'Cannot delete user with active transactions' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await page.goto('/admin/users/delete?userId=USER0001');
      
      await page.click('button:has-text("Delete User")');
      await page.click('button:has-text("Confirm Delete")');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Cannot delete user with active transactions');
    });
  });

  test.describe('Admin Navigation', () => {
    test('should navigate back to admin menu from user management', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.click('button:has-text("Back to Admin Menu")');
      await expect(page).toHaveURL('/admin-menu');
    });

    test('should navigate between user management operations', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToAddUser();
      
      await page.click('button:has-text("Update User")');
      await expect(page).toHaveURL('/admin/users/update');
      
      await page.click('button:has-text("Delete User")');
      await expect(page).toHaveURL('/admin/users/delete');
      
      await page.click('button:has-text("User List")');
      await expect(page).toHaveURL('/admin/users');
    });

    test('should maintain admin context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.fill('input[name="userSearch"]', 'USER0001');
      await page.click('button:has-text("Search")');
      
      await page.click('button:has-text("Update User")');
      const searchValue = await page.inputValue('input[name="searchUserId"]');
      
      expect(searchValue).toBe('USER0001');
    });
  });

  test.describe('Admin Error Handling', () => {
    test('should handle user management API errors', async ({ page }) => {
      await page.route('**/api/admin/users', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to load users');
    });

    test('should handle network timeouts', async ({ page }) => {
      await page.route('**/api/admin/users', route => {
        return new Promise(() => {});
      });
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
    });

    test('should retry failed requests', async ({ page }) => {
      let requestCount = 0;
      await page.route('**/api/admin/users', route => {
        requestCount++;
        if (requestCount === 1) {
          route.fulfill({ status: 500, body: 'Server Error' });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToUserManagement();
      
      await page.click('button:has-text("Retry")');
      await expect(page.locator('.user-item').first()).toBeVisible();
    });
  });
});
