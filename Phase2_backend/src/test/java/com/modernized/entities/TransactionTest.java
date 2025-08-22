package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }

    @Test
    void testTransactionCreation() {
        assertNotNull(transaction);
        assertNull(transaction.getTranId());
        assertNull(transaction.getTranAmt());
        assertNull(transaction.getTranCardNum());
        assertNull(transaction.getTranTypeCd());
        assertNull(transaction.getTranCatCd());
        assertNull(transaction.getTranDesc());
        assertNull(transaction.getTranSource());
    }

    @Test
    void testSetAndGetTranId() {
        String tranId = "T123456";
        transaction.setTranId(tranId);
        assertEquals(tranId, transaction.getTranId());
    }

    @Test
    void testSetAndGetTranIdWithNull() {
        transaction.setTranId(null);
        assertNull(transaction.getTranId());
    }

    @Test
    void testSetAndGetTranCardNum() {
        String cardNum = "1234567890123456";
        transaction.setTranCardNum(cardNum);
        assertEquals(cardNum, transaction.getTranCardNum());
    }

    @Test
    void testSetAndGetTranCardNumWithNull() {
        transaction.setTranCardNum(null);
        assertNull(transaction.getTranCardNum());
    }

    @Test
    void testSetAndGetTranAmt() {
        BigDecimal amount = new BigDecimal("100.00");
        transaction.setTranAmt(amount);
        assertEquals(amount, transaction.getTranAmt());
    }

    @Test
    void testSetAndGetTranAmtWithZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        transaction.setTranAmt(zeroAmount);
        assertEquals(zeroAmount, transaction.getTranAmt());
    }

    @Test
    void testSetAndGetTranAmtWithNegative() {
        BigDecimal negativeAmount = new BigDecimal("-50.00");
        transaction.setTranAmt(negativeAmount);
        assertEquals(negativeAmount, transaction.getTranAmt());
    }

    @Test
    void testSetAndGetTranAmtWithLargeValue() {
        BigDecimal largeAmount = new BigDecimal("999999.99");
        transaction.setTranAmt(largeAmount);
        assertEquals(largeAmount, transaction.getTranAmt());
    }

    @Test
    void testSetAndGetTranTypeCd() {
        String typeCode = "01";
        transaction.setTranTypeCd(typeCode);
        assertEquals(typeCode, transaction.getTranTypeCd());
    }

    @Test
    void testSetAndGetTranTypeCdDifferentValues() {
        String[] typeCodes = {"01", "02", "03", "04", "05"};
        
        for (String typeCode : typeCodes) {
            transaction.setTranTypeCd(typeCode);
            assertEquals(typeCode, transaction.getTranTypeCd());
        }
    }

    @Test
    void testSetAndGetTranCatCd() {
        Integer categoryCode = 1;
        transaction.setTranCatCd(categoryCode);
        assertEquals(categoryCode, transaction.getTranCatCd());
    }

    @Test
    void testSetAndGetTranCatCdDifferentValues() {
        Integer[] categoryCodes = {1, 2, 3, 4, 5};
        
        for (Integer categoryCode : categoryCodes) {
            transaction.setTranCatCd(categoryCode);
            assertEquals(categoryCode, transaction.getTranCatCd());
        }
    }

    @Test
    void testSetAndGetTranDesc() {
        String description = "Test Transaction";
        transaction.setTranDesc(description);
        assertEquals(description, transaction.getTranDesc());
    }

    @Test
    void testSetAndGetTranDescWithSpecialCharacters() {
        String description = "Transaction with special chars: @#$%^&*()";
        transaction.setTranDesc(description);
        assertEquals(description, transaction.getTranDesc());
    }

    @Test
    void testSetAndGetTranSource() {
        String source = "ONLINE";
        transaction.setTranSource(source);
        assertEquals(source, transaction.getTranSource());
    }

    @Test
    void testSetAndGetTranSourceDifferentValues() {
        String[] sources = {"ONLINE", "ATM", "POS", "PHONE", "MOBILE"};
        
        for (String source : sources) {
            transaction.setTranSource(source);
            assertEquals(source, transaction.getTranSource());
        }
    }

    @Test
    void testSetAndGetTimestamps() {
        String origTs = "2024-01-15 10:30:00";
        String procTs = "2024-01-15 10:31:00";
        
        transaction.setTranOrigTs(origTs);
        transaction.setTranProcTs(procTs);
        
        assertEquals(origTs, transaction.getTranOrigTs());
        assertEquals(procTs, transaction.getTranProcTs());
    }

    @Test
    void testSetAndGetTimestampsWithNull() {
        transaction.setTranOrigTs(null);
        transaction.setTranProcTs(null);
        
        assertNull(transaction.getTranOrigTs());
        assertNull(transaction.getTranProcTs());
    }

    @Test
    void testSetAndGetMerchantInfo() {
        Long merchantId = 12345L;
        String merchantName = "Test Merchant";
        String merchantCity = "Test City";
        String merchantZip = "12345";
        
        transaction.setTranMerchantId(merchantId);
        transaction.setTranMerchantName(merchantName);
        transaction.setTranMerchantCity(merchantCity);
        transaction.setTranMerchantZip(merchantZip);
        
        assertEquals(merchantId, transaction.getTranMerchantId());
        assertEquals(merchantName, transaction.getTranMerchantName());
        assertEquals(merchantCity, transaction.getTranMerchantCity());
        assertEquals(merchantZip, transaction.getTranMerchantZip());
    }

    @Test
    void testSetAndGetMerchantInfoWithNull() {
        transaction.setTranMerchantId(null);
        transaction.setTranMerchantName(null);
        transaction.setTranMerchantCity(null);
        transaction.setTranMerchantZip(null);
        
        assertNull(transaction.getTranMerchantId());
        assertNull(transaction.getTranMerchantName());
        assertNull(transaction.getTranMerchantCity());
        assertNull(transaction.getTranMerchantZip());
    }

    @Test
    void testCompleteTransactionSetup() {
        transaction.setTranId("T123456");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranAmt(new BigDecimal("100.00"));
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(1);
        transaction.setTranDesc("Test Transaction");
        transaction.setTranSource("ONLINE");
        transaction.setTranOrigTs("2024-01-15 10:30:00");
        transaction.setTranProcTs("2024-01-15 10:31:00");
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Merchant");
        transaction.setTranMerchantCity("Test City");
        transaction.setTranMerchantZip("12345");

        assertEquals("T123456", transaction.getTranId());
        assertEquals("1234567890123456", transaction.getTranCardNum());
        assertEquals(new BigDecimal("100.00"), transaction.getTranAmt());
        assertEquals("01", transaction.getTranTypeCd());
        assertEquals(1, transaction.getTranCatCd());
        assertEquals("Test Transaction", transaction.getTranDesc());
        assertEquals("ONLINE", transaction.getTranSource());
        assertEquals("2024-01-15 10:30:00", transaction.getTranOrigTs());
        assertEquals("2024-01-15 10:31:00", transaction.getTranProcTs());
        assertEquals(12345L, transaction.getTranMerchantId());
        assertEquals("Test Merchant", transaction.getTranMerchantName());
        assertEquals("Test City", transaction.getTranMerchantCity());
        assertEquals("12345", transaction.getTranMerchantZip());
    }

    @Test
    void testTransactionEquality() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        
        transaction1.setTranId("T123456");
        transaction2.setTranId("T123456");
        
        assertEquals(transaction1.getTranId(), transaction2.getTranId());
    }

    @Test
    void testTransactionToString() {
        transaction.setTranId("T123456");
        transaction.setTranAmt(new BigDecimal("100.00"));
        String transactionString = transaction.toString();
        assertNotNull(transactionString);
        assertTrue(transactionString.contains("Transaction") || transactionString.contains("T123456"));
    }
}
