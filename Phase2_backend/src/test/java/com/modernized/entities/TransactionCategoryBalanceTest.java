package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryBalanceTest {

    private TransactionCategoryBalance transactionCategoryBalance;
    private TransactionCategoryBalanceId balanceId;

    @BeforeEach
    void setUp() {
        transactionCategoryBalance = new TransactionCategoryBalance();
        balanceId = new TransactionCategoryBalanceId(123456789L, "01", 1);
    }

    @Test
    void testTransactionCategoryBalanceCreation() {
        assertNotNull(transactionCategoryBalance);
        assertNull(transactionCategoryBalance.getId());
        assertNull(transactionCategoryBalance.getTranCatBal());
        assertNull(transactionCategoryBalance.getAccount());
        assertNull(transactionCategoryBalance.getTransactionCategory());
    }

    @Test
    void testParameterizedConstructor() {
        BigDecimal balance = new BigDecimal("1500.00");
        TransactionCategoryBalance tcb = new TransactionCategoryBalance(balanceId, balance);
        
        assertEquals(balanceId, tcb.getId());
        assertEquals(balance, tcb.getTranCatBal());
    }

    @Test
    void testSetAndGetId() {
        transactionCategoryBalance.setId(balanceId);
        assertEquals(balanceId, transactionCategoryBalance.getId());
    }

    @Test
    void testSetAndGetIdWithNull() {
        transactionCategoryBalance.setId(null);
        assertNull(transactionCategoryBalance.getId());
    }

    @Test
    void testSetAndGetTranCatBal() {
        BigDecimal balance = new BigDecimal("2500.75");
        transactionCategoryBalance.setTranCatBal(balance);
        assertEquals(balance, transactionCategoryBalance.getTranCatBal());
    }

    @Test
    void testSetAndGetTranCatBalWithZero() {
        BigDecimal zeroBalance = BigDecimal.ZERO;
        transactionCategoryBalance.setTranCatBal(zeroBalance);
        assertEquals(zeroBalance, transactionCategoryBalance.getTranCatBal());
    }

    @Test
    void testSetAndGetTranCatBalWithNegative() {
        BigDecimal negativeBalance = new BigDecimal("-500.00");
        transactionCategoryBalance.setTranCatBal(negativeBalance);
        assertEquals(negativeBalance, transactionCategoryBalance.getTranCatBal());
    }

    @Test
    void testSetAndGetTranCatBalWithNull() {
        transactionCategoryBalance.setTranCatBal(null);
        assertNull(transactionCategoryBalance.getTranCatBal());
    }

    @Test
    void testSetAndGetAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        
        transactionCategoryBalance.setAccount(account);
        assertEquals(account, transactionCategoryBalance.getAccount());
        assertEquals("Y", transactionCategoryBalance.getAccount().getAcctActiveStatus());
    }

    @Test
    void testSetAndGetAccountWithNull() {
        transactionCategoryBalance.setAccount(null);
        assertNull(transactionCategoryBalance.getAccount());
    }

    @Test
    void testSetAndGetTransactionCategory() {
        TransactionCategory category = new TransactionCategory(1, "01", "Retail Purchase");
        
        transactionCategoryBalance.setTransactionCategory(category);
        assertEquals(category, transactionCategoryBalance.getTransactionCategory());
        assertEquals("Retail Purchase", transactionCategoryBalance.getTransactionCategory().getTranCatTypeDesc());
    }

    @Test
    void testSetAndGetTransactionCategoryWithNull() {
        transactionCategoryBalance.setTransactionCategory(null);
        assertNull(transactionCategoryBalance.getTransactionCategory());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(transactionCategoryBalance.equals(transactionCategoryBalance));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(transactionCategoryBalance.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(transactionCategoryBalance.equals("not a transaction category balance"));
    }

    @Test
    void testEqualsWithSameId() {
        TransactionCategoryBalance tcb1 = new TransactionCategoryBalance(balanceId, new BigDecimal("1000.00"));
        TransactionCategoryBalance tcb2 = new TransactionCategoryBalance(balanceId, new BigDecimal("2000.00"));
        
        assertTrue(tcb1.equals(tcb2));
    }

    @Test
    void testEqualsWithDifferentId() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(987654321L, "02", 2);
        
        TransactionCategoryBalance tcb1 = new TransactionCategoryBalance(id1, new BigDecimal("1000.00"));
        TransactionCategoryBalance tcb2 = new TransactionCategoryBalance(id2, new BigDecimal("1000.00"));
        
        assertFalse(tcb1.equals(tcb2));
    }

    @Test
    void testHashCodeConsistency() {
        transactionCategoryBalance.setId(balanceId);
        int hash1 = transactionCategoryBalance.hashCode();
        int hash2 = transactionCategoryBalance.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameId() {
        TransactionCategoryBalance tcb1 = new TransactionCategoryBalance(balanceId, new BigDecimal("1000.00"));
        TransactionCategoryBalance tcb2 = new TransactionCategoryBalance(balanceId, new BigDecimal("2000.00"));
        
        assertEquals(tcb1.hashCode(), tcb2.hashCode());
    }

    @Test
    void testToString() {
        transactionCategoryBalance.setId(balanceId);
        transactionCategoryBalance.setTranCatBal(new BigDecimal("1500.00"));
        
        String result = transactionCategoryBalance.toString();
        assertNotNull(result);
        assertTrue(result.contains("TransactionCategoryBalance"));
        assertTrue(result.contains("1500.00"));
    }

    @Test
    void testCompleteTransactionCategoryBalanceSetup() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        
        TransactionCategory category = new TransactionCategory(1, "01", "Retail Purchase");
        BigDecimal balance = new BigDecimal("2500.75");
        
        transactionCategoryBalance.setId(balanceId);
        transactionCategoryBalance.setTranCatBal(balance);
        transactionCategoryBalance.setAccount(account);
        transactionCategoryBalance.setTransactionCategory(category);

        assertEquals(balanceId, transactionCategoryBalance.getId());
        assertEquals(balance, transactionCategoryBalance.getTranCatBal());
        assertEquals(account, transactionCategoryBalance.getAccount());
        assertEquals(category, transactionCategoryBalance.getTransactionCategory());
    }

    @Test
    void testBalancePrecisionAndScale() {
        BigDecimal preciseBalance = new BigDecimal("12345678.99");
        transactionCategoryBalance.setTranCatBal(preciseBalance);
        assertEquals(preciseBalance, transactionCategoryBalance.getTranCatBal());
        assertTrue(transactionCategoryBalance.getTranCatBal().scale() <= 2);
    }

    @Test
    void testRelationshipConsistency() {
        Account account = new Account();
        account.setAcctId(123456789L);
        
        TransactionCategory category = new TransactionCategory(1, "01", "Retail Purchase");
        
        transactionCategoryBalance.setId(balanceId);
        transactionCategoryBalance.setAccount(account);
        transactionCategoryBalance.setTransactionCategory(category);
        
        assertEquals(balanceId.getTrancatAcctId(), transactionCategoryBalance.getAccount().getAcctId());
        assertEquals(balanceId.getTrancatTypeCd(), transactionCategoryBalance.getTransactionCategory().getTranTypeCd());
        assertEquals(balanceId.getTrancatCd(), transactionCategoryBalance.getTransactionCategory().getTranCatCd());
    }

    @Test
    void testLargeBalanceValues() {
        BigDecimal largeBalance = new BigDecimal("999999999.99");
        transactionCategoryBalance.setTranCatBal(largeBalance);
        assertEquals(largeBalance, transactionCategoryBalance.getTranCatBal());
    }

    @Test
    void testSmallBalanceValues() {
        BigDecimal smallBalance = new BigDecimal("0.01");
        transactionCategoryBalance.setTranCatBal(smallBalance);
        assertEquals(smallBalance, transactionCategoryBalance.getTranCatBal());
    }
}
