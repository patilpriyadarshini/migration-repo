package com.modernized.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void constructor_ShouldCreateAccountWithAllFields() {
        Account account = new Account(
                1L, "Y", new BigDecimal("1000.00"), new BigDecimal("5000.00"),
                new BigDecimal("1000.00"), "2023-01-01", "2025-12-31", "2023-01-01",
                new BigDecimal("0.00"), new BigDecimal("0.00"), "12345", "GROUP1"
        );

        assertEquals(1L, account.getAcctId());
        assertEquals("Y", account.getAcctActiveStatus());
        assertEquals(new BigDecimal("1000.00"), account.getAcctCurrBal());
        assertEquals(new BigDecimal("5000.00"), account.getAcctCreditLimit());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameAcctId() {
        Account account1 = new Account();
        account1.setAcctId(1L);
        
        Account account2 = new Account();
        account2.setAcctId(1L);

        assertEquals(account1, account2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentAcctId() {
        Account account1 = new Account();
        account1.setAcctId(1L);
        
        Account account2 = new Account();
        account2.setAcctId(2L);

        assertNotEquals(account1, account2);
    }

    @Test
    void hashCode_ShouldBeSame_WhenSameAcctId() {
        Account account1 = new Account();
        account1.setAcctId(1L);
        
        Account account2 = new Account();
        account2.setAcctId(1L);

        assertEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void toString_ShouldContainAcctId() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctActiveStatus("Y");
        account.setAcctCurrBal(new BigDecimal("1000.00"));

        String result = account.toString();

        assertTrue(result.contains("acctId=1"));
        assertTrue(result.contains("acctActiveStatus='Y'"));
        assertTrue(result.contains("acctCurrBal=1000.00"));
    }
}
