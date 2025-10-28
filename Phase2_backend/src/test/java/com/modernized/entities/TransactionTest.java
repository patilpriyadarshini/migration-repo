package com.modernized.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionCreation() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranAmt(new BigDecimal("100.00"));
        transaction.setTranDesc("Test Transaction");
        transaction.setTranTypeCd("01");

        assertEquals("T123", transaction.getTranId());
        assertEquals(new BigDecimal("100.00"), transaction.getTranAmt());
        assertEquals("Test Transaction", transaction.getTranDesc());
        assertEquals("01", transaction.getTranTypeCd());
    }

    @Test
    void testTransactionMerchantInfo() {
        Transaction transaction = new Transaction();
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Merchant");
        transaction.setTranMerchantCity("Test City");

        assertEquals(12345L, transaction.getTranMerchantId());
        assertEquals("Test Merchant", transaction.getTranMerchantName());
        assertEquals("Test City", transaction.getTranMerchantCity());
    }

    @Test
    void testTransactionTimestamps() {
        Transaction transaction = new Transaction();
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:05:00");

        assertEquals("2023-01-01 10:00:00", transaction.getTranOrigTs());
        assertEquals("2023-01-01 10:05:00", transaction.getTranProcTs());
    }
}
