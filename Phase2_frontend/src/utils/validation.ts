import * as yup from 'yup';
import { AccountUpdateRequest, TransactionCreateRequest, UserCreateRequest, BillPaymentRequest, ReportRequest } from '../types/api';

export const loginSchema = yup.object({
  userId: yup
    .string()
    .required('User ID cannot be empty')
    .length(8, 'User ID must be exactly 8 characters'),
  password: yup
    .string()
    .required('Password cannot be empty')
    .length(8, 'Password must be exactly 8 characters'),
});

export const accountUpdateSchema: yup.ObjectSchema<AccountUpdateRequest> = yup.object({
  acctActiveStatus: yup
    .string()
    .required('Account status is required')
    .matches(/^[YN]$/, 'Account status must be Y or N'),
  acctCurrBal: yup
    .number()
    .required('Current balance is required')
    .min(-99999999.99, 'Balance cannot be less than -99999999.99')
    .max(99999999.99, 'Balance cannot exceed 99999999.99'),
  acctCreditLimit: yup
    .number()
    .required('Credit limit is required')
    .min(0, 'Credit limit must be positive')
    .max(99999999.99, 'Credit limit cannot exceed 99999999.99'),
  acctCashCreditLimit: yup
    .number()
    .required('Cash credit limit is required')
    .min(0, 'Cash credit limit must be positive'),
  acctOpenDate: yup
    .string()
    .required('Open date is required'),
  acctExpiraionDate: yup
    .string()
    .required('Expiration date is required'),
  acctReissueDate: yup
    .string()
    .required('Reissue date is required'),
  acctCurrCycCredit: yup
    .number()
    .required('Current cycle credit is required'),
  acctCurrCycDebit: yup
    .number()
    .required('Current cycle debit is required'),
  acctAddrZip: yup
    .string()
    .required('Address ZIP is required'),
  acctGroupId: yup
    .string()
    .required('Group ID is required'),
  customerFirstName: yup
    .string()
    .required('First name is required')
    .max(25, 'First name cannot exceed 25 characters'),
  customerLastName: yup
    .string()
    .required('Last name is required')
    .max(25, 'Last name cannot exceed 25 characters'),
  customerSsn: yup
    .string()
    .required('SSN is required')
    .matches(/^\d{3}-\d{2}-\d{4}$/, 'SSN must be in XXX-XX-XXXX format'),
  customerDateOfBirth: yup
    .string()
    .required('Date of birth is required'),
  customerFicoScore: yup
    .number()
    .required('FICO score is required')
    .min(300, 'FICO score must be at least 300')
    .max(850, 'FICO score cannot exceed 850'),
  customerPhone1: yup
    .string()
    .required('Phone number is required'),
  customerPhone2: yup
    .string()
    .optional(),
  customerAddress1: yup
    .string()
    .required('Address is required'),
  customerAddress2: yup
    .string()
    .optional(),
  customerCity: yup
    .string()
    .required('City is required'),
  customerState: yup
    .string()
    .required('State is required'),
  customerZipCode: yup
    .string()
    .required('ZIP code is required'),
  customerCountry: yup
    .string()
    .required('Country is required'),
  customerGovtIssuedId: yup
    .string()
    .optional(),
  customerEftAccountId: yup
    .string()
    .optional(),
  customerPriCardHolderInd: yup
    .string()
    .oneOf(['Y', 'N'], 'Primary card holder must be Y or N')
    .optional(),
});

export const cardUpdateSchema = yup.object({
  cardName: yup
    .string()
    .required('Card name is required')
    .max(50, 'Card name cannot exceed 50 characters'),
  cardStatus: yup
    .string()
    .required('Card status is required')
    .matches(/^[YN]$/, 'Card status must be Y or N'),
  expiryMonth: yup
    .string()
    .required('Expiry month is required')
    .matches(/^(0[1-9]|1[0-2])$/, 'Expiry month must be 01-12'),
  expiryYear: yup
    .string()
    .required('Expiry year is required')
    .matches(/^\d{4}$/, 'Expiry year must be 4 digits'),
});

export const transactionCreateSchema: yup.ObjectSchema<TransactionCreateRequest> = yup.object({
  cardNum: yup
    .string()
    .optional(),
  acctId: yup
    .string()
    .optional(),
  tranTypeCd: yup
    .string()
    .required('Transaction type is required')
    .length(2, 'Transaction type must be 2 characters'),
  tranCatCd: yup
    .string()
    .required('Transaction category is required')
    .length(2, 'Transaction category must be 2 characters'),
  tranSource: yup
    .string()
    .required('Transaction source is required')
    .max(10, 'Transaction source cannot exceed 10 characters'),
  tranDesc: yup
    .string()
    .required('Transaction description is required')
    .max(26, 'Transaction description cannot exceed 26 characters'),
  tranAmt: yup
    .number()
    .required('Transaction amount is required')
    .min(-99999999.99, 'Amount cannot be less than -99999999.99')
    .max(99999999.99, 'Amount cannot exceed 99999999.99'),
  origDate: yup
    .string()
    .required('Original date is required'),
  procDate: yup
    .string()
    .required('Processing date is required'),
  merchantId: yup
    .string()
    .required('Merchant ID is required')
    .matches(/^\d+$/, 'Merchant ID must be numeric'),
  merchantName: yup
    .string()
    .required('Merchant name is required')
    .max(50, 'Merchant name cannot exceed 50 characters'),
  merchantCity: yup
    .string()
    .required('Merchant city is required')
    .max(50, 'Merchant city cannot exceed 50 characters'),
  merchantZip: yup
    .string()
    .required('Merchant ZIP is required')
    .matches(/^\d{5}$/, 'ZIP code must be exactly 5 digits'),
  confirmation: yup
    .string()
    .required('Confirmation is required')
    .matches(/^[YN]$/, 'Confirmation must be Y or N'),
});

export const userCreateSchema: yup.ObjectSchema<UserCreateRequest> = yup.object({
  firstName: yup
    .string()
    .required('First name is required')
    .max(25, 'First name cannot exceed 25 characters'),
  lastName: yup
    .string()
    .required('Last name is required')
    .max(25, 'Last name cannot exceed 25 characters'),
  userId: yup
    .string()
    .required('User ID is required')
    .length(8, 'User ID must be exactly 8 characters'),
  password: yup
    .string()
    .required('Password is required')
    .length(8, 'Password must be exactly 8 characters'),
  userType: yup
    .string()
    .required('User type is required')
    .matches(/^[AU]$/, 'User type must be A (Admin) or U (User)'),
});

export const billPaymentSchema = yup.object({
  paymentAmount: yup
    .number()
    .required('Payment amount is required')
    .positive('Payment amount must be positive')
    .max(99999999.99, 'Payment amount cannot exceed 99999999.99'),
  paymentDate: yup
    .string()
    .required('Payment date is required'),
  confirmation: yup
    .string()
    .required('Confirmation is required')
    .matches(/^[YN]$/, 'Confirmation must be Y or N'),
});

export const reportSchema: yup.ObjectSchema<ReportRequest> = yup.object({
  reportType: yup
    .string()
    .required('Report type is required')
    .oneOf(['monthly', 'yearly', 'custom'], 'Invalid report type'),
  startDate: yup
    .string()
    .required('Start date is required'),
  endDate: yup
    .string()
    .required('End date is required'),
  confirmation: yup
    .string()
    .matches(/^[YN]$/, 'Confirmation must be Y or N')
    .optional(),
});
