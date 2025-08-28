import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Multi-User Concurrent Testing', () => {
  test.describe('Complex Concurrent Session Management', () => {
    test('should handle 15+ concurrent user sessions with complex state isolation', async ({ browser }) => {
      const contexts = await Promise.all(
        Array.from({ length: 15 }, () => browser.newContext())
      );
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));
      const mainMenuPages = pages.map(page => new MainMenuPage(page));
      
      const userCredentials = [
        { userId: 'USER0001', password: 'user1234', type: 'regular' },
        { userId: 'ADMIN001', password: 'admin123', type: 'admin' },
        { userId: 'USER0002', password: 'user1234', type: 'regular' },
        { userId: 'USER0003', password: 'user1234', type: 'regular' },
        { userId: 'ADMIN002', password: 'admin123', type: 'admin' }
      ];
      
      const loginPromises = pages.map(async (page, index) => {
        const cred = userCredentials[index % userCredentials.length];
        await loginPages[index].goto();
        await loginPages[index].login(cred.userId, cred.password);
        
        if (cred.type === 'admin') {
          await expect(page).toHaveURL(/.*admin-menu.*/);
        } else {
          await expect(page).toHaveURL(/.*menu.*/);
        }
      });
      
      await Promise.all(loginPromises);
      
      const concurrentOperations = pages.map(async (page, index) => {
        const cred = userCredentials[index % userCredentials.length];
        
        if (cred.type === 'admin') {
          const adminMenu = new AdminMenuPage(page);
          await adminMenu.navigateToUserManagement();
          
          await page.route('**/api/admin/users**', async (route) => {
            const users = Array.from({ length: 100 }, (_, i) => ({
              userId: `USER${String(i + 1).padStart(4, '0')}`,
              firstName: `User${i + 1}`,
              lastName: `Test${i + 1}`,
              userType: i % 5 === 0 ? 'A' : 'U',
              status: 'active'
            }));
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: users, totalElements: users.length })
            });
          });
          
          await page.reload();
          await page.waitForTimeout(2000);
          
          const userRows = await page.locator('.user-row, tr').count();
          expect(userRows).toBeGreaterThan(10);
          
        } else {
          await mainMenuPages[index].navigateToAccountView();
          
          await page.route('**/api/accounts/**', async (route) => {
            const accountData = {
              accountNumber: `12345678${String(index).padStart(3, '0')}`,
              balance: Math.random() * 10000,
              accountType: 'CHECKING',
              status: 'ACTIVE',
              customerId: `CUST${String(index).padStart(4, '0')}`
            };
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify(accountData)
            });
          });
          
          const accountInput = page.locator('input[name="accountNumber"]');
          if (await accountInput.isVisible()) {
            await accountInput.fill(`12345678${String(index).padStart(3, '0')}`);
            await page.click('button:has-text("Search"), button:has-text("View")');
            await page.waitForTimeout(1000);
          }
        }
      });
      
      await Promise.all(concurrentOperations);
      
      const memoryUsage = await Promise.all(
        pages.map(page => page.evaluate(() => {
          return {
            usedJSHeapSize: (performance as any).memory?.usedJSHeapSize || 0,
            totalJSHeapSize: (performance as any).memory?.totalJSHeapSize || 0
          };
        }))
      );
      
      const avgMemoryUsage = memoryUsage.reduce((sum, mem) => sum + mem.usedJSHeapSize, 0) / memoryUsage.length;
      expect(avgMemoryUsage).toBeLessThan(50 * 1024 * 1024); // Less than 50MB per session
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });

    test('should handle complex race conditions in concurrent form submissions', async ({ browser }) => {
      const contexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const adminMenuPages = pages.map(page => new AdminMenuPage(page));
      
      await Promise.all(pages.map(async (page, index) => {
        const loginPage = new LoginPage(page);
        await loginPage.goto();
        await loginPage.login('ADMIN001', 'admin123');
      }));
      
      await Promise.all(adminMenuPages.map(menu => menu.navigateToUserAdd()));
      
      let submissionCount = 0;
      const submissionResults = [];
      
      await Promise.all(pages.map(page => 
        page.route('**/api/admin/users', async (route) => {
          submissionCount++;
          const delay = Math.random() * 2000; // Random delay 0-2 seconds
          
          await new Promise(resolve => setTimeout(resolve, delay));
          
          if (submissionCount <= 3) {
            submissionResults.push({ success: true, timestamp: Date.now() });
            await route.fulfill({
              status: 201,
              contentType: 'application/json',
              body: JSON.stringify({ message: 'User created successfully', userId: `USER${submissionCount.toString().padStart(4, '0')}` })
            });
          } else {
            submissionResults.push({ success: false, timestamp: Date.now() });
            await route.fulfill({
              status: 409,
              contentType: 'application/json',
              body: JSON.stringify({ error: 'User already exists or concurrent modification detected' })
            });
          }
        })
      ));
      
      const formSubmissions = pages.map(async (page, index) => {
        const userIdInput = page.locator('input[name="userId"]');
        const firstNameInput = page.locator('input[name="firstName"]');
        const lastNameInput = page.locator('input[name="lastName"]');
        const submitButton = page.locator('button[type="submit"]');
        
        if (await userIdInput.isVisible()) {
          await userIdInput.fill(`TESTUSER${index}`);
          await firstNameInput.fill(`Test${index}`);
          await lastNameInput.fill(`User${index}`);
          
          await submitButton.click();
          
          await page.waitForTimeout(3000);
          
          const successMessage = page.locator('.success-message, .alert-success');
          const errorMessage = page.locator('.error-message, .alert-danger');
          
          if (await successMessage.isVisible()) {
            return { index, result: 'success' };
          } else if (await errorMessage.isVisible()) {
            return { index, result: 'error' };
          } else {
            return { index, result: 'unknown' };
          }
        }
        
        return { index, result: 'form_not_found' };
      });
      
      const results = await Promise.all(formSubmissions);
      
      const successCount = results.filter(r => r.result === 'success').length;
      const errorCount = results.filter(r => r.result === 'error').length;
      
      expect(successCount).toBeGreaterThan(0);
      expect(successCount + errorCount).toBe(5);
      expect(submissionResults.length).toBe(5);
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });

  test.describe('Advanced Cross-User Data Consistency Testing', () => {
    test('should maintain data consistency across multiple admin users modifying same records', async ({ browser }) => {
      const adminContexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);
      
      const adminPages = await Promise.all(adminContexts.map(ctx => ctx.newPage()));
      const adminMenus = adminPages.map(page => new AdminMenuPage(page));
      
      await Promise.all(adminPages.map(async page => {
        const loginPage = new LoginPage(page);
        await loginPage.goto();
        await loginPage.login('ADMIN001', 'admin123');
      }));
      
      await Promise.all(adminMenus.map(menu => menu.navigateToUserManagement()));
      
      let updateAttempts = 0;
      const updateResults = [];
      
      await Promise.all(adminPages.map(page => 
        page.route('**/api/admin/users/USER0001', async (route) => {
          updateAttempts++;
          const method = route.request().method();
          
          if (method === 'PUT') {
            const delay = Math.random() * 1000;
            await new Promise(resolve => setTimeout(resolve, delay));
            
            if (updateAttempts === 1) {
              updateResults.push({ success: true, version: 1 });
              await route.fulfill({
                status: 200,
                contentType: 'application/json',
                body: JSON.stringify({ 
                  message: 'User updated successfully',
                  version: 1,
                  lastModified: new Date().toISOString()
                })
              });
            } else {
              updateResults.push({ success: false, conflict: true });
              await route.fulfill({
                status: 409,
                contentType: 'application/json',
                body: JSON.stringify({ 
                  error: 'Conflict: User has been modified by another admin',
                  currentVersion: 1
                })
              });
            }
          } else {
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({
                userId: 'USER0001',
                firstName: 'John',
                lastName: 'Doe',
                userType: 'U',
                status: 'active',
                version: 0
              })
            });
          }
        })
      ));
      
      const concurrentUpdates = adminPages.map(async (page, index) => {
        const editButton = page.locator('button:has-text("Edit"), a:has-text("Edit")').first();
        if (await editButton.isVisible()) {
          await editButton.click();
          await page.waitForTimeout(500);
        }
        
        const firstNameInput = page.locator('input[name="firstName"]');
        if (await firstNameInput.isVisible()) {
          await firstNameInput.fill(`UpdatedName${index}`);
          
          const saveButton = page.locator('button:has-text("Save"), button:has-text("Update")');
          await saveButton.click();
          
          await page.waitForTimeout(2000);
          
          const successMessage = page.locator('.success-message, .alert-success');
          const conflictMessage = page.locator('.error-message, .alert-danger, text=conflict, text=modified');
          
          if (await successMessage.isVisible()) {
            return { index, result: 'success' };
          } else if (await conflictMessage.isVisible()) {
            return { index, result: 'conflict' };
          } else {
            return { index, result: 'unknown' };
          }
        }
        
        return { index, result: 'form_not_found' };
      });
      
      const results = await Promise.all(concurrentUpdates);
      
      const successCount = results.filter(r => r.result === 'success').length;
      const conflictCount = results.filter(r => r.result === 'conflict').length;
      
      expect(successCount).toBe(1);
      expect(conflictCount).toBeGreaterThan(0);
      expect(updateResults.length).toBe(3);
      
      await Promise.all(adminContexts.map(ctx => ctx.close()));
    });

    test('should handle complex multi-step workflows with state synchronization', async ({ browser }) => {
      const userContext = await browser.newContext();
      const adminContext = await browser.newContext();
      
      const userPage = await userContext.newPage();
      const adminPage = await adminContext.newPage();
      
      const userLogin = new LoginPage(userPage);
      const adminLogin = new LoginPage(adminPage);
      const userMenu = new MainMenuPage(userPage);
      const adminMenu = new AdminMenuPage(adminPage);
      
      await userLogin.goto();
      await userLogin.login('USER0001', 'user1234');
      
      await adminLogin.goto();
      await adminLogin.login('ADMIN001', 'admin123');
      
      let transactionState = {
        pending: [],
        approved: [],
        rejected: []
      };
      
      await userPage.route('**/api/transactions**', async (route) => {
        const method = route.request().method();
        const url = route.request().url();
        
        if (method === 'POST') {
          const transactionData = await route.request().postDataJSON();
          const newTransaction = {
            id: `TXN${Date.now()}`,
            ...transactionData,
            status: 'pending',
            timestamp: new Date().toISOString()
          };
          
          transactionState.pending.push(newTransaction);
          
          await route.fulfill({
            status: 201,
            contentType: 'application/json',
            body: JSON.stringify(newTransaction)
          });
        } else {
          const allTransactions = [
            ...transactionState.pending,
            ...transactionState.approved,
            ...transactionState.rejected
          ];
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ content: allTransactions, totalElements: allTransactions.length })
          });
        }
      });
      
      await adminPage.route('**/api/admin/transactions**', async (route) => {
        const method = route.request().method();
        const url = route.request().url();
        
        if (method === 'PUT' && url.includes('/approve/')) {
          const transactionId = url.split('/approve/')[1];
          const transaction = transactionState.pending.find(t => t.id === transactionId);
          
          if (transaction) {
            transaction.status = 'approved';
            transactionState.approved.push(transaction);
            transactionState.pending = transactionState.pending.filter(t => t.id !== transactionId);
          }
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ message: 'Transaction approved', transactionId })
          });
        } else {
          const pendingTransactions = transactionState.pending;
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ content: pendingTransactions, totalElements: pendingTransactions.length })
          });
        }
      });
      
      await userMenu.navigateToTransactions();
      
      const addTransactionButton = userPage.locator('button:has-text("Add"), a:has-text("Add")');
      if (await addTransactionButton.isVisible()) {
        await addTransactionButton.click();
        
        const amountInput = userPage.locator('input[name="amount"]');
        const merchantInput = userPage.locator('input[name="merchant"]');
        const submitButton = userPage.locator('button[type="submit"]');
        
        if (await amountInput.isVisible()) {
          await amountInput.fill('150.00');
          await merchantInput.fill('Test Merchant');
          await submitButton.click();
          
          await userPage.waitForTimeout(2000);
        }
      }
      
      await adminMenu.navigateToTransactionApproval();
      
      await adminPage.waitForTimeout(1000);
      
      const pendingTransaction = adminPage.locator('.transaction-row, tr').first();
      if (await pendingTransaction.isVisible()) {
        const approveButton = adminPage.locator('button:has-text("Approve")').first();
        if (await approveButton.isVisible()) {
          await approveButton.click();
          await adminPage.waitForTimeout(1000);
        }
      }
      
      await userPage.reload();
      await userPage.waitForTimeout(2000);
      
      const approvedTransaction = userPage.locator('text=approved, .status-approved');
      expect(await approvedTransaction.isVisible()).toBeTruthy();
      
      expect(transactionState.approved.length).toBe(1);
      expect(transactionState.pending.length).toBe(0);
      
      await userContext.close();
      await adminContext.close();
    });
  });

  test.describe('Advanced Performance Under Concurrent Load', () => {
    test('should maintain sub-3-second response times under 20+ concurrent user load', async ({ browser }) => {
      const contexts = await Promise.all(
        Array.from({ length: 20 }, () => browser.newContext())
      );
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const performanceMetrics = [];
      
      const loadTestOperations = pages.map(async (page, index) => {
        const startTime = Date.now();
        
        const loginPage = new LoginPage(page);
        await loginPage.goto();
        
        const loginStartTime = Date.now();
        await loginPage.login('USER0001', 'user1234');
        const loginEndTime = Date.now();
        
        const mainMenu = new MainMenuPage(page);
        
        const navigationStartTime = Date.now();
        await mainMenu.navigateToAccountView();
        const navigationEndTime = Date.now();
        
        await page.route('**/api/accounts/**', async (route) => {
          const delay = Math.random() * 500; // Random API delay
          await new Promise(resolve => setTimeout(resolve, delay));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              accountNumber: `12345678${String(index).padStart(3, '0')}`,
              balance: Math.random() * 10000,
              accountType: 'CHECKING',
              status: 'ACTIVE'
            })
          });
        });
        
        const dataLoadStartTime = Date.now();
        const accountInput = page.locator('input[name="accountNumber"]');
        if (await accountInput.isVisible()) {
          await accountInput.fill(`12345678${String(index).padStart(3, '0')}`);
          await page.click('button:has-text("Search"), button:has-text("View")');
          await page.waitForTimeout(1000);
        }
        const dataLoadEndTime = Date.now();
        
        const totalTime = Date.now() - startTime;
        
        performanceMetrics.push({
          sessionIndex: index,
          loginTime: loginEndTime - loginStartTime,
          navigationTime: navigationEndTime - navigationStartTime,
          dataLoadTime: dataLoadEndTime - dataLoadStartTime,
          totalTime: totalTime
        });
        
        return { index, totalTime };
      });
      
      const results = await Promise.all(loadTestOperations);
      
      const avgLoginTime = performanceMetrics.reduce((sum, m) => sum + m.loginTime, 0) / performanceMetrics.length;
      const avgNavigationTime = performanceMetrics.reduce((sum, m) => sum + m.navigationTime, 0) / performanceMetrics.length;
      const avgDataLoadTime = performanceMetrics.reduce((sum, m) => sum + m.dataLoadTime, 0) / performanceMetrics.length;
      const avgTotalTime = performanceMetrics.reduce((sum, m) => sum + m.totalTime, 0) / performanceMetrics.length;
      
      expect(avgLoginTime).toBeLessThan(2000); // Less than 2 seconds
      expect(avgNavigationTime).toBeLessThan(1000); // Less than 1 second
      expect(avgDataLoadTime).toBeLessThan(3000); // Less than 3 seconds
      expect(avgTotalTime).toBeLessThan(8000); // Less than 8 seconds total
      
      const slowSessions = results.filter(r => r.totalTime > 10000);
      expect(slowSessions.length).toBeLessThan(2); // Less than 10% slow sessions
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });
});
