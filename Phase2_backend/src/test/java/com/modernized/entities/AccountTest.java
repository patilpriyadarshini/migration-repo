package com.modernized.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testAccountCreation() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctActiveStatus("Y");

        assertEquals(123456789L, account.getAcctId());
        assertEquals(new BigDecimal("1000.00"), account.getAcctCurrBal());
        assertEquals("Y", account.getAcctActiveStatus());
    }

    @Test
    void testAccountWithCustomer() {
        Account account = new Account();
        Customer customer = new Customer();
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        
        account.setCustomer(customer);

        assertNotNull(account.getCustomer());
        assertEquals("John", account.getCustomer().getCustFirstName());
        assertEquals("Doe", account.getCustomer().getCustLastName());
    }

    @Test
    void testAccountEquality() {
        Account account1 = new Account();
        account1.setAcctId(123456789L);
        
        Account account2 = new Account();
        account2.setAcctId(123456789L);

        assertEquals(account1.getAcctId(), account2.getAcctId());
    }
}
