package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryBalanceIdTest {

    private TransactionCategoryBalanceId balanceId;

    @BeforeEach
    void setUp() {
        balanceId = new TransactionCategoryBalanceId();
    }

    @Test
    void testTransactionCategoryBalanceIdCreation() {
        assertNotNull(balanceId);
        assertNull(balanceId.getTrancatAcctId());
        assertNull(balanceId.getTrancatTypeCd());
        assertNull(balanceId.getTrancatCd());
    }

    @Test
    void testParameterizedConstructor() {
        TransactionCategoryBalanceId id = new TransactionCategoryBalanceId(123456789L, "01", 1);
        
        assertEquals(123456789L, id.getTrancatAcctId());
        assertEquals("01", id.getTrancatTypeCd());
        assertEquals(1, id.getTrancatCd());
    }

    @Test
    void testSetAndGetTrancatAcctId() {
        Long acctId = 123456789L;
        balanceId.setTrancatAcctId(acctId);
        assertEquals(acctId, balanceId.getTrancatAcctId());
    }

    @Test
    void testSetAndGetTrancatAcctIdWithNull() {
        balanceId.setTrancatAcctId(null);
        assertNull(balanceId.getTrancatAcctId());
    }

    @Test
    void testSetAndGetTrancatTypeCd() {
        String typeCd = "01";
        balanceId.setTrancatTypeCd(typeCd);
        assertEquals(typeCd, balanceId.getTrancatTypeCd());
    }

    @Test
    void testSetAndGetTrancatTypeCdWithNull() {
        balanceId.setTrancatTypeCd(null);
        assertNull(balanceId.getTrancatTypeCd());
    }

    @Test
    void testSetAndGetTrancatCd() {
        Integer catCd = 1;
        balanceId.setTrancatCd(catCd);
        assertEquals(catCd, balanceId.getTrancatCd());
    }

    @Test
    void testSetAndGetTrancatCdWithNull() {
        balanceId.setTrancatCd(null);
        assertNull(balanceId.getTrancatCd());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(balanceId.equals(balanceId));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(balanceId.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(balanceId.equals("not a transaction category balance id"));
    }

    @Test
    void testEqualsWithSameValues() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        
        assertTrue(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentTrancatAcctId() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(987654321L, "01", 1);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentTrancatTypeCd() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "02", 1);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentTrancatCd() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "01", 2);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testHashCodeConsistency() {
        balanceId.setTrancatAcctId(123456789L);
        balanceId.setTrancatTypeCd("01");
        balanceId.setTrancatCd(1);
        
        int hash1 = balanceId.hashCode();
        int hash2 = balanceId.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameValues() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentValues() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(987654321L, "02", 2);
        
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testCompleteTransactionCategoryBalanceIdSetup() {
        balanceId.setTrancatAcctId(123456789L);
        balanceId.setTrancatTypeCd("01");
        balanceId.setTrancatCd(1);

        assertEquals(123456789L, balanceId.getTrancatAcctId());
        assertEquals("01", balanceId.getTrancatTypeCd());
        assertEquals(1, balanceId.getTrancatCd());
    }

    @Test
    void testSerializableImplementation() {
        TransactionCategoryBalanceId id = new TransactionCategoryBalanceId(123456789L, "01", 1);
        assertNotNull(id);
        assertTrue(id instanceof java.io.Serializable);
    }

    @Test
    void testEqualitySymmetry() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));
    }

    @Test
    void testEqualityTransitivity() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        TransactionCategoryBalanceId id3 = new TransactionCategoryBalanceId(123456789L, "01", 1);
        
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id3));
        assertTrue(id1.equals(id3));
    }

    @Test
    void testNullFieldsEquality() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId();
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId();
        
        assertTrue(id1.equals(id2));
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testMixedNullAndNonNullFields() {
        TransactionCategoryBalanceId id1 = new TransactionCategoryBalanceId(123456789L, null, 1);
        TransactionCategoryBalanceId id2 = new TransactionCategoryBalanceId(123456789L, null, 1);
        
        assertTrue(id1.equals(id2));
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testLargeAccountIdValues() {
        Long largeAcctId = 999999999999L;
        balanceId.setTrancatAcctId(largeAcctId);
        assertEquals(largeAcctId, balanceId.getTrancatAcctId());
    }

    @Test
    void testTypeCdValidation() {
        String validTypeCd = "99";
        balanceId.setTrancatTypeCd(validTypeCd);
        assertEquals(validTypeCd, balanceId.getTrancatTypeCd());
        assertTrue(balanceId.getTrancatTypeCd().length() <= 2);
    }

    @Test
    void testCategoryCodeValidation() {
        Integer validCatCd = 999;
        balanceId.setTrancatCd(validCatCd);
        assertEquals(validCatCd, balanceId.getTrancatCd());
    }
}
