import { test as base, expect } from '@playwright/test';

export const test = base.extend({
  page: async ({ page }, use) => {
    await page.route('**/api/auth/login', async (route, request) => {
      if (request.method() === 'POST') {
        const postData = request.postDataJSON();
        
        if (postData?.userId === 'ADMIN001' && postData?.password === 'admin123') {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              userId: 'ADMIN001',
              userType: 'A',
              success: true,
              message: 'Login successful',
              firstName: 'John',
              lastName: 'Smith'
            })
          });
          return;
        }
        
        if (postData?.userId === 'USER0001' && postData?.password === 'user1234') {
          await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
              userId: 'USER0001',
              userType: 'U',
              success: true,
              message: 'Login successful',
              firstName: 'Jane',
              lastName: 'Doe'
            })
          });
          return;
        }
        
        await route.fulfill({
          status: 401,
          contentType: 'application/json',
          body: JSON.stringify({
            success: false,
            message: 'Invalid credentials'
          })
        });
      } else {
        await route.continue();
      }
    });

    await page.route('**/api/accounts/**', async (route, request) => {
      if (request.method() === 'GET') {
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({
            acctId: 12345678901,
            acctActiveStatus: 'Y',
            acctCurrBal: 1500.00,
            acctCreditLimit: 5000.00,
            acctCashCreditLimit: 1000.00,
            acctOpenDate: '2020-01-15',
            acctExpiraionDate: '2027-01-15',
            acctReissueDate: '2023-01-15',
            acctCurrCycCredit: 2500.00,
            acctCurrCycDebit: 1000.00,
            acctAddrZip: '10001',
            acctGroupId: 'GRP001',
            customerFirstName: 'Alice',
            customerLastName: 'Johnson',
            customerSsn: '123456789',
            customerDateOfBirth: '1985-03-15',
            customerFicoScore: 750,
            customerPhone1: '555-0101',
            customerPhone2: '555-0102',
            customerAddress1: '123 Main St',
            customerAddress2: 'Apt 4B',
            customerCity: 'New York',
            customerState: 'NY',
            customerZipCode: '10001',
            customerCountry: 'USA',
            customerGovtIssuedId: null,
            customerEftAccountId: null,
            customerPriCardHolderInd: null
          })
        });
      } else {
        await route.continue();
      }
    });

    await use(page);
  },
});

export { expect };
