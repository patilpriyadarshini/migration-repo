package com.modernized.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void constructor_ShouldCreateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(100);
        transaction.setTranAmt(new BigDecimal("50.00"));
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:01:00");

        assertEquals("T123", transaction.getTranId());
        assertEquals("1234567890123456", transaction.getTranCardNum());
        assertEquals("01", transaction.getTranTypeCd());
        assertEquals(100, transaction.getTranCatCd());
        assertEquals(new BigDecimal("50.00"), transaction.getTranAmt());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameTranId() {
        Transaction transaction1 = new Transaction();
        transaction1.setTranId("T123");
        
        Transaction transaction2 = new Transaction();
        transaction2.setTranId("T123");

        assertEquals(transaction1, transaction2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentTranId() {
        Transaction transaction1 = new Transaction();
        transaction1.setTranId("T123");
        
        Transaction transaction2 = new Transaction();
        transaction2.setTranId("T456");

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void toString_ShouldContainTranId() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranAmt(new BigDecimal("50.00"));

        String result = transaction.toString();

        assertTrue(result.contains("T123"));
    }
}
