package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DisclosureGroupTest {

    private DisclosureGroup disclosureGroup;
    private DisclosureGroupId disclosureGroupId;

    @BeforeEach
    void setUp() {
        disclosureGroup = new DisclosureGroup();
        disclosureGroupId = new DisclosureGroupId();
        disclosureGroupId.setDisAcctGroupId("GROUP01");
    }

    @Test
    void testDisclosureGroupCreation() {
        assertNotNull(disclosureGroup);
        assertNull(disclosureGroup.getId());
        assertNull(disclosureGroup.getDisIntRate());
        assertNull(disclosureGroup.getTransactionType());
        assertNull(disclosureGroup.getTransactionCategory());
    }

    @Test
    void testParameterizedConstructor() {
        BigDecimal intRate = new BigDecimal("15.99");
        DisclosureGroup dg = new DisclosureGroup(disclosureGroupId, intRate);
        
        assertEquals(disclosureGroupId, dg.getId());
        assertEquals(intRate, dg.getDisIntRate());
    }

    @Test
    void testSetAndGetId() {
        disclosureGroup.setId(disclosureGroupId);
        assertEquals(disclosureGroupId, disclosureGroup.getId());
    }

    @Test
    void testSetAndGetIdWithNull() {
        disclosureGroup.setId(null);
        assertNull(disclosureGroup.getId());
    }

    @Test
    void testSetAndGetDisIntRate() {
        BigDecimal intRate = new BigDecimal("12.50");
        disclosureGroup.setDisIntRate(intRate);
        assertEquals(intRate, disclosureGroup.getDisIntRate());
    }

    @Test
    void testSetAndGetDisIntRateWithZero() {
        BigDecimal zeroRate = BigDecimal.ZERO;
        disclosureGroup.setDisIntRate(zeroRate);
        assertEquals(zeroRate, disclosureGroup.getDisIntRate());
    }

    @Test
    void testSetAndGetDisIntRateWithNull() {
        disclosureGroup.setDisIntRate(null);
        assertNull(disclosureGroup.getDisIntRate());
    }

    @Test
    void testSetAndGetTransactionType() {
        TransactionType transactionType = new TransactionType("01", "Purchase");
        disclosureGroup.setTransactionType(transactionType);
        assertEquals(transactionType, disclosureGroup.getTransactionType());
    }

    @Test
    void testSetAndGetTransactionTypeWithNull() {
        disclosureGroup.setTransactionType(null);
        assertNull(disclosureGroup.getTransactionType());
    }

    @Test
    void testSetAndGetTransactionCategory() {
        TransactionCategory transactionCategory = new TransactionCategory(1, "01", "Retail Purchase");
        disclosureGroup.setTransactionCategory(transactionCategory);
        assertEquals(transactionCategory, disclosureGroup.getTransactionCategory());
    }

    @Test
    void testSetAndGetTransactionCategoryWithNull() {
        disclosureGroup.setTransactionCategory(null);
        assertNull(disclosureGroup.getTransactionCategory());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(disclosureGroup.equals(disclosureGroup));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(disclosureGroup.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(disclosureGroup.equals("not a disclosure group"));
    }

    @Test
    void testEqualsWithSameId() {
        DisclosureGroup dg1 = new DisclosureGroup(disclosureGroupId, new BigDecimal("15.99"));
        DisclosureGroup dg2 = new DisclosureGroup(disclosureGroupId, new BigDecimal("12.50"));
        
        assertTrue(dg1.equals(dg2));
    }

    @Test
    void testEqualsWithDifferentId() {
        DisclosureGroupId id1 = new DisclosureGroupId();
        id1.setDisAcctGroupId("GROUP01");
        
        DisclosureGroupId id2 = new DisclosureGroupId();
        id2.setDisAcctGroupId("GROUP02");
        
        DisclosureGroup dg1 = new DisclosureGroup(id1, new BigDecimal("15.99"));
        DisclosureGroup dg2 = new DisclosureGroup(id2, new BigDecimal("15.99"));
        
        assertFalse(dg1.equals(dg2));
    }

    @Test
    void testHashCodeConsistency() {
        disclosureGroup.setId(disclosureGroupId);
        int hash1 = disclosureGroup.hashCode();
        int hash2 = disclosureGroup.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameId() {
        DisclosureGroup dg1 = new DisclosureGroup(disclosureGroupId, new BigDecimal("15.99"));
        DisclosureGroup dg2 = new DisclosureGroup(disclosureGroupId, new BigDecimal("12.50"));
        
        assertEquals(dg1.hashCode(), dg2.hashCode());
    }

    @Test
    void testToString() {
        disclosureGroup.setId(disclosureGroupId);
        disclosureGroup.setDisIntRate(new BigDecimal("15.99"));
        
        String result = disclosureGroup.toString();
        assertNotNull(result);
        assertTrue(result.contains("DisclosureGroup"));
        assertTrue(result.contains("15.99"));
    }

    @Test
    void testCompleteDisclosureGroupSetup() {
        TransactionType transactionType = new TransactionType("01", "Purchase");
        TransactionCategory transactionCategory = new TransactionCategory(1, "01", "Retail Purchase");
        BigDecimal intRate = new BigDecimal("15.99");
        
        disclosureGroup.setId(disclosureGroupId);
        disclosureGroup.setDisIntRate(intRate);
        disclosureGroup.setTransactionType(transactionType);
        disclosureGroup.setTransactionCategory(transactionCategory);

        assertEquals(disclosureGroupId, disclosureGroup.getId());
        assertEquals(intRate, disclosureGroup.getDisIntRate());
        assertEquals(transactionType, disclosureGroup.getTransactionType());
        assertEquals(transactionCategory, disclosureGroup.getTransactionCategory());
    }

    @Test
    void testInterestRateValidation() {
        BigDecimal validRate = new BigDecimal("15.99");
        disclosureGroup.setDisIntRate(validRate);
        assertEquals(validRate, disclosureGroup.getDisIntRate());
        assertTrue(disclosureGroup.getDisIntRate().scale() <= 2);
    }

    @Test
    void testInterestRatePrecision() {
        BigDecimal preciseRate = new BigDecimal("99.99");
        disclosureGroup.setDisIntRate(preciseRate);
        assertEquals(preciseRate, disclosureGroup.getDisIntRate());
        assertTrue(disclosureGroup.getDisIntRate().compareTo(new BigDecimal("100.00")) < 0);
    }

    @Test
    void testRelationshipConsistency() {
        TransactionType transactionType = new TransactionType("01", "Purchase");
        TransactionCategory transactionCategory = new TransactionCategory(1, "01", "Retail Purchase");
        
        disclosureGroup.setTransactionType(transactionType);
        disclosureGroup.setTransactionCategory(transactionCategory);
        
        assertEquals("01", disclosureGroup.getTransactionType().getTranType());
        assertEquals("01", disclosureGroup.getTransactionCategory().getTranTypeCd());
        assertEquals(disclosureGroup.getTransactionType().getTranType(), 
                    disclosureGroup.getTransactionCategory().getTranTypeCd());
    }
}
