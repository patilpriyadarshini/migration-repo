package com.modernized.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void testAccountCreation() {
        Account account = new Account();
        account.setAcctActiveStatus("Y");
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        
        assertEquals("Y", account.getAcctActiveStatus());
        assertEquals(new BigDecimal("1000.00"), account.getAcctCurrBal());
    }

    @Test
    public void testAccountEquality() {
        Account account1 = new Account();
        account1.setAcctId(1L);
        
        Account account2 = new Account();
        account2.setAcctId(1L);
        
        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());
    }
}
