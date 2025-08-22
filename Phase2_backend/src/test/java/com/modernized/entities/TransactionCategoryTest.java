package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryTest {

    private TransactionCategory transactionCategory;

    @BeforeEach
    void setUp() {
        transactionCategory = new TransactionCategory();
    }

    @Test
    void testTransactionCategoryCreation() {
        assertNotNull(transactionCategory);
        assertNull(transactionCategory.getTranCatCd());
        assertNull(transactionCategory.getTranTypeCd());
        assertNull(transactionCategory.getTranCatTypeDesc());
        assertNull(transactionCategory.getTransactionType());
        assertNull(transactionCategory.getTransactions());
        assertNull(transactionCategory.getTransactionCategoryBalances());
        assertNull(transactionCategory.getDisclosureGroups());
    }

    @Test
    void testParameterizedConstructor() {
        TransactionCategory category = new TransactionCategory(1, "01", "Retail Purchase");
        
        assertEquals(1, category.getTranCatCd());
        assertEquals("01", category.getTranTypeCd());
        assertEquals("Retail Purchase", category.getTranCatTypeDesc());
    }

    @Test
    void testSetAndGetTranCatCd() {
        Integer catCode = 1;
        transactionCategory.setTranCatCd(catCode);
        assertEquals(catCode, transactionCategory.getTranCatCd());
    }

    @Test
    void testSetAndGetTranCatCdWithNull() {
        transactionCategory.setTranCatCd(null);
        assertNull(transactionCategory.getTranCatCd());
    }

    @Test
    void testSetAndGetTranTypeCd() {
        String typeCode = "01";
        transactionCategory.setTranTypeCd(typeCode);
        assertEquals(typeCode, transactionCategory.getTranTypeCd());
    }

    @Test
    void testSetAndGetTranTypeCdWithNull() {
        transactionCategory.setTranTypeCd(null);
        assertNull(transactionCategory.getTranTypeCd());
    }

    @Test
    void testSetAndGetTranCatTypeDesc() {
        String description = "Retail Purchase Transaction";
        transactionCategory.setTranCatTypeDesc(description);
        assertEquals(description, transactionCategory.getTranCatTypeDesc());
    }

    @Test
    void testSetAndGetTranCatTypeDescWithNull() {
        transactionCategory.setTranCatTypeDesc(null);
        assertNull(transactionCategory.getTranCatTypeDesc());
    }

    @Test
    void testSetAndGetTransactionType() {
        TransactionType transactionType = new TransactionType("01", "Purchase");
        transactionCategory.setTransactionType(transactionType);
        assertEquals(transactionType, transactionCategory.getTransactionType());
    }

    @Test
    void testSetAndGetTransactionTypeWithNull() {
        transactionCategory.setTransactionType(null);
        assertNull(transactionCategory.getTransactionType());
    }

    @Test
    void testSetAndGetTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactionCategory.setTransactions(transactions);
        assertEquals(transactions, transactionCategory.getTransactions());
    }

    @Test
    void testSetAndGetTransactionCategoryBalances() {
        List<TransactionCategoryBalance> balances = new ArrayList<>();
        transactionCategory.setTransactionCategoryBalances(balances);
        assertEquals(balances, transactionCategory.getTransactionCategoryBalances());
    }

    @Test
    void testSetAndGetDisclosureGroups() {
        List<DisclosureGroup> groups = new ArrayList<>();
        transactionCategory.setDisclosureGroups(groups);
        assertEquals(groups, transactionCategory.getDisclosureGroups());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(transactionCategory.equals(transactionCategory));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(transactionCategory.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(transactionCategory.equals("not a transaction category"));
    }

    @Test
    void testEqualsWithSameTranCatCd() {
        TransactionCategory cat1 = new TransactionCategory(1, "01", "Purchase");
        TransactionCategory cat2 = new TransactionCategory(1, "02", "Different Type");
        
        assertTrue(cat1.equals(cat2));
    }

    @Test
    void testEqualsWithDifferentTranCatCd() {
        TransactionCategory cat1 = new TransactionCategory(1, "01", "Purchase");
        TransactionCategory cat2 = new TransactionCategory(2, "01", "Purchase");
        
        assertFalse(cat1.equals(cat2));
    }

    @Test
    void testHashCodeConsistency() {
        transactionCategory.setTranCatCd(1);
        int hash1 = transactionCategory.hashCode();
        int hash2 = transactionCategory.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameTranCatCd() {
        TransactionCategory cat1 = new TransactionCategory(1, "01", "Purchase");
        TransactionCategory cat2 = new TransactionCategory(1, "02", "Different Type");
        
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString() {
        transactionCategory.setTranCatCd(1);
        transactionCategory.setTranTypeCd("01");
        transactionCategory.setTranCatTypeDesc("Retail Purchase");
        
        String result = transactionCategory.toString();
        assertNotNull(result);
        assertTrue(result.contains("TransactionCategory"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("01"));
        assertTrue(result.contains("Retail Purchase"));
    }

    @Test
    void testCompleteTransactionCategorySetup() {
        TransactionType transactionType = new TransactionType("01", "Purchase");
        
        transactionCategory.setTranCatCd(1);
        transactionCategory.setTranTypeCd("01");
        transactionCategory.setTranCatTypeDesc("Retail Purchase");
        transactionCategory.setTransactionType(transactionType);
        transactionCategory.setTransactions(new ArrayList<>());
        transactionCategory.setTransactionCategoryBalances(new ArrayList<>());
        transactionCategory.setDisclosureGroups(new ArrayList<>());

        assertEquals(1, transactionCategory.getTranCatCd());
        assertEquals("01", transactionCategory.getTranTypeCd());
        assertEquals("Retail Purchase", transactionCategory.getTranCatTypeDesc());
        assertEquals(transactionType, transactionCategory.getTransactionType());
        assertNotNull(transactionCategory.getTransactions());
        assertNotNull(transactionCategory.getTransactionCategoryBalances());
        assertNotNull(transactionCategory.getDisclosureGroups());
    }

    @Test
    void testTranTypeCdValidation() {
        String validTypeCode = "01";
        transactionCategory.setTranTypeCd(validTypeCode);
        assertEquals(validTypeCode, transactionCategory.getTranTypeCd());
        assertTrue(transactionCategory.getTranTypeCd().length() <= 2);
    }

    @Test
    void testTranCatTypeDescValidation() {
        String validDesc = "Retail Purchase Transaction Category";
        transactionCategory.setTranCatTypeDesc(validDesc);
        assertEquals(validDesc, transactionCategory.getTranCatTypeDesc());
        assertTrue(transactionCategory.getTranCatTypeDesc().length() <= 50);
    }
}
