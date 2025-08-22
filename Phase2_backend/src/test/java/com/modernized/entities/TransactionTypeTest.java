package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    private TransactionType transactionType;

    @BeforeEach
    void setUp() {
        transactionType = new TransactionType();
    }

    @Test
    void testTransactionTypeCreation() {
        assertNotNull(transactionType);
        assertNull(transactionType.getTranType());
        assertNull(transactionType.getTranTypeDesc());
        assertNull(transactionType.getTransactions());
        assertNull(transactionType.getTransactionCategories());
        assertNull(transactionType.getDisclosureGroups());
    }

    @Test
    void testParameterizedConstructor() {
        TransactionType type = new TransactionType("01", "Purchase");
        
        assertEquals("01", type.getTranType());
        assertEquals("Purchase", type.getTranTypeDesc());
    }

    @Test
    void testSetAndGetTranType() {
        String tranType = "01";
        transactionType.setTranType(tranType);
        assertEquals(tranType, transactionType.getTranType());
    }

    @Test
    void testSetAndGetTranTypeWithNull() {
        transactionType.setTranType(null);
        assertNull(transactionType.getTranType());
    }

    @Test
    void testSetAndGetTranTypeDesc() {
        String description = "Credit Card Purchase";
        transactionType.setTranTypeDesc(description);
        assertEquals(description, transactionType.getTranTypeDesc());
    }

    @Test
    void testSetAndGetTranTypeDescWithNull() {
        transactionType.setTranTypeDesc(null);
        assertNull(transactionType.getTranTypeDesc());
    }

    @Test
    void testSetAndGetTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactionType.setTransactions(transactions);
        assertEquals(transactions, transactionType.getTransactions());
    }

    @Test
    void testSetAndGetTransactionCategories() {
        List<TransactionCategory> categories = new ArrayList<>();
        transactionType.setTransactionCategories(categories);
        assertEquals(categories, transactionType.getTransactionCategories());
    }

    @Test
    void testSetAndGetDisclosureGroups() {
        List<DisclosureGroup> groups = new ArrayList<>();
        transactionType.setDisclosureGroups(groups);
        assertEquals(groups, transactionType.getDisclosureGroups());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(transactionType.equals(transactionType));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(transactionType.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(transactionType.equals("not a transaction type"));
    }

    @Test
    void testEqualsWithSameTranType() {
        TransactionType type1 = new TransactionType("01", "Purchase");
        TransactionType type2 = new TransactionType("01", "Different Description");
        
        assertTrue(type1.equals(type2));
    }

    @Test
    void testEqualsWithDifferentTranType() {
        TransactionType type1 = new TransactionType("01", "Purchase");
        TransactionType type2 = new TransactionType("02", "Payment");
        
        assertFalse(type1.equals(type2));
    }

    @Test
    void testHashCodeConsistency() {
        transactionType.setTranType("01");
        int hash1 = transactionType.hashCode();
        int hash2 = transactionType.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameTranType() {
        TransactionType type1 = new TransactionType("01", "Purchase");
        TransactionType type2 = new TransactionType("01", "Different Description");
        
        assertEquals(type1.hashCode(), type2.hashCode());
    }

    @Test
    void testToString() {
        transactionType.setTranType("01");
        transactionType.setTranTypeDesc("Purchase");
        
        String result = transactionType.toString();
        assertNotNull(result);
        assertTrue(result.contains("TransactionType"));
        assertTrue(result.contains("01"));
        assertTrue(result.contains("Purchase"));
    }

    @Test
    void testCompleteTransactionTypeSetup() {
        transactionType.setTranType("01");
        transactionType.setTranTypeDesc("Credit Card Purchase");
        transactionType.setTransactions(new ArrayList<>());
        transactionType.setTransactionCategories(new ArrayList<>());
        transactionType.setDisclosureGroups(new ArrayList<>());

        assertEquals("01", transactionType.getTranType());
        assertEquals("Credit Card Purchase", transactionType.getTranTypeDesc());
        assertNotNull(transactionType.getTransactions());
        assertNotNull(transactionType.getTransactionCategories());
        assertNotNull(transactionType.getDisclosureGroups());
    }

    @Test
    void testTranTypeValidation() {
        String validType = "01";
        transactionType.setTranType(validType);
        assertEquals(validType, transactionType.getTranType());
        assertTrue(transactionType.getTranType().length() <= 2);
    }

    @Test
    void testTranTypeDescValidation() {
        String validDesc = "Credit Card Purchase Transaction";
        transactionType.setTranTypeDesc(validDesc);
        assertEquals(validDesc, transactionType.getTranTypeDesc());
        assertTrue(transactionType.getTranTypeDesc().length() <= 50);
    }
}
