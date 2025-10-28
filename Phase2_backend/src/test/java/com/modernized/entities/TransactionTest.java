package com.modernized.entities;

import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    @Test
    void constructor_ShouldCreateTransactionWithAllFields() {
        Transaction transaction = TestDataBuilder.createTestTransaction();
        
        assertThat(transaction.getTranId()).isEqualTo("T123456789");
        assertThat(transaction.getTranTypeCd()).isEqualTo("01");
        assertThat(transaction.getTranCatCd()).isEqualTo(1);
        assertThat(transaction.getTranSource()).isEqualTo("ONLINE");
        assertThat(transaction.getTranDesc()).isEqualTo("Test Purchase");
        assertThat(transaction.getTranAmt()).isEqualTo(new BigDecimal("100.00"));
        assertThat(transaction.getTranCardNum()).isEqualTo("4000123456789012");
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameTransactionId() {
        Transaction transaction1 = TestDataBuilder.createTestTransaction();
        Transaction transaction2 = TestDataBuilder.createTestTransaction();
        
        assertThat(transaction1).isEqualTo(transaction2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentTransactionId() {
        Transaction transaction1 = TestDataBuilder.createTestTransaction();
        Transaction transaction2 = TestDataBuilder.createTestTransaction();
        transaction2.setTranId("T999999999");
        
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void toString_ShouldContainKeyFields() {
        Transaction transaction = TestDataBuilder.createTestTransaction();
        
        String result = transaction.toString();
        
        assertThat(result).contains("T123456789");
        assertThat(result).contains("01");
        assertThat(result).contains("100.00");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Transaction transaction = new Transaction();
        
        transaction.setTranId("T999");
        transaction.setTranTypeCd("02");
        transaction.setTranAmt(new BigDecimal("250.00"));
        
        assertThat(transaction.getTranId()).isEqualTo("T999");
        assertThat(transaction.getTranTypeCd()).isEqualTo("02");
        assertThat(transaction.getTranAmt()).isEqualTo(new BigDecimal("250.00"));
    }
}
