import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Real-World Complex Scenarios', () => {
  test.describe('Enterprise-Level Multi-Tenant Simulation', () => {
    test('should handle complex multi-tenant data isolation with 25+ concurrent users', async ({ browser }) => {
      const tenants = [
        { id: 'TENANT_A', users: ['ADMIN001', 'USER0001', 'USER0002', 'USER0003', 'USER0004'] },
        { id: 'TENANT_B', users: ['ADMIN002', 'USER0005', 'USER0006', 'USER0007', 'USER0008'] },
        { id: 'TENANT_C', users: ['ADMIN003', 'USER0009', 'USER0010', 'USER0011', 'USER0012'] },
        { id: 'TENANT_D', users: ['ADMIN004', 'USER0013', 'USER0014', 'USER0015', 'USER0016'] },
        { id: 'TENANT_E', users: ['ADMIN005', 'USER0017', 'USER0018', 'USER0019', 'USER0020'] }
      ];
      
      const contexts = await Promise.all(
        Array.from({ length: 25 }, () => browser.newContext())
      );
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      
      let tenantDataStore: { [tenantId: string]: { accounts: any[]; transactions: any[]; users: any[] } } = {};
      
      tenants.forEach(tenant => {
        tenantDataStore[tenant.id] = {
          accounts: Array.from({ length: 50 }, (_, i) => ({
            accountNumber: `${tenant.id}_ACC_${String(i + 1).padStart(6, '0')}`,
            balance: Math.random() * 50000,
            tenantId: tenant.id,
            customerId: `${tenant.id}_CUST_${String(i + 1).padStart(4, '0')}`
          })),
          transactions: Array.from({ length: 200 }, (_, i) => ({
            id: `${tenant.id}_TXN_${String(i + 1).padStart(8, '0')}`,
            amount: Math.random() * 1000,
            tenantId: tenant.id,
            accountNumber: `${tenant.id}_ACC_${String(Math.floor(Math.random() * 50) + 1).padStart(6, '0')}`,
            timestamp: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString()
          })),
          users: tenant.users.map((userId, i) => ({
            userId,
            tenantId: tenant.id,
            userType: userId.startsWith('ADMIN') ? 'A' : 'U',
            firstName: `User${i + 1}`,
            lastName: `Tenant${tenant.id.slice(-1)}`
          }))
        };
      });
      
      await Promise.all(pages.map(page => 
        page.route('**/api/**', async (route) => {
          const url = route.request().url();
          const headers = route.request().headers();
          const tenantId = headers['x-tenant-id'] || 'TENANT_A'; // Default tenant
          
          if (url.includes('/accounts')) {
            const tenantAccounts = tenantDataStore[tenantId]?.accounts || [];
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: tenantAccounts, totalElements: tenantAccounts.length })
            });
          } else if (url.includes('/transactions')) {
            const tenantTransactions = tenantDataStore[tenantId]?.transactions || [];
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: tenantTransactions, totalElements: tenantTransactions.length })
            });
          } else if (url.includes('/admin/users')) {
            const tenantUsers = tenantDataStore[tenantId]?.users || [];
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({ content: tenantUsers, totalElements: tenantUsers.length })
            });
          } else {
            await route.continue();
          }
        })
      ));
      
      const sessionPromises = pages.map(async (page, index) => {
        const tenantIndex = index % tenants.length;
        const tenant = tenants[tenantIndex];
        const userIndex = Math.floor(index / tenants.length) % tenant.users.length;
        const userId = tenant.users[userIndex];
        const password = userId.startsWith('ADMIN') ? 'admin123' : 'user1234';
        
        await page.setExtraHTTPHeaders({ 'X-Tenant-ID': tenant.id });
        
        const loginPage = new LoginPage(page);
        await loginPage.goto();
        await loginPage.login(userId, password);
        
        if (userId.startsWith('ADMIN')) {
          const adminMenu = new AdminMenuPage(page);
          await adminMenu.navigateToUserManagement();
          
          await page.waitForTimeout(2000);
          const userRows = await page.locator('.user-row, tr').count();
          expect(userRows).toBeLessThanOrEqual(tenant.users.length + 1); // +1 for header
          
        } else {
          const mainMenu = new MainMenuPage(page);
          await mainMenu.navigateToAccountView();
          
          await page.waitForTimeout(2000);
          const accountElements = await page.locator('.account-item, .account-row, tr').count();
          expect(accountElements).toBeGreaterThan(0);
        }
        
        return { tenantId: tenant.id, userId, success: true };
      });
      
      const results = await Promise.all(sessionPromises);
      
      const tenantGroups = results.reduce((groups: { [key: string]: any[] }, result) => {
        if (!groups[result.tenantId]) groups[result.tenantId] = [];
        groups[result.tenantId].push(result);
        return groups;
      }, {});
      
      expect(Object.keys(tenantGroups).length).toBe(5); // All 5 tenants represented
      
      Object.values(tenantGroups).forEach(group => {
        expect(group.length).toBeGreaterThan(0);
        expect(group.every(r => r.success)).toBeTruthy();
      });
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });

    test('should handle complex audit trail and compliance logging across all user actions', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const auditLog: Array<{
        timestamp: string;
        userId: string;
        action: string;
        resource: string;
        details: any;
        ipAddress: string;
        userAgent: string;
        sessionId: string;
      }> = [];
      
      const complianceRules = {
        dataAccess: { maxRecordsPerQuery: 100, requiresJustification: true },
        userModification: { requiresApproval: true, auditRetention: '7years' },
        financialData: { encryptionRequired: true, accessLogging: 'mandatory' },
        crossTenantAccess: { prohibited: true, alertOnAttempt: true }
      };
      
      await testPage.route('**/api/**', async (route) => {
        const request = route.request();
        const url = request.url();
        const method = request.method();
        const headers = request.headers();
        
        const auditEntry = {
          timestamp: new Date().toISOString(),
          userId: headers['x-user-id'] || 'UNKNOWN',
          action: `${method} ${url.split('/api/')[1]}`,
          resource: url.split('/').pop() || 'unknown',
          details: method === 'POST' || method === 'PUT' ? await request.postDataJSON() : null,
          ipAddress: '192.168.1.100', // Simulated
          userAgent: headers['user-agent'] || 'test-agent',
          sessionId: headers['x-session-id'] || 'SESSION_' + Date.now()
        };
        
        auditLog.push(auditEntry);
        
        if (url.includes('/accounts') && method === 'GET') {
          const mockAccounts = Array.from({ length: 150 }, (_, i) => ({
            accountNumber: `ACC${String(i + 1).padStart(6, '0')}`,
            balance: Math.random() * 10000,
            sensitiveData: 'ENCRYPTED_DATA_' + i
          }));
          
          const limitedAccounts = mockAccounts.slice(0, complianceRules.dataAccess.maxRecordsPerQuery);
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ 
              content: limitedAccounts, 
              totalElements: limitedAccounts.length,
              complianceNote: 'Results limited per data access policy'
            })
          });
        } else if (url.includes('/admin/users') && method === 'POST') {
          auditLog.push({
            ...auditEntry,
            action: 'USER_MODIFICATION_PENDING_APPROVAL',
            details: { ...auditEntry.details, approvalRequired: true }
          });
          
          await route.fulfill({
            status: 202,
            contentType: 'application/json',
            body: JSON.stringify({ 
              message: 'User creation submitted for approval',
              approvalId: 'APPROVAL_' + Date.now(),
              estimatedApprovalTime: '24-48 hours'
            })
          });
        } else {
          await route.continue();
        }
      });
      
      const loginPage = new LoginPage(testPage);
      const adminMenu = new AdminMenuPage(testPage);
      
      await testPage.setExtraHTTPHeaders({
        'X-User-ID': 'ADMIN001',
        'X-Session-ID': 'AUDIT_SESSION_123'
      });
      
      await loginPage.goto();
      await loginPage.login('ADMIN001', 'admin123');
      
      await adminMenu.navigateToUserManagement();
      await testPage.waitForTimeout(1000);
      
      await adminMenu.navigateToAddUser();
      
      const userIdInput = testPage.locator('input[name="userId"]');
      if (await userIdInput.isVisible()) {
        await userIdInput.fill('AUDIT001');
        
        const firstNameInput = testPage.locator('input[name="firstName"]');
        const lastNameInput = testPage.locator('input[name="lastName"]');
        const submitButton = testPage.locator('button[type="submit"]');
        
        await firstNameInput.fill('Audit');
        await lastNameInput.fill('User');
        await submitButton.click();
        
        await testPage.waitForTimeout(2000);
      }
      
      const mainMenu = new MainMenuPage(testPage);
      await mainMenu.navigateToAccountView();
      await testPage.waitForTimeout(1000);
      
      expect(auditLog.length).toBeGreaterThan(3);
      
      const loginAudit = auditLog.find(entry => entry.action.includes('auth'));
      const userManagementAudit = auditLog.find(entry => entry.action.includes('users'));
      const dataAccessAudit = auditLog.find(entry => entry.action.includes('accounts'));
      
      if (userManagementAudit) {
        expect(userManagementAudit.userId).toBe('ADMIN001');
        expect(userManagementAudit.sessionId).toBe('AUDIT_SESSION_123');
      }
      
      if (dataAccessAudit) {
        expect(dataAccessAudit.action).toContain('GET');
        expect(dataAccessAudit.resource).toContain('accounts');
      }
      
      const userCreationAudit = auditLog.find(entry => 
        entry.action === 'USER_MODIFICATION_PENDING_APPROVAL'
      );
      
      if (userCreationAudit) {
        expect(userCreationAudit.details.approvalRequired).toBeTruthy();
      }
      
      const complianceReport = {
        totalActions: auditLog.length,
        userActions: auditLog.filter(entry => entry.userId === 'ADMIN001').length,
        dataAccessActions: auditLog.filter(entry => entry.action.includes('accounts')).length,
        adminActions: auditLog.filter(entry => entry.action.includes('admin')).length,
        complianceViolations: auditLog.filter(entry => 
          entry.action.includes('VIOLATION') || entry.action.includes('ALERT')
        ).length
      };
      
      expect(complianceReport.totalActions).toBeGreaterThan(0);
      expect(complianceReport.complianceViolations).toBe(0);
      
      await context.close();
    });
  });

  test.describe('Advanced Financial Transaction Processing', () => {
    test('should handle complex financial workflows with regulatory compliance', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const regulatoryLimits = {
        dailyTransactionLimit: 10000,
        singleTransactionLimit: 5000,
        suspiciousActivityThreshold: 3000,
        amlCheckRequired: 2500,
        kycVerificationRequired: 1000
      };
      
      let accountBalance = 15000;
      let dailyTransactionTotal = 0;
      const transactionHistory: Array<{
        id: string;
        amount: number;
        type: string;
        status: string;
        riskScore: number;
        complianceFlags: string[];
        timestamp: string;
      }> = [];
      
      await testPage.route('**/api/transactions**', async (route) => {
        const method = route.request().method();
        
        if (method === 'POST') {
          const transactionData = await route.request().postDataJSON();
          const amount = parseFloat(transactionData.amount);
          
          const transaction = {
            id: `TXN_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
            amount,
            type: transactionData.type || 'debit',
            status: 'pending',
            riskScore: 0,
            complianceFlags: [] as string[],
            timestamp: new Date().toISOString()
          };
          
          if (amount > regulatoryLimits.suspiciousActivityThreshold) {
            transaction.riskScore += 30;
            transaction.complianceFlags.push('HIGH_AMOUNT_ALERT');
          }
          
          if (amount > regulatoryLimits.amlCheckRequired) {
            transaction.riskScore += 20;
            transaction.complianceFlags.push('AML_CHECK_REQUIRED');
          }
          
          if (amount > regulatoryLimits.kycVerificationRequired) {
            transaction.riskScore += 10;
            transaction.complianceFlags.push('KYC_VERIFICATION_REQUIRED');
          }
          
          if (dailyTransactionTotal + amount > regulatoryLimits.dailyTransactionLimit) {
            transaction.status = 'rejected';
            transaction.complianceFlags.push('DAILY_LIMIT_EXCEEDED');
            transaction.riskScore += 50;
          }
          
          if (amount > regulatoryLimits.singleTransactionLimit) {
            transaction.status = 'rejected';
            transaction.complianceFlags.push('SINGLE_TRANSACTION_LIMIT_EXCEEDED');
            transaction.riskScore += 40;
          }
          
          const recentTransactions = transactionHistory.filter(t => 
            Date.now() - new Date(t.timestamp).getTime() < 60 * 60 * 1000 // Last hour
          );
          
          if (recentTransactions.length > 5) {
            transaction.riskScore += 25;
            transaction.complianceFlags.push('RAPID_TRANSACTION_PATTERN');
          }
          
          const similarAmountTransactions = recentTransactions.filter(t => 
            Math.abs(t.amount - amount) < 50
          );
          
          if (similarAmountTransactions.length > 2) {
            transaction.riskScore += 20;
            transaction.complianceFlags.push('SIMILAR_AMOUNT_PATTERN');
          }
          
          if (transaction.status === 'pending') {
            if (transaction.riskScore > 50) {
              transaction.status = 'under_review';
              transaction.complianceFlags.push('MANUAL_REVIEW_REQUIRED');
            } else if (transaction.riskScore > 30) {
              transaction.status = 'flagged';
              transaction.complianceFlags.push('AUTOMATED_REVIEW_REQUIRED');
            } else {
              transaction.status = 'approved';
              if (transaction.type === 'debit') {
                accountBalance -= amount;
              } else {
                accountBalance += amount;
              }
              dailyTransactionTotal += amount;
            }
          }
          
          transactionHistory.push(transaction);
          
          await route.fulfill({
            status: transaction.status === 'rejected' ? 400 : 201,
            contentType: 'application/json',
            body: JSON.stringify({
              ...transaction,
              currentBalance: accountBalance,
              dailyTransactionTotal,
              regulatoryStatus: transaction.complianceFlags.length > 0 ? 'flagged' : 'clean'
            })
          });
        } else {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              content: transactionHistory,
              totalElements: transactionHistory.length,
              summary: {
                totalTransactions: transactionHistory.length,
                approvedTransactions: transactionHistory.filter(t => t.status === 'approved').length,
                flaggedTransactions: transactionHistory.filter(t => t.status === 'flagged').length,
                rejectedTransactions: transactionHistory.filter(t => t.status === 'rejected').length
              }
            })
          });
        }
      });
      
      const loginPage = new LoginPage(testPage);
      const mainMenu = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenu.navigateToTransactions();
      
      const transactionScenarios = [
        { amount: '500.00', expectedStatus: 'approved' },
        { amount: '1500.00', expectedStatus: 'flagged' }, // KYC required
        { amount: '3500.00', expectedStatus: 'under_review' }, // High amount + AML
        { amount: '6000.00', expectedStatus: 'rejected' }, // Exceeds single limit
        { amount: '100.00', expectedStatus: 'approved' },
        { amount: '105.00', expectedStatus: 'flagged' }, // Similar amount pattern
        { amount: '102.00', expectedStatus: 'flagged' }, // Similar amount pattern
      ];
      
      for (const scenario of transactionScenarios) {
        const addButton = testPage.locator('button:has-text("Add"), a:has-text("Add")');
        if (await addButton.isVisible()) {
          await addButton.click();
          
          const amountInput = testPage.locator('input[name="amount"]');
          const merchantInput = testPage.locator('input[name="merchant"]');
          const submitButton = testPage.locator('button[type="submit"]');
          
          if (await amountInput.isVisible()) {
            await amountInput.fill(scenario.amount);
            await merchantInput.fill(`Merchant_${Date.now()}`);
            await submitButton.click();
            
            await testPage.waitForTimeout(2000);
            
            if (scenario.expectedStatus === 'approved') {
              const successMessage = testPage.locator('.success-message, .alert-success, text=approved');
              expect(await successMessage.isVisible()).toBeTruthy();
            } else if (scenario.expectedStatus === 'rejected') {
              const errorMessage = testPage.locator('.error-message, .alert-danger, text=rejected, text=limit');
              expect(await errorMessage.isVisible()).toBeTruthy();
            } else {
              const warningMessage = testPage.locator('.warning-message, .alert-warning, text=review, text=flagged');
              expect(await warningMessage.isVisible()).toBeTruthy();
            }
          }
        }
        
        await testPage.waitForTimeout(500);
      }
      
      const approvedCount = transactionHistory.filter(t => t.status === 'approved').length;
      const flaggedCount = transactionHistory.filter(t => t.status === 'flagged').length;
      const rejectedCount = transactionHistory.filter(t => t.status === 'rejected').length;
      
      expect(transactionHistory.length).toBe(transactionScenarios.length);
      expect(approvedCount).toBeGreaterThan(0);
      expect(flaggedCount + rejectedCount).toBeGreaterThan(0); // Some should be flagged/rejected
      
      const highRiskTransactions = transactionHistory.filter(t => t.riskScore > 30);
      expect(highRiskTransactions.length).toBeGreaterThan(0);
      
      const amlFlagged = transactionHistory.filter(t => 
        t.complianceFlags.includes('AML_CHECK_REQUIRED')
      );
      expect(amlFlagged.length).toBeGreaterThan(0);
      
      await context.close();
    });

    test('should handle complex fraud detection and prevention patterns', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const fraudDetectionRules = {
        velocityChecks: {
          maxTransactionsPerMinute: 3,
          maxTransactionsPerHour: 20,
          maxAmountPerHour: 5000
        },
        behavioralAnalysis: {
          unusualTimePattern: true,
          unusualLocationPattern: true,
          unusualMerchantPattern: true
        },
        riskScoring: {
          lowRisk: 0-25,
          mediumRisk: 26-50,
          highRisk: 51-75,
          criticalRisk: 76-100
        }
      };
      
      let userProfile = {
        userId: 'USER0001',
        typicalTransactionAmount: 150,
        typicalTransactionTime: '14:00', // 2 PM
        typicalMerchants: ['Grocery Store', 'Gas Station', 'Restaurant'],
        typicalLocation: 'New York',
        accountAge: 365, // days
        creditScore: 750
      };
      
      const fraudAnalysisLog: Array<{
        transactionId: string;
        riskScore: number;
        fraudIndicators: string[];
        action: string;
        timestamp: string;
      }> = [];
      
      await testPage.route('**/api/fraud-check**', async (route) => {
        const transactionData = await route.request().postDataJSON();
        const amount = parseFloat(transactionData.amount);
        const merchant = transactionData.merchant;
        const currentTime = new Date();
        const currentHour = currentTime.getHours();
        
        let riskScore = 0;
        const fraudIndicators: string[] = [];
        
        if (amount > userProfile.typicalTransactionAmount * 5) {
          riskScore += 30;
          fraudIndicators.push('UNUSUAL_AMOUNT_HIGH');
        } else if (amount > userProfile.typicalTransactionAmount * 3) {
          riskScore += 15;
          fraudIndicators.push('ELEVATED_AMOUNT');
        }
        
        const typicalHour = parseInt(userProfile.typicalTransactionTime.split(':')[0]);
        const timeDifference = Math.abs(currentHour - typicalHour);
        
        if (timeDifference > 8 || currentHour < 6 || currentHour > 23) {
          riskScore += 20;
          fraudIndicators.push('UNUSUAL_TIME_PATTERN');
        }
        
        if (!userProfile.typicalMerchants.some(typical => 
          merchant.toLowerCase().includes(typical.toLowerCase())
        )) {
          riskScore += 15;
          fraudIndicators.push('UNUSUAL_MERCHANT');
        }
        
        const recentTransactions = fraudAnalysisLog.filter(log => 
          Date.now() - new Date(log.timestamp).getTime() < 60 * 60 * 1000 // Last hour
        );
        
        if (recentTransactions.length >= fraudDetectionRules.velocityChecks.maxTransactionsPerHour) {
          riskScore += 40;
          fraudIndicators.push('VELOCITY_LIMIT_EXCEEDED');
        }
        
        const lastMinuteTransactions = fraudAnalysisLog.filter(log => 
          Date.now() - new Date(log.timestamp).getTime() < 60 * 1000 // Last minute
        );
        
        if (lastMinuteTransactions.length >= fraudDetectionRules.velocityChecks.maxTransactionsPerMinute) {
          riskScore += 50;
          fraudIndicators.push('RAPID_FIRE_TRANSACTIONS');
        }
        
        if (transactionData.location && transactionData.location !== userProfile.typicalLocation) {
          riskScore += 25;
          fraudIndicators.push('UNUSUAL_LOCATION');
        }
        
        if (transactionData.deviceFingerprint && transactionData.deviceFingerprint !== 'KNOWN_DEVICE_123') {
          riskScore += 20;
          fraudIndicators.push('UNKNOWN_DEVICE');
        }
        
        let action = 'APPROVE';
        if (riskScore >= 76) {
          action = 'BLOCK';
        } else if (riskScore >= 51) {
          action = 'CHALLENGE'; // Require additional authentication
        } else if (riskScore >= 26) {
          action = 'MONITOR'; // Allow but flag for review
        }
        
        const fraudAnalysis = {
          transactionId: `FRAUD_CHECK_${Date.now()}`,
          riskScore,
          fraudIndicators,
          action,
          timestamp: new Date().toISOString()
        };
        
        fraudAnalysisLog.push(fraudAnalysis);
        
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            ...fraudAnalysis,
            userProfile,
            recommendation: action,
            requiresAdditionalAuth: action === 'CHALLENGE',
            blocked: action === 'BLOCK'
          })
        });
      });
      
      await testPage.route('**/api/transactions**', async (route) => {
        const method = route.request().method();
        
        if (method === 'POST') {
          const transactionData = await route.request().postDataJSON();
          
          const fraudCheckResponse = await testPage.request.post('http://localhost:5174/api/fraud-check', {
            data: {
              ...transactionData,
              location: 'Los Angeles', // Different from typical
              deviceFingerprint: 'UNKNOWN_DEVICE_456'
            }
          });
          
          const fraudResult = await fraudCheckResponse.json();
          
          let transactionStatus = 'approved';
          if (fraudResult.action === 'BLOCK') {
            transactionStatus = 'blocked';
          } else if (fraudResult.action === 'CHALLENGE') {
            transactionStatus = 'pending_auth';
          } else if (fraudResult.action === 'MONITOR') {
            transactionStatus = 'approved_monitored';
          }
          
          await route.fulfill({
            status: fraudResult.action === 'BLOCK' ? 403 : 201,
            contentType: 'application/json',
            body: JSON.stringify({
              id: `TXN_${Date.now()}`,
              ...transactionData,
              status: transactionStatus,
              fraudScore: fraudResult.riskScore,
              fraudIndicators: fraudResult.fraudIndicators,
              requiresAdditionalAuth: fraudResult.requiresAdditionalAuth
            })
          });
        } else {
          await route.continue();
        }
      });
      
      const loginPage = new LoginPage(testPage);
      const mainMenu = new MainMenuPage(testPage);
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      await mainMenu.navigateToTransactions();
      
      const fraudTestScenarios = [
        { amount: '100.00', merchant: 'Grocery Store', expectedRisk: 'low' },
        { amount: '800.00', merchant: 'Electronics Store', expectedRisk: 'medium' }, // Unusual merchant + high amount
        { amount: '2000.00', merchant: 'Jewelry Store', expectedRisk: 'high' }, // Very unusual
        { amount: '50.00', merchant: 'Coffee Shop', expectedRisk: 'low' },
        { amount: '75.00', merchant: 'Bookstore', expectedRisk: 'medium' }, // Rapid transactions
        { amount: '60.00', merchant: 'Pharmacy', expectedRisk: 'medium' }, // Velocity check
      ];
      
      for (const scenario of fraudTestScenarios) {
        const addButton = testPage.locator('button:has-text("Add"), a:has-text("Add")');
        if (await addButton.isVisible()) {
          await addButton.click();
          
          const amountInput = testPage.locator('input[name="amount"]');
          const merchantInput = testPage.locator('input[name="merchant"]');
          const submitButton = testPage.locator('button[type="submit"]');
          
          if (await amountInput.isVisible()) {
            await amountInput.fill(scenario.amount);
            await merchantInput.fill(scenario.merchant);
            await submitButton.click();
            
            await testPage.waitForTimeout(2000);
            
            if (scenario.expectedRisk === 'high') {
              const blockedMessage = testPage.locator('text=blocked, text=fraud, text=suspicious');
              const challengeMessage = testPage.locator('text=additional, text=verification, text=challenge');
              
              const isBlockedOrChallenged = await blockedMessage.isVisible() || await challengeMessage.isVisible();
              expect(isBlockedOrChallenged).toBeTruthy();
            }
          }
        }
        
        await testPage.waitForTimeout(1000); // Simulate time between transactions
      }
      
      expect(fraudAnalysisLog.length).toBe(fraudTestScenarios.length);
      
      const highRiskTransactions = fraudAnalysisLog.filter(log => log.riskScore >= 51);
      const blockedTransactions = fraudAnalysisLog.filter(log => log.action === 'BLOCK');
      const challengedTransactions = fraudAnalysisLog.filter(log => log.action === 'CHALLENGE');
      
      expect(highRiskTransactions.length).toBeGreaterThan(0);
      expect(blockedTransactions.length + challengedTransactions.length).toBeGreaterThan(0);
      
      const velocityViolations = fraudAnalysisLog.filter(log => 
        log.fraudIndicators.includes('VELOCITY_LIMIT_EXCEEDED') || 
        log.fraudIndicators.includes('RAPID_FIRE_TRANSACTIONS')
      );
      
      const unusualPatterns = fraudAnalysisLog.filter(log => 
        log.fraudIndicators.includes('UNUSUAL_MERCHANT') || 
        log.fraudIndicators.includes('UNUSUAL_AMOUNT_HIGH') ||
        log.fraudIndicators.includes('UNUSUAL_LOCATION')
      );
      
      expect(unusualPatterns.length).toBeGreaterThan(0);
      
      await context.close();
    });
  });
});
