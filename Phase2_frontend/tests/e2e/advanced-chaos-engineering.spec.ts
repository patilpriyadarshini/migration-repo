import { test, expect } from '@playwright/test';
import { LoginPage } from './page-objects/LoginPage';
import { MainMenuPage } from './page-objects/MainMenuPage';
import { AdminMenuPage } from './page-objects/AdminMenuPage';

test.describe('Advanced Chaos Engineering and Resilience Testing', () => {
  test.describe('Network Partition and Service Degradation Simulation', () => {
    test('should handle complex network partitions with graceful degradation', async ({ page, browser }) => {
      const contexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));
      const mainMenuPages = pages.map(page => new MainMenuPage(page));
      
      const networkPartitions = [
        { service: 'account-service', partition: true, duration: 5000 },
        { service: 'transaction-service', partition: false, duration: 0 },
        { service: 'card-service', partition: true, duration: 3000 },
        { service: 'notification-service', partition: true, duration: 7000 }
      ];
      
      let partitionStartTime = Date.now();
      
      for (let i = 0; i < pages.length; i++) {
        await pages[i].route('**/api/**', async (route) => {
          const url = route.request().url();
          const currentTime = Date.now();
          
          const serviceName = url.includes('/accounts') ? 'account-service' :
                            url.includes('/transactions') ? 'transaction-service' :
                            url.includes('/cards') ? 'card-service' :
                            url.includes('/notifications') ? 'notification-service' : 'unknown';
          
          const partition = networkPartitions.find(p => p.service === serviceName);
          
          if (partition && partition.partition && 
              (currentTime - partitionStartTime) < partition.duration) {
            
            const failureType = Math.random();
            
            if (failureType < 0.3) {
              await new Promise(resolve => setTimeout(resolve, 30000));
              await route.abort('timedout');
            } else if (failureType < 0.6) {
              await route.abort('connectionrefused');
            } else {
              await route.fulfill({
                status: 503,
                contentType: 'application/json',
                body: JSON.stringify({
                  error: 'Service temporarily unavailable due to network partition',
                  retryAfter: 5000,
                  partitionActive: true
                })
              });
            }
            return;
          }
          
          if (partition && partition.partition && 
              (currentTime - partitionStartTime) >= partition.duration) {
            partition.partition = false; // Heal the partition
          }
          
          const mockResponses = {
            'account-service': { content: [{ accountNumber: '12345678901', balance: 1500.00, status: 'ACTIVE' }] },
            'transaction-service': { content: [{ id: 'TXN001', amount: 100.00, date: new Date().toISOString(), status: 'COMPLETED' }] },
            'card-service': { content: [{ cardNumber: '****1234', status: 'ACTIVE', expiryDate: '12/25' }] },
            'notification-service': { content: [{ id: 'NOTIF001', message: 'Transaction completed', status: 'SENT' }] }
          };
          
          const responseData = mockResponses[serviceName as keyof typeof mockResponses] || { content: [] };
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            headers: {
              'X-Service-Name': serviceName,
              'X-Partition-Healed': (!partition?.partition).toString(),
              'X-Recovery-Time': (currentTime - partitionStartTime).toString()
            },
            body: JSON.stringify(responseData)
          });
        });
      }
      
      await Promise.all([
        loginPages[0].goto().then(() => loginPages[0].login('ADMIN001', 'admin123')),
        loginPages[1].goto().then(() => loginPages[1].login('USER0001', 'user1234')),
        loginPages[2].goto().then(() => loginPages[2].login('USER0001', 'user1234'))
      ]);
      
      const operationResults = await Promise.allSettled([
        mainMenuPages[0].navigateToAccountView().catch(e => ({ error: e.message })),
        mainMenuPages[1].navigateToTransactions().catch(e => ({ error: e.message })),
        mainMenuPages[2].navigateToCards().catch(e => ({ error: e.message }))
      ]);
      
      let successfulOperations = 0;
      let gracefulFailures = 0;
      
      for (const result of operationResults) {
        if (result.status === 'fulfilled') {
          successfulOperations++;
        } else if (result.status === 'rejected') {
          gracefulFailures++;
        }
      }
      
      expect(successfulOperations + gracefulFailures).toBe(operationResults.length);
      
      await pages[0].waitForTimeout(8000);
      
      const recoveryResults = await Promise.allSettled([
        mainMenuPages[0].navigateToAccountView(),
        mainMenuPages[1].navigateToTransactions(),
        mainMenuPages[2].navigateToCards()
      ]);
      
      const recoveredOperations = recoveryResults.filter(r => r.status === 'fulfilled').length;
      expect(recoveredOperations).toBeGreaterThan(successfulOperations);
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });

    test('should handle cascading failure scenarios with circuit breaker patterns', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      const circuitBreakers = new Map([
        ['account-service', { state: 'CLOSED', failures: 0, lastFailure: 0, timeout: 5000 }],
        ['transaction-service', { state: 'CLOSED', failures: 0, lastFailure: 0, timeout: 5000 }],
        ['card-service', { state: 'CLOSED', failures: 0, lastFailure: 0, timeout: 5000 }],
        ['billing-service', { state: 'CLOSED', failures: 0, lastFailure: 0, timeout: 5000 }]
      ]);
      
      const maxFailures = 3;
      const circuitOpenDuration = 10000;
      
      await testPage.route('**/api/**', async (route) => {
        const url = route.request().url();
        const serviceName = url.includes('/accounts') ? 'account-service' :
                          url.includes('/transactions') ? 'transaction-service' :
                          url.includes('/cards') ? 'card-service' :
                          url.includes('/billing') ? 'billing-service' : 'unknown';
        
        const circuitBreaker = circuitBreakers.get(serviceName);
        if (!circuitBreaker) {
          await route.continue();
          return;
        }
        
        const currentTime = Date.now();
        
        if (circuitBreaker.state === 'OPEN') {
          if (currentTime - circuitBreaker.lastFailure > circuitOpenDuration) {
            circuitBreaker.state = 'HALF_OPEN';
            circuitBreaker.failures = 0;
          } else {
            await route.fulfill({
              status: 503,
              contentType: 'application/json',
              body: JSON.stringify({
                error: 'Circuit breaker is OPEN',
                serviceName: serviceName,
                state: 'OPEN',
                retryAfter: circuitOpenDuration - (currentTime - circuitBreaker.lastFailure)
              })
            });
            return;
          }
        }
        
        const shouldFail = Math.random() < 0.4; // 40% failure rate
        
        if (shouldFail) {
          circuitBreaker.failures++;
          circuitBreaker.lastFailure = currentTime;
          
          if (circuitBreaker.failures >= maxFailures) {
            circuitBreaker.state = 'OPEN';
          }
          
          if (serviceName === 'account-service') {
            const transactionCB = circuitBreakers.get('transaction-service');
            if (transactionCB) {
              transactionCB.failures++;
              if (transactionCB.failures >= maxFailures) {
                transactionCB.state = 'OPEN';
                transactionCB.lastFailure = currentTime;
              }
            }
          }
          
          await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({
              error: 'Internal server error',
              serviceName: serviceName,
              circuitBreakerState: circuitBreaker.state,
              failures: circuitBreaker.failures
            })
          });
        } else {
          if (circuitBreaker.state === 'HALF_OPEN') {
            circuitBreaker.state = 'CLOSED';
            circuitBreaker.failures = 0;
          }
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            headers: {
              'X-Circuit-Breaker-State': circuitBreaker.state,
              'X-Service-Name': serviceName,
              'X-Failure-Count': circuitBreaker.failures.toString()
            },
            body: JSON.stringify({
              content: [{ id: 1, data: `${serviceName}-data`, status: 'SUCCESS' }],
              circuitBreakerState: circuitBreaker.state
            })
          });
        }
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      const testOperations = [
        () => mainMenuPage.navigateToAccountView(),
        () => mainMenuPage.navigateToTransactions(),
        () => mainMenuPage.navigateToCards()
      ];
      
      let totalAttempts = 0;
      let successfulAttempts = 0;
      let circuitBreakerTriggered = false;
      
      for (let round = 0; round < 5; round++) {
        for (const operation of testOperations) {
          totalAttempts++;
          try {
            await operation();
            successfulAttempts++;
          } catch (error) {
            const cbStates = Array.from(circuitBreakers.values());
            if (cbStates.some(cb => cb.state === 'OPEN')) {
              circuitBreakerTriggered = true;
            }
          }
          await testPage.waitForTimeout(1000);
        }
      }
      
      expect(totalAttempts).toBeGreaterThan(0);
      expect(circuitBreakerTriggered).toBeTruthy();
      
      await testPage.waitForTimeout(12000);
      
      let recoveryAttempts = 0;
      let recoverySuccesses = 0;
      
      for (const operation of testOperations) {
        recoveryAttempts++;
        try {
          await operation();
          recoverySuccesses++;
        } catch (error) {
        }
        await testPage.waitForTimeout(1000);
      }
      
      expect(recoverySuccesses).toBeGreaterThan(0);
      
      await context.close();
    });
  });

  test.describe('Resource Exhaustion and Memory Pressure Testing', () => {
    test('should handle memory pressure and resource exhaustion gracefully', async ({ page, browser }) => {
      const context = await browser.newContext();
      const testPage = await context.newPage();
      
      const loginPage = new LoginPage(testPage);
      const mainMenuPage = new MainMenuPage(testPage);
      
      let memoryPressure = 0;
      let cpuLoad = 0;
      let activeConnections = 0;
      const maxConnections = 100;
      const memoryThreshold = 0.8;
      const cpuThreshold = 0.9;
      
      await testPage.route('**/api/**', async (route) => {
        activeConnections++;
        memoryPressure = Math.min(1.0, memoryPressure + Math.random() * 0.1);
        cpuLoad = Math.min(1.0, cpuLoad + Math.random() * 0.05);
        
        if (activeConnections > maxConnections) {
          await route.fulfill({
            status: 503,
            contentType: 'application/json',
            body: JSON.stringify({
              error: 'Too many connections',
              activeConnections: activeConnections,
              maxConnections: maxConnections,
              retryAfter: 5000
            })
          });
          activeConnections--;
          return;
        }
        
        if (memoryPressure > memoryThreshold) {
          await route.fulfill({
            status: 507,
            contentType: 'application/json',
            body: JSON.stringify({
              error: 'Insufficient storage',
              memoryPressure: memoryPressure,
              threshold: memoryThreshold,
              recommendation: 'Reduce request size or retry later'
            })
          });
          activeConnections--;
          memoryPressure = Math.max(0, memoryPressure - 0.1);
          return;
        }
        
        if (cpuLoad > cpuThreshold) {
          await new Promise(resolve => setTimeout(resolve, 2000 + Math.random() * 3000));
          
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            headers: {
              'X-CPU-Load': cpuLoad.toString(),
              'X-Response-Delayed': 'true',
              'X-Processing-Time': '5000ms'
            },
            body: JSON.stringify({
              content: [{ id: 1, data: 'delayed-response' }],
              processingTime: 5000,
              cpuLoad: cpuLoad
            })
          });
          activeConnections--;
          cpuLoad = Math.max(0, cpuLoad - 0.1);
          return;
        }
        
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          headers: {
            'X-Memory-Pressure': memoryPressure.toString(),
            'X-CPU-Load': cpuLoad.toString(),
            'X-Active-Connections': activeConnections.toString()
          },
          body: JSON.stringify({
            content: [{ id: 1, data: 'normal-response' }],
            resourceMetrics: {
              memoryPressure: memoryPressure,
              cpuLoad: cpuLoad,
              activeConnections: activeConnections
            }
          })
        });
        
        activeConnections--;
        memoryPressure = Math.max(0, memoryPressure - 0.02);
        cpuLoad = Math.max(0, cpuLoad - 0.01);
      });
      
      await loginPage.goto();
      await loginPage.login('USER0001', 'user1234');
      
      const concurrentRequests = Array.from({ length: 50 }, async (_, i) => {
        try {
          if (i % 3 === 0) {
            await mainMenuPage.navigateToAccountView();
          } else if (i % 3 === 1) {
            await mainMenuPage.navigateToTransactions();
          } else {
            await mainMenuPage.navigateToCards();
          }
          return { success: true, index: i };
        } catch (error) {
          return { success: false, index: i, error: error.message };
        }
      });
      
      const results = await Promise.allSettled(concurrentRequests);
      
      let successCount = 0;
      let resourceExhaustionCount = 0;
      let timeoutCount = 0;
      
      for (const result of results) {
        if (result.status === 'fulfilled') {
          if (result.value.success) {
            successCount++;
          } else {
            const errorMsg = result.value.error?.toLowerCase() || '';
            if (errorMsg.includes('connection') || errorMsg.includes('storage')) {
              resourceExhaustionCount++;
            } else if (errorMsg.includes('timeout')) {
              timeoutCount++;
            }
          }
        }
      }
      
      expect(successCount + resourceExhaustionCount + timeoutCount).toBe(results.length);
      expect(resourceExhaustionCount).toBeGreaterThan(0); // Should have triggered resource limits
      
      await testPage.waitForTimeout(10000);
      
      const recoveryOperations = [
        mainMenuPage.navigateToAccountView(),
        mainMenuPage.navigateToTransactions(),
        mainMenuPage.navigateToCards()
      ];
      
      const recoveryResults = await Promise.allSettled(recoveryOperations);
      const recoverySuccesses = recoveryResults.filter(r => r.status === 'fulfilled').length;
      
      expect(recoverySuccesses).toBeGreaterThan(0);
      
      await context.close();
    });
  });

  test.describe('Byzantine Fault Tolerance and Consensus Testing', () => {
    test('should handle Byzantine failures in distributed consensus scenarios', async ({ page, browser }) => {
      const contexts = await Promise.all([
        browser.newContext(),
        browser.newContext(),
        browser.newContext(),
        browser.newContext(),
        browser.newContext()
      ]);
      
      const pages = await Promise.all(contexts.map(ctx => ctx.newPage()));
      const loginPages = pages.map(page => new LoginPage(page));
      
      const nodes: Array<{
        id: string;
        status: 'honest' | 'byzantine';
        vote: {
          nodeId: string;
          proposal: string;
          accept: boolean;
          term: number;
        } | null;
        term: number;
      }> = [
        { id: 'node-1', status: 'honest', vote: null, term: 1 },
        { id: 'node-2', status: 'honest', vote: null, term: 1 },
        { id: 'node-3', status: 'byzantine', vote: null, term: 1 }, // Byzantine node
        { id: 'node-4', status: 'honest', vote: null, term: 1 },
        { id: 'node-5', status: 'honest', vote: null, term: 1 }
      ];
      
      let consensusRound = 0;
      const consensusLog: Array<{ round: number; proposal: string; votes: any[]; outcome: string }> = [];
      
      for (let i = 0; i < pages.length; i++) {
        await pages[i].route('**/api/consensus/**', async (route) => {
          const url = route.request().url();
          const method = route.request().method();
          const nodeId = `node-${i + 1}`;
          const node = nodes[i];
          
          if (method === 'POST' && url.includes('/propose')) {
            consensusRound++;
            const proposal = await route.request().postDataJSON();
            
            nodes.forEach(n => n.vote = null);
            
            for (const votingNode of nodes) {
              if (votingNode.status === 'honest') {
                votingNode.vote = {
                  nodeId: votingNode.id,
                  proposal: proposal.transactionId,
                  accept: true,
                  term: votingNode.term
                };
              } else if (votingNode.status === 'byzantine') {
                votingNode.vote = {
                  nodeId: votingNode.id,
                  proposal: Math.random() > 0.5 ? proposal.transactionId : 'malicious-proposal',
                  accept: Math.random() > 0.3, // Random acceptance
                  term: Math.floor(Math.random() * 5) + 1 // Wrong term
                };
              }
            }
            
            const votes = nodes.map(n => n.vote).filter(v => v !== null);
            const honestVotes = votes.filter(v => nodes.find(n => n.id === v.nodeId)?.status === 'honest');
            const byzantineVotes = votes.filter(v => nodes.find(n => n.id === v.nodeId)?.status === 'byzantine');
            
            const acceptVotes = honestVotes.filter(v => v.accept).length;
            const consensus = acceptVotes > Math.floor(nodes.filter(n => n.status === 'honest').length / 2);
            
            consensusLog.push({
              round: consensusRound,
              proposal: proposal.transactionId,
              votes: votes,
              outcome: consensus ? 'ACCEPTED' : 'REJECTED'
            });
            
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({
                round: consensusRound,
                nodeId: nodeId,
                proposal: proposal.transactionId,
                votes: votes,
                consensus: consensus,
                honestNodes: nodes.filter(n => n.status === 'honest').length,
                byzantineNodes: nodes.filter(n => n.status === 'byzantine').length,
                outcome: consensus ? 'ACCEPTED' : 'REJECTED'
              })
            });
          } else if (method === 'GET' && url.includes('/status')) {
            await route.fulfill({
              status: 200,
              contentType: 'application/json',
              body: JSON.stringify({
                nodeId: nodeId,
                status: node.status,
                term: node.term,
                consensusRounds: consensusRound,
                consensusLog: consensusLog.slice(-5) // Last 5 rounds
              })
            });
          }
        });
      }
      
      await Promise.all([
        loginPages[0].goto().then(() => loginPages[0].login('ADMIN001', 'admin123')),
        loginPages[1].goto().then(() => loginPages[1].login('USER0001', 'user1234')),
        loginPages[2].goto().then(() => loginPages[2].login('USER0001', 'user1234')),
        loginPages[3].goto().then(() => loginPages[3].login('ADMIN001', 'admin123')),
        loginPages[4].goto().then(() => loginPages[4].login('USER0001', 'user1234'))
      ]);
      
      const consensusTests = Array.from({ length: 10 }, (_, i) => ({
        transactionId: `TXN-${Date.now()}-${i}`,
        amount: Math.random() * 1000,
        type: 'TRANSFER'
      }));
      
      let acceptedTransactions = 0;
      let rejectedTransactions = 0;
      
      for (const transaction of consensusTests) {
        const proposerIndex = Math.floor(Math.random() * pages.length);
        const proposerPage = pages[proposerIndex];
        
        const consensusResult = await proposerPage.evaluate((tx) => {
          return fetch('/api/consensus/propose', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(tx)
          }).then(r => r.json());
        }, transaction);
        
        if (consensusResult.consensus) {
          acceptedTransactions++;
        } else {
          rejectedTransactions++;
        }
        
        await pages[0].waitForTimeout(1000);
      }
      
      expect(acceptedTransactions + rejectedTransactions).toBe(consensusTests.length);
      expect(acceptedTransactions).toBeGreaterThan(0); // Should achieve consensus despite Byzantine node
      
      const finalStatus = await pages[0].evaluate(() => {
        return fetch('/api/consensus/status').then(r => r.json());
      });
      
      expect(finalStatus.consensusRounds).toBe(consensusTests.length);
      expect(finalStatus.consensusLog).toBeDefined();
      expect(finalStatus.consensusLog.length).toBeGreaterThan(0);
      
      await Promise.all(contexts.map(ctx => ctx.close()));
    });
  });
});
