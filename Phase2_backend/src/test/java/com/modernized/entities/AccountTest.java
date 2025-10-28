package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private Customer customer;

    @BeforeEach
    void setUp() {
        account = new Account();
        customer = new Customer();
        customer.setCustId(1L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
    }

    @Test
    void testAccountCreation() {
        assertNotNull(account);
        assertNull(account.getAcctId());
        assertNull(account.getAcctCurrBal());
        assertNull(account.getAcctCreditLimit());
        assertNull(account.getAcctActiveStatus());
        assertNull(account.getCustomer());
    }

    @Test
    void testSetAndGetAcctId() {
        Long accountId = 123456789L;
        account.setAcctId(accountId);
        assertEquals(accountId, account.getAcctId());
    }

    @Test
    void testSetAndGetAcctIdWithNull() {
        account.setAcctId(null);
        assertNull(account.getAcctId());
    }

    @Test
    void testSetAndGetAcctCurrBal() {
        BigDecimal balance = new BigDecimal("1000.00");
        account.setAcctCurrBal(balance);
        assertEquals(balance, account.getAcctCurrBal());
    }

    @Test
    void testSetAndGetAcctCurrBalWithZero() {
        BigDecimal zeroBalance = BigDecimal.ZERO;
        account.setAcctCurrBal(zeroBalance);
        assertEquals(zeroBalance, account.getAcctCurrBal());
    }

    @Test
    void testSetAndGetAcctCurrBalWithNegative() {
        BigDecimal negativeBalance = new BigDecimal("-500.00");
        account.setAcctCurrBal(negativeBalance);
        assertEquals(negativeBalance, account.getAcctCurrBal());
    }

    @Test
    void testSetAndGetAcctCreditLimit() {
        BigDecimal creditLimit = new BigDecimal("5000.00");
        account.setAcctCreditLimit(creditLimit);
        assertEquals(creditLimit, account.getAcctCreditLimit());
    }

    @Test
    void testSetAndGetAcctCreditLimitWithNull() {
        account.setAcctCreditLimit(null);
        assertNull(account.getAcctCreditLimit());
    }

    @Test
    void testSetAndGetAcctActiveStatus() {
        String status = "Y";
        account.setAcctActiveStatus(status);
        assertEquals(status, account.getAcctActiveStatus());
    }

    @Test
    void testSetAndGetAcctActiveStatusInactive() {
        String status = "N";
        account.setAcctActiveStatus(status);
        assertEquals(status, account.getAcctActiveStatus());
    }

    @Test
    void testSetAndGetAcctOpenDate() {
        String openDate = "2023-01-01";
        account.setAcctOpenDate(openDate);
        assertEquals(openDate, account.getAcctOpenDate());
    }

    @Test
    void testSetAndGetAcctExpiraionDate() {
        String expirationDate = "2025-12-31";
        account.setAcctExpiraionDate(expirationDate);
        assertEquals(expirationDate, account.getAcctExpiraionDate());
    }

    @Test
    void testSetAndGetCustomer() {
        account.setCustomer(customer);
        assertEquals(customer, account.getCustomer());
        assertEquals("John", account.getCustomer().getCustFirstName());
        assertEquals("Doe", account.getCustomer().getCustLastName());
        assertEquals(1L, account.getCustomer().getCustId());
    }

    @Test
    void testSetAndGetCustomerWithNull() {
        account.setCustomer(null);
        assertNull(account.getCustomer());
    }

    @Test
    void testCycleBalances() {
        BigDecimal credit = new BigDecimal("2000.00");
        BigDecimal debit = new BigDecimal("500.00");
        
        account.setAcctCurrCycCredit(credit);
        account.setAcctCurrCycDebit(debit);
        
        assertEquals(credit, account.getAcctCurrCycCredit());
        assertEquals(debit, account.getAcctCurrCycDebit());
    }

    @Test
    void testCycleBalancesWithZero() {
        BigDecimal zero = BigDecimal.ZERO;
        
        account.setAcctCurrCycCredit(zero);
        account.setAcctCurrCycDebit(zero);
        
        assertEquals(zero, account.getAcctCurrCycCredit());
        assertEquals(zero, account.getAcctCurrCycDebit());
    }

    @Test
    void testSetAndGetAcctCashCreditLimit() {
        BigDecimal cashLimit = new BigDecimal("1000.00");
        account.setAcctCashCreditLimit(cashLimit);
        assertEquals(cashLimit, account.getAcctCashCreditLimit());
    }

    @Test
    void testCompleteAccountSetup() {
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        account.setAcctCurrBal(new BigDecimal("1500.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        account.setAcctOpenDate("2023-01-01");
        account.setAcctExpiraionDate("2025-12-31");
        account.setAcctCurrCycCredit(new BigDecimal("0.00"));
        account.setAcctCurrCycDebit(new BigDecimal("0.00"));
        account.setCustomer(customer);

        assertEquals(123456789L, account.getAcctId());
        assertEquals("Y", account.getAcctActiveStatus());
        assertEquals(new BigDecimal("1500.00"), account.getAcctCurrBal());
        assertEquals(new BigDecimal("5000.00"), account.getAcctCreditLimit());
        assertEquals(new BigDecimal("1000.00"), account.getAcctCashCreditLimit());
        assertEquals("2023-01-01", account.getAcctOpenDate());
        assertEquals("2025-12-31", account.getAcctExpiraionDate());
        assertEquals(new BigDecimal("0.00"), account.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("0.00"), account.getAcctCurrCycDebit());
        assertEquals(customer, account.getCustomer());
    }

    @Test
    void testAccountEquality() {
        Account account1 = new Account();
        Account account2 = new Account();
        
        account1.setAcctId(123456789L);
        account2.setAcctId(123456789L);
        
        assertEquals(account1.getAcctId(), account2.getAcctId());
    }

    @Test
    void testAccountToString() {
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        String accountString = account.toString();
        assertNotNull(accountString);
        assertTrue(accountString.contains("Account") || accountString.contains("123456789"));
    }
}
