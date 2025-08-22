package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DailyTransactionTest {

    private DailyTransaction dailyTransaction;

    @BeforeEach
    void setUp() {
        dailyTransaction = new DailyTransaction();
    }

    @Test
    void testDailyTransactionCreation() {
        assertNotNull(dailyTransaction);
        assertNull(dailyTransaction.getDalytranId());
        assertNull(dailyTransaction.getDalytranTypeCd());
        assertNull(dailyTransaction.getDalytranCatCd());
        assertNull(dailyTransaction.getDalytranSource());
        assertNull(dailyTransaction.getDalytranDesc());
        assertNull(dailyTransaction.getDalytranAmt());
        assertNull(dailyTransaction.getDalytranMerchantId());
        assertNull(dailyTransaction.getDalytranMerchantName());
        assertNull(dailyTransaction.getDalytranMerchantCity());
        assertNull(dailyTransaction.getDalytranMerchantZip());
        assertNull(dailyTransaction.getDalytranCardNum());
        assertNull(dailyTransaction.getDalytranOrigTs());
        assertNull(dailyTransaction.getDalytranProcTs());
        assertNull(dailyTransaction.getCard());
        assertNull(dailyTransaction.getTransaction());
    }

    @Test
    void testParameterizedConstructor() {
        DailyTransaction dt = new DailyTransaction(
            "1234567890123456", "01", 1, "POS", "Purchase at Store",
            new BigDecimal("100.00"), 12345L, "Test Merchant",
            "Test City", "12345", "1234567890123456",
            "2023-01-01 10:00:00", "2023-01-01 10:05:00"
        );
        
        assertEquals("1234567890123456", dt.getDalytranId());
        assertEquals("01", dt.getDalytranTypeCd());
        assertEquals(1, dt.getDalytranCatCd());
        assertEquals("POS", dt.getDalytranSource());
        assertEquals("Purchase at Store", dt.getDalytranDesc());
        assertEquals(new BigDecimal("100.00"), dt.getDalytranAmt());
        assertEquals(12345L, dt.getDalytranMerchantId());
        assertEquals("Test Merchant", dt.getDalytranMerchantName());
        assertEquals("Test City", dt.getDalytranMerchantCity());
        assertEquals("12345", dt.getDalytranMerchantZip());
        assertEquals("1234567890123456", dt.getDalytranCardNum());
        assertEquals("2023-01-01 10:00:00", dt.getDalytranOrigTs());
        assertEquals("2023-01-01 10:05:00", dt.getDalytranProcTs());
    }

    @Test
    void testSetAndGetDalytranId() {
        String id = "1234567890123456";
        dailyTransaction.setDalytranId(id);
        assertEquals(id, dailyTransaction.getDalytranId());
    }

    @Test
    void testSetAndGetDalytranIdWithNull() {
        dailyTransaction.setDalytranId(null);
        assertNull(dailyTransaction.getDalytranId());
    }

    @Test
    void testSetAndGetDalytranTypeCd() {
        String typeCd = "01";
        dailyTransaction.setDalytranTypeCd(typeCd);
        assertEquals(typeCd, dailyTransaction.getDalytranTypeCd());
    }

    @Test
    void testSetAndGetDalytranCatCd() {
        Integer catCd = 1;
        dailyTransaction.setDalytranCatCd(catCd);
        assertEquals(catCd, dailyTransaction.getDalytranCatCd());
    }

    @Test
    void testSetAndGetDalytranSource() {
        String source = "POS";
        dailyTransaction.setDalytranSource(source);
        assertEquals(source, dailyTransaction.getDalytranSource());
    }

    @Test
    void testSetAndGetDalytranDesc() {
        String desc = "Purchase at retail store";
        dailyTransaction.setDalytranDesc(desc);
        assertEquals(desc, dailyTransaction.getDalytranDesc());
    }

    @Test
    void testSetAndGetDalytranAmt() {
        BigDecimal amount = new BigDecimal("150.75");
        dailyTransaction.setDalytranAmt(amount);
        assertEquals(amount, dailyTransaction.getDalytranAmt());
    }

    @Test
    void testSetAndGetDalytranAmtWithZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        dailyTransaction.setDalytranAmt(zeroAmount);
        assertEquals(zeroAmount, dailyTransaction.getDalytranAmt());
    }

    @Test
    void testSetAndGetDalytranMerchantId() {
        Long merchantId = 12345L;
        dailyTransaction.setDalytranMerchantId(merchantId);
        assertEquals(merchantId, dailyTransaction.getDalytranMerchantId());
    }

    @Test
    void testSetAndGetDalytranMerchantName() {
        String merchantName = "Test Merchant Store";
        dailyTransaction.setDalytranMerchantName(merchantName);
        assertEquals(merchantName, dailyTransaction.getDalytranMerchantName());
    }

    @Test
    void testSetAndGetDalytranMerchantCity() {
        String city = "New York";
        dailyTransaction.setDalytranMerchantCity(city);
        assertEquals(city, dailyTransaction.getDalytranMerchantCity());
    }

    @Test
    void testSetAndGetDalytranMerchantZip() {
        String zip = "10001";
        dailyTransaction.setDalytranMerchantZip(zip);
        assertEquals(zip, dailyTransaction.getDalytranMerchantZip());
    }

    @Test
    void testSetAndGetDalytranCardNum() {
        String cardNum = "1234567890123456";
        dailyTransaction.setDalytranCardNum(cardNum);
        assertEquals(cardNum, dailyTransaction.getDalytranCardNum());
    }

    @Test
    void testSetAndGetDalytranOrigTs() {
        String origTs = "2023-01-01 10:00:00";
        dailyTransaction.setDalytranOrigTs(origTs);
        assertEquals(origTs, dailyTransaction.getDalytranOrigTs());
    }

    @Test
    void testSetAndGetDalytranProcTs() {
        String procTs = "2023-01-01 10:05:00";
        dailyTransaction.setDalytranProcTs(procTs);
        assertEquals(procTs, dailyTransaction.getDalytranProcTs());
    }

    @Test
    void testSetAndGetCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        dailyTransaction.setCard(card);
        assertEquals(card, dailyTransaction.getCard());
    }

    @Test
    void testSetAndGetCardWithNull() {
        dailyTransaction.setCard(null);
        assertNull(dailyTransaction.getCard());
    }

    @Test
    void testSetAndGetTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("1234567890123456");
        dailyTransaction.setTransaction(transaction);
        assertEquals(transaction, dailyTransaction.getTransaction());
    }

    @Test
    void testSetAndGetTransactionWithNull() {
        dailyTransaction.setTransaction(null);
        assertNull(dailyTransaction.getTransaction());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(dailyTransaction.equals(dailyTransaction));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(dailyTransaction.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(dailyTransaction.equals("not a daily transaction"));
    }

    @Test
    void testEqualsWithSameDalytranId() {
        DailyTransaction dt1 = new DailyTransaction();
        DailyTransaction dt2 = new DailyTransaction();
        dt1.setDalytranId("1234567890123456");
        dt2.setDalytranId("1234567890123456");
        
        assertTrue(dt1.equals(dt2));
    }

    @Test
    void testEqualsWithDifferentDalytranId() {
        DailyTransaction dt1 = new DailyTransaction();
        DailyTransaction dt2 = new DailyTransaction();
        dt1.setDalytranId("1234567890123456");
        dt2.setDalytranId("6543210987654321");
        
        assertFalse(dt1.equals(dt2));
    }

    @Test
    void testHashCodeConsistency() {
        dailyTransaction.setDalytranId("1234567890123456");
        int hash1 = dailyTransaction.hashCode();
        int hash2 = dailyTransaction.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameDalytranId() {
        DailyTransaction dt1 = new DailyTransaction();
        DailyTransaction dt2 = new DailyTransaction();
        dt1.setDalytranId("1234567890123456");
        dt2.setDalytranId("1234567890123456");
        
        assertEquals(dt1.hashCode(), dt2.hashCode());
    }

    @Test
    void testToString() {
        dailyTransaction.setDalytranId("1234567890123456");
        dailyTransaction.setDalytranTypeCd("01");
        dailyTransaction.setDalytranAmt(new BigDecimal("100.00"));
        
        String result = dailyTransaction.toString();
        assertNotNull(result);
        assertTrue(result.contains("DailyTransaction"));
        assertTrue(result.contains("1234567890123456"));
        assertTrue(result.contains("01"));
        assertTrue(result.contains("100.00"));
    }

    @Test
    void testCompleteDailyTransactionSetup() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        
        Transaction transaction = new Transaction();
        transaction.setTranId("1234567890123456");
        
        dailyTransaction.setDalytranId("1234567890123456");
        dailyTransaction.setDalytranTypeCd("01");
        dailyTransaction.setDalytranCatCd(1);
        dailyTransaction.setDalytranSource("POS");
        dailyTransaction.setDalytranDesc("Purchase at store");
        dailyTransaction.setDalytranAmt(new BigDecimal("100.00"));
        dailyTransaction.setDalytranMerchantId(12345L);
        dailyTransaction.setDalytranMerchantName("Test Merchant");
        dailyTransaction.setDalytranMerchantCity("Test City");
        dailyTransaction.setDalytranMerchantZip("12345");
        dailyTransaction.setDalytranCardNum("1234567890123456");
        dailyTransaction.setDalytranOrigTs("2023-01-01 10:00:00");
        dailyTransaction.setDalytranProcTs("2023-01-01 10:05:00");
        dailyTransaction.setCard(card);
        dailyTransaction.setTransaction(transaction);

        assertEquals("1234567890123456", dailyTransaction.getDalytranId());
        assertEquals("01", dailyTransaction.getDalytranTypeCd());
        assertEquals(1, dailyTransaction.getDalytranCatCd());
        assertEquals("POS", dailyTransaction.getDalytranSource());
        assertEquals("Purchase at store", dailyTransaction.getDalytranDesc());
        assertEquals(new BigDecimal("100.00"), dailyTransaction.getDalytranAmt());
        assertEquals(12345L, dailyTransaction.getDalytranMerchantId());
        assertEquals("Test Merchant", dailyTransaction.getDalytranMerchantName());
        assertEquals("Test City", dailyTransaction.getDalytranMerchantCity());
        assertEquals("12345", dailyTransaction.getDalytranMerchantZip());
        assertEquals("1234567890123456", dailyTransaction.getDalytranCardNum());
        assertEquals("2023-01-01 10:00:00", dailyTransaction.getDalytranOrigTs());
        assertEquals("2023-01-01 10:05:00", dailyTransaction.getDalytranProcTs());
        assertEquals(card, dailyTransaction.getCard());
        assertEquals(transaction, dailyTransaction.getTransaction());
    }
}
