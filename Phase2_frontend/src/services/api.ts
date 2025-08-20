import axios from 'axios';
import {
  LoginRequest,
  LoginResponse,
  AccountResponse,
  AccountUpdateRequest,
  CardResponse,
  CardUpdateRequest,
  TransactionResponse,
  TransactionCreateRequest,
  BillPaymentRequest,
  BillPaymentResponse,
  ReportRequest,
  ReportResponse,
  UserResponse,
  UserCreateRequest,
  UserUpdateRequest,
  PagedResponse
} from '../types/api';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/carddemo';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export const authApi = {
  login: (credentials: LoginRequest): Promise<LoginResponse> =>
    api.post('/api/auth/login', credentials).then(res => res.data),
};

export const accountApi = {
  getAccount: (accountId: number): Promise<AccountResponse> =>
    api.get(`/api/accounts/${accountId}`).then(res => res.data),
  
  updateAccount: (accountId: number, data: AccountUpdateRequest): Promise<AccountResponse> =>
    api.put(`/api/accounts/${accountId}`, data).then(res => res.data),
};

export const cardApi = {
  getCards: (params?: { accountId?: number; cardNumber?: string; page?: number; size?: number }): Promise<PagedResponse<CardResponse>> =>
    api.get('/api/cards', { params }).then(res => res.data),
  
  getCard: (cardNumber: string): Promise<CardResponse> =>
    api.get(`/api/cards/${cardNumber}`).then(res => res.data),
  
  updateCard: (cardNumber: string, data: CardUpdateRequest): Promise<CardResponse> =>
    api.put(`/api/cards/${cardNumber}`, data).then(res => res.data),
};

export const transactionApi = {
  getTransactions: (params?: { transactionId?: string; page?: number; size?: number }): Promise<PagedResponse<TransactionResponse>> =>
    api.get('/api/transactions', { params }).then(res => res.data),
  
  getTransaction: (transactionId: string): Promise<TransactionResponse> =>
    api.get(`/api/transactions/${transactionId}`).then(res => res.data),
  
  createTransaction: (data: TransactionCreateRequest): Promise<TransactionResponse> =>
    api.post('/api/transactions', data).then(res => res.data),
};

export const billPaymentApi = {
  getCurrentBalance: (accountId: number): Promise<BillPaymentResponse> =>
    api.get(`/api/bill-payment/${accountId}`).then(res => res.data),
  
  processBillPayment: (data: BillPaymentRequest): Promise<BillPaymentResponse> =>
    api.post('/api/bill-payment', data).then(res => res.data),
};

export const reportApi = {
  generateMonthlyReport: (data: ReportRequest): Promise<ReportResponse> =>
    api.post('/api/reports/monthly', data).then(res => res.data),
  
  generateYearlyReport: (data: ReportRequest): Promise<ReportResponse> =>
    api.post('/api/reports/yearly', data).then(res => res.data),
  
  generateCustomReport: (data: ReportRequest): Promise<ReportResponse> =>
    api.post('/api/reports/custom', data).then(res => res.data),
};

export const userAdminApi = {
  getUsers: (params?: { userId?: string; page?: number; size?: number }): Promise<PagedResponse<UserResponse>> =>
    api.get('/api/admin/users', { params }).then(res => res.data),
  
  getUser: (userId: string): Promise<UserResponse> =>
    api.get(`/api/admin/users/${userId}`).then(res => res.data),
  
  createUser: (data: UserCreateRequest): Promise<UserResponse> =>
    api.post('/api/admin/users', data).then(res => res.data),
  
  updateUser: (userId: string, data: UserUpdateRequest): Promise<UserResponse> =>
    api.put(`/api/admin/users/${userId}`, data).then(res => res.data),
  
  deleteUser: (userId: string): Promise<void> =>
    api.delete(`/api/admin/users/${userId}`).then(res => res.data),
};
