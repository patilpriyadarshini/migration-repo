import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Report Generation Workflows', () => {
  let loginPage: LoginPage;
  let mainMenuPage: MainMenuPage;
  let adminMenuPage: AdminMenuPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    mainMenuPage = new MainMenuPage(page);
    adminMenuPage = new AdminMenuPage(page);
  });

  test.describe('Monthly Reports', () => {
    test('should generate monthly report for regular user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToReports();
      
      await expect(page).toHaveURL('/reports');
      await expect(page.locator('h2')).toContainText('Reports');
      await page.click('button:has-text("Monthly Report")');
      
      await expect(page).toHaveURL('/reports/monthly');
      await expect(page.locator('.report-content')).toBeVisible();
    });

    test('should generate monthly report for admin user', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToReports();
      
      await expect(page).toHaveURL('/reports');
      await page.click('button:has-text("Monthly Report")');
      
      await expect(page).toHaveURL('/reports/monthly');
      await expect(page.locator('.report-content')).toBeVisible();
    });

    test('should display monthly report data', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await expect(page.locator('.report-title')).toContainText('Monthly Report');
      await expect(page.locator('.report-period')).toBeVisible();
      await expect(page.locator('.transaction-summary')).toBeVisible();
      await expect(page.locator('.account-summary')).toBeVisible();
      await expect(page.locator('.total-balance')).toBeVisible();
    });

    test('should allow selecting different months', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.selectOption('select[name="month"]', '01');
      await page.selectOption('select[name="year"]', '2024');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-period')).toContainText('January 2024');
    });

    test('should export monthly report to PDF', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      const downloadPromise = page.waitForEvent('download');
      await page.click('button:has-text("Export PDF")');
      const download = await downloadPromise;
      
      expect(download.suggestedFilename()).toContain('monthly-report');
      expect(download.suggestedFilename()).toContain('.pdf');
    });

    test('should export monthly report to CSV', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      const downloadPromise = page.waitForEvent('download');
      await page.click('button:has-text("Export CSV")');
      const download = await downloadPromise;
      
      expect(download.suggestedFilename()).toContain('monthly-report');
      expect(download.suggestedFilename()).toContain('.csv');
    });
  });

  test.describe('Yearly Reports', () => {
    test('should generate yearly report', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToReports();
      
      await page.click('button:has-text("Yearly Report")');
      await expect(page).toHaveURL('/reports/yearly');
      await expect(page.locator('.report-content')).toBeVisible();
    });

    test('should display yearly report data', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/yearly');
      
      await expect(page.locator('.report-title')).toContainText('Yearly Report');
      await expect(page.locator('.yearly-summary')).toBeVisible();
      await expect(page.locator('.monthly-breakdown')).toBeVisible();
      await expect(page.locator('.category-analysis')).toBeVisible();
    });

    test('should allow selecting different years', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/yearly');
      
      await page.selectOption('select[name="year"]', '2023');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-period')).toContainText('2023');
    });

    test('should show year-over-year comparison', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/yearly');
      
      await page.check('input[name="showComparison"]');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.comparison-chart')).toBeVisible();
      await expect(page.locator('.growth-metrics')).toBeVisible();
    });
  });

  test.describe('Custom Date Range Reports', () => {
    test('should generate custom date range report', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToReports();
      
      await page.click('button:has-text("Custom Report")');
      await expect(page).toHaveURL('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-03-31');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-content')).toBeVisible();
    });

    test('should validate date range', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-03-31');
      await page.fill('input[name="endDate"]', '2024-01-01');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('End date must be after start date');
    });

    test('should limit date range to maximum period', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2020-01-01');
      await page.fill('input[name="endDate"]', '2024-12-31');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.validation-error')).toBeVisible();
      await expect(page.locator('.validation-error')).toContainText('Date range cannot exceed 2 years');
    });

    test('should filter by transaction type', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-03-31');
      await page.selectOption('select[name="transactionType"]', 'PURCHASE');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-filters')).toContainText('Transaction Type: PURCHASE');
    });

    test('should filter by account', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/custom');
      
      await page.fill('input[name="startDate"]', '2024-01-01');
      await page.fill('input[name="endDate"]', '2024-03-31');
      await page.fill('input[name="accountNumber"]', '12345678901');
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.report-filters')).toContainText('Account: 12345678901');
    });
  });

  test.describe('Admin-Only Reports', () => {
    test('should allow admin to access system reports', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToReports();
      
      await expect(page.locator('button:has-text("System Report")')).toBeVisible();
      await expect(page.locator('button:has-text("User Activity Report")')).toBeVisible();
      await expect(page.locator('button:has-text("Audit Report")')).toBeVisible();
    });

    test('should prevent regular user from accessing admin reports', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await mainMenuPage.navigateToReports();
      
      await expect(page.locator('button:has-text("System Report")')).not.toBeVisible();
      await expect(page.locator('button:has-text("User Activity Report")')).not.toBeVisible();
      await expect(page.locator('button:has-text("Audit Report")')).not.toBeVisible();
    });

    test('should generate system report for admin', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToReports();
      
      await page.click('button:has-text("System Report")');
      await expect(page).toHaveURL('/reports/system');
      
      await expect(page.locator('.system-metrics')).toBeVisible();
      await expect(page.locator('.performance-stats')).toBeVisible();
      await expect(page.locator('.error-summary')).toBeVisible();
    });

    test('should generate user activity report for admin', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      await adminMenuPage.navigateToReports();
      
      await page.click('button:has-text("User Activity Report")');
      await expect(page).toHaveURL('/reports/user-activity');
      
      await expect(page.locator('.activity-summary')).toBeVisible();
      await expect(page.locator('.login-stats')).toBeVisible();
      await expect(page.locator('.transaction-stats')).toBeVisible();
    });
  });

  test.describe('Report Error Handling', () => {
    test('should handle report generation errors', async ({ page }) => {
      await page.route('**/api/reports/**', route => 
        route.fulfill({ status: 500, body: 'Internal Server Error' })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.error-message')).toBeVisible();
      await expect(page.locator('.error-message')).toContainText('Unable to generate report');
    });

    test('should handle network timeouts', async ({ page }) => {
      await page.route('**/api/reports/**', route => {
        return new Promise(() => {});
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.loading-spinner')).toBeVisible();
    });

    test('should handle empty report data', async ({ page }) => {
      await page.route('**/api/reports/**', route => 
        route.fulfill({ status: 200, body: JSON.stringify({ data: [], message: 'No data found' }) })
      );
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      
      await expect(page.locator('.empty-report')).toBeVisible();
      await expect(page.locator('.empty-report')).toContainText('No data available for the selected period');
    });

    test('should retry failed report generation', async ({ page }) => {
      let requestCount = 0;
      await page.route('**/api/reports/**', route => {
        requestCount++;
        if (requestCount === 1) {
          route.fulfill({ status: 500, body: 'Server Error' });
        } else {
          route.continue();
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Generate Report")');
      await page.click('button:has-text("Retry")');
      
      await expect(page.locator('.report-content')).toBeVisible();
    });
  });

  test.describe('Report Navigation', () => {
    test('should navigate back to menu from reports', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Back to Menu")');
      await expect(page).toHaveURL('/menu');
    });

    test('should navigate between different report types', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.click('button:has-text("Yearly Report")');
      await expect(page).toHaveURL('/reports/yearly');
      
      await page.click('button:has-text("Custom Report")');
      await expect(page).toHaveURL('/reports/custom');
    });

    test('should maintain report context across navigation', async ({ page }) => {
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      await page.goto('/reports/monthly');
      
      await page.selectOption('select[name="month"]', '03');
      await page.selectOption('select[name="year"]', '2024');
      
      await page.click('button:has-text("Back to Reports")');
      await page.click('button:has-text("Monthly Report")');
      
      const selectedMonth = await page.inputValue('select[name="month"]');
      const selectedYear = await page.inputValue('select[name="year"]');
      
      expect(selectedMonth).toBe('03');
      expect(selectedYear).toBe('2024');
    });
  });
});
