package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisclosureGroupIdTest {

    private DisclosureGroupId disclosureGroupId;

    @BeforeEach
    void setUp() {
        disclosureGroupId = new DisclosureGroupId();
    }

    @Test
    void testDisclosureGroupIdCreation() {
        assertNotNull(disclosureGroupId);
        assertNull(disclosureGroupId.getDisAcctGroupId());
        assertNull(disclosureGroupId.getDisTranTypeCd());
        assertNull(disclosureGroupId.getDisTranCatCd());
    }

    @Test
    void testParameterizedConstructor() {
        DisclosureGroupId id = new DisclosureGroupId("GROUP01", "01", 1);
        
        assertEquals("GROUP01", id.getDisAcctGroupId());
        assertEquals("01", id.getDisTranTypeCd());
        assertEquals(1, id.getDisTranCatCd());
    }

    @Test
    void testSetAndGetDisAcctGroupId() {
        String groupId = "GROUP01";
        disclosureGroupId.setDisAcctGroupId(groupId);
        assertEquals(groupId, disclosureGroupId.getDisAcctGroupId());
    }

    @Test
    void testSetAndGetDisAcctGroupIdWithNull() {
        disclosureGroupId.setDisAcctGroupId(null);
        assertNull(disclosureGroupId.getDisAcctGroupId());
    }

    @Test
    void testSetAndGetDisTranTypeCd() {
        String tranTypeCd = "01";
        disclosureGroupId.setDisTranTypeCd(tranTypeCd);
        assertEquals(tranTypeCd, disclosureGroupId.getDisTranTypeCd());
    }

    @Test
    void testSetAndGetDisTranTypeCdWithNull() {
        disclosureGroupId.setDisTranTypeCd(null);
        assertNull(disclosureGroupId.getDisTranTypeCd());
    }

    @Test
    void testSetAndGetDisTranCatCd() {
        Integer catCd = 1;
        disclosureGroupId.setDisTranCatCd(catCd);
        assertEquals(catCd, disclosureGroupId.getDisTranCatCd());
    }

    @Test
    void testSetAndGetDisTranCatCdWithNull() {
        disclosureGroupId.setDisTranCatCd(null);
        assertNull(disclosureGroupId.getDisTranCatCd());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(disclosureGroupId.equals(disclosureGroupId));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(disclosureGroupId.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(disclosureGroupId.equals("not a disclosure group id"));
    }

    @Test
    void testEqualsWithSameValues() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "01", 1);
        
        assertTrue(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentDisAcctGroupId() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP02", "01", 1);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentDisTranTypeCd() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "02", 1);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsWithDifferentDisTranCatCd() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "01", 2);
        
        assertFalse(id1.equals(id2));
    }

    @Test
    void testHashCodeConsistency() {
        disclosureGroupId.setDisAcctGroupId("GROUP01");
        disclosureGroupId.setDisTranTypeCd("01");
        disclosureGroupId.setDisTranCatCd(1);
        
        int hash1 = disclosureGroupId.hashCode();
        int hash2 = disclosureGroupId.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameValues() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "01", 1);
        
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentValues() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP02", "02", 2);
        
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testCompleteDisclosureGroupIdSetup() {
        disclosureGroupId.setDisAcctGroupId("GROUP01");
        disclosureGroupId.setDisTranTypeCd("01");
        disclosureGroupId.setDisTranCatCd(1);

        assertEquals("GROUP01", disclosureGroupId.getDisAcctGroupId());
        assertEquals("01", disclosureGroupId.getDisTranTypeCd());
        assertEquals(1, disclosureGroupId.getDisTranCatCd());
    }

    @Test
    void testSerializableImplementation() {
        DisclosureGroupId id = new DisclosureGroupId("GROUP01", "01", 1);
        assertNotNull(id);
        assertTrue(id instanceof java.io.Serializable);
    }

    @Test
    void testEqualitySymmetry() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "01", 1);
        
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));
    }

    @Test
    void testEqualityTransitivity() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", "01", 1);
        DisclosureGroupId id3 = new DisclosureGroupId("GROUP01", "01", 1);
        
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id3));
        assertTrue(id1.equals(id3));
    }

    @Test
    void testNullFieldsEquality() {
        DisclosureGroupId id1 = new DisclosureGroupId();
        DisclosureGroupId id2 = new DisclosureGroupId();
        
        assertTrue(id1.equals(id2));
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testMixedNullAndNonNullFields() {
        DisclosureGroupId id1 = new DisclosureGroupId("GROUP01", null, 1);
        DisclosureGroupId id2 = new DisclosureGroupId("GROUP01", null, 1);
        
        assertTrue(id1.equals(id2));
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
