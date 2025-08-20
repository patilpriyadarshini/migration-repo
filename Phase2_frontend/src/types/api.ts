export interface LoginRequest {
  userId: string;
  password: string;
}

export interface LoginResponse {
  userId: string;
  userType: string;
  success: boolean;
  message: string;
}

export interface AccountResponse {
  acctId: number;
  acctActiveStatus: string;
  acctCurrBal: number;
  acctCreditLimit: number;
  acctCashCreditLimit: number;
  acctOpenDate: string;
  acctExpiraionDate: string;
  acctReissueDate: string;
  acctCurrCycCredit: number;
  acctCurrCycDebit: number;
  acctAddrZip: string;
  acctGroupId: string;
  customerFirstName?: string;
  customerLastName?: string;
  customerSsn?: string;
  customerDateOfBirth?: string;
  customerFicoScore?: number;
  customerPhone1?: string;
  customerPhone2?: string;
  customerAddress1?: string;
  customerAddress2?: string;
  customerCity?: string;
  customerState?: string;
  customerZipCode?: string;
  customerCountry?: string;
  customerGovtIssuedId?: string;
  customerEftAccountId?: string;
  customerPriCardHolderInd?: string;
}

export interface AccountUpdateRequest {
  acctActiveStatus: string;
  acctCurrBal: number;
  acctCreditLimit: number;
  acctCashCreditLimit: number;
  acctOpenDate: string;
  acctExpiraionDate: string;
  acctReissueDate: string;
  acctCurrCycCredit: number;
  acctCurrCycDebit: number;
  acctAddrZip: string;
  acctGroupId: string;
  customerFirstName: string;
  customerLastName: string;
  customerSsn: string;
  customerDateOfBirth: string;
  customerFicoScore: number;
  customerPhone1: string;
  customerPhone2?: string;
  customerAddress1: string;
  customerAddress2?: string;
  customerCity: string;
  customerState: string;
  customerZipCode: string;
  customerCountry: string;
  customerGovtIssuedId?: string;
  customerEftAccountId?: string;
  customerPriCardHolderInd?: string;
}

export interface CardResponse {
  cardNum: string;
  acctId: number;
  cardName: string;
  cardStatus: string;
  expiryMonth: string;
  expiryYear: string;
}

export interface CardUpdateRequest {
  cardName: string;
  cardStatus: string;
  expiryMonth: string;
  expiryYear: string;
}

export interface TransactionResponse {
  tranId: string;
  cardNum: string;
  tranTypeCd: string;
  tranCatCd: string;
  tranSource: string;
  tranDesc: string;
  tranAmt: number;
  origTs: string;
  procTs: string;
  merchantId: string;
  merchantName: string;
  merchantCity: string;
  merchantZip: string;
}

export interface TransactionCreateRequest {
  cardNum?: string;
  acctId?: string;
  tranTypeCd: string;
  tranCatCd: string;
  tranSource: string;
  tranDesc: string;
  tranAmt: number;
  origDate: string;
  procDate: string;
  merchantId: string;
  merchantName: string;
  merchantCity: string;
  merchantZip: string;
  confirmation: string;
}

export interface BillPaymentRequest {
  accountId: number;
  paymentAmount: number;
  paymentDate: string;
  confirmation: string;
}

export interface BillPaymentResponse {
  acctId: number;
  currentBalance: number;
  paymentAmount: number;
  newBalance: number;
  success: boolean;
  message: string;
}

export interface ReportRequest {
  reportType: string;
  startDate: string;
  endDate: string;
  confirmation?: string;
}

export interface ReportResponse {
  reportId: string;
  reportType: string;
  status: string;
  message: string;
}

export interface UserResponse {
  userId: string;
  firstName: string;
  lastName: string;
  userType: string;
}

export interface UserCreateRequest {
  firstName: string;
  lastName: string;
  userId: string;
  password: string;
  userType: string;
}

export interface UserUpdateRequest {
  firstName: string;
  lastName: string;
  password: string;
  userType: string;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
}

export interface ErrorResponse {
  message: string;
  timestamp: string;
  status: number;
}
