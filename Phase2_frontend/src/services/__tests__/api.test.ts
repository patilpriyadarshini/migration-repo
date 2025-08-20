import { describe, it, expect, beforeEach } from 'vitest'
import { authApi, accountApi, cardApi, transactionApi } from '../api'
import { server } from '../../test/mocks/server'

describe('API Service', () => {
  beforeEach(() => {
    server.resetHandlers()
  })

  describe('Authentication', () => {
    it('successfully logs in with valid credentials', async () => {
      const result = await authApi.login({ userId: 'ADMIN001', password: 'admin123' })
      
      expect(result).toEqual({
        userId: 'ADMIN001',
        userType: 'A',
        firstName: 'John',
        lastName: 'Smith',
        success: true,
        message: 'Login successful'
      })
    })

    it('throws error for invalid credentials', async () => {
      await expect(authApi.login({ userId: 'INVALID', password: 'invalid' })).rejects.toThrow('Request failed with status code 401')
    })
  })

  describe('Account Management', () => {
    it('successfully retrieves account information', async () => {
      const result = await accountApi.getAccount(12345678901)
      
      expect(result.acctId).toBe(12345678901)
      expect(result.customerFirstName).toBe('Alice')
      expect(result.customerLastName).toBe('Johnson')
      expect(result.acctCurrBal).toBe(1500.00)
    })

    it('throws error for non-existent account', async () => {
      await expect(accountApi.getAccount(99999999999)).rejects.toThrow('Request failed with status code 404')
    })
  })

  describe('Card Management', () => {
    it('successfully retrieves cards for account', async () => {
      const result = await cardApi.getCards({ accountId: 12345678901 })
      
      expect(result.content).toHaveLength(1)
      expect(result.content[0].cardNum).toBe('4111111111111111')
      expect(result.content[0].cardName).toBe('Alice Johnson')
    })

    it('returns empty array for account with no cards', async () => {
      const result = await cardApi.getCards({ accountId: 99999999999 })
      
      expect(result.content).toHaveLength(0)
    })
  })

  describe('Transaction Management', () => {
    it('successfully retrieves transactions', async () => {
      const result = await transactionApi.getTransactions({})
      
      expect(result.content).toHaveLength(1)
      expect(result.content[0].tranDesc).toBe('GROCERY STORE PURCHASE')
      expect(result.content[0].tranAmt).toBe(85.50)
    })
  })
})
