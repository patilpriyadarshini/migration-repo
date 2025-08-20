import { http, HttpResponse } from 'msw'

const API_BASE_URL = 'http://localhost:8080/carddemo'

export const handlers = [
  http.post(`${API_BASE_URL}/api/auth/login`, async ({ request }) => {
    const body = await request.json() as { userId: string; password: string }
    
    if (body.userId === 'ADMIN001' && body.password === 'admin123') {
      return HttpResponse.json({
        userId: 'ADMIN001',
        userType: 'A',
        success: true,
        message: 'Login successful',
        firstName: 'John',
        lastName: 'Smith'
      })
    }
    
    if (body.userId === 'USER0001' && body.password === 'user1234') {
      return HttpResponse.json({
        userId: 'USER0001',
        userType: 'U',
        success: true,
        message: 'Login successful',
        firstName: 'Jane',
        lastName: 'Doe'
      })
    }
    
    return HttpResponse.json(
      { 
        success: false,
        message: 'Invalid credentials' 
      },
      { status: 401 }
    )
  }),

  http.get(`${API_BASE_URL}/api/accounts/:accountId`, ({ params }) => {
    const { accountId } = params
    
    if (accountId === '12345678901') {
      return HttpResponse.json({
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
    }
    
    return HttpResponse.json(
      { message: 'Account ID NOT found' },
      { status: 404 }
    )
  }),

  http.get(`${API_BASE_URL}/api/cards`, ({ request }) => {
    const url = new URL(request.url)
    const accountId = url.searchParams.get('accountId')
    
    if (accountId === '12345678901') {
      return HttpResponse.json({
        content: [
          {
            cardNum: '4111111111111111',
            acctId: 12345678901,
            cardName: 'Alice Johnson',
            cardStatus: 'Y',
            expiryMonth: '12',
            expiryYear: '2025'
          }
        ],
        page: 0,
        size: 10,
        totalElements: 1
      })
    }
    
    return HttpResponse.json({
      content: [],
      page: 0,
      size: 10,
      totalElements: 0
    })
  }),

  http.get(`${API_BASE_URL}/api/transactions`, () => {
    return HttpResponse.json({
      content: [
        {
          tranId: 'T001',
          cardNum: '4111111111111111',
          tranTypeCd: '01',
          tranCatCd: '1',
          tranSource: 'POS',
          tranDesc: 'GROCERY STORE PURCHASE',
          tranAmt: 85.50,
          origTs: '2024-01-15T10:30:00',
          procTs: '2024-01-15T10:30:00',
          merchantId: '1001',
          merchantName: 'SuperMart Grocery',
          merchantCity: 'New York',
          merchantZip: '10001'
        }
      ],
      page: 0,
      size: 10,
      totalElements: 1
    })
  })
]
