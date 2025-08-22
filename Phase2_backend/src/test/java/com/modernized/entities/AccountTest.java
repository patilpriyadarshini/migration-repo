package com.modernized.entities;

import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void constructor_ShouldCreateAccountWithAllFields() {
        Account account = TestDataBuilder.createTestAccount();
        
        assertThat(account.getAcctId()).isEqualTo(12345678901L);
        assertThat(account.getAcctActiveStatus()).isEqualTo("Y");
        assertThat(account.getAcctCurrBal()).isEqualTo(new BigDecimal("1500.00"));
        assertThat(account.getAcctCreditLimit()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(account.getAcctOpenDate()).isEqualTo("2023-01-15");
        assertThat(account.getAcctExpirationDate()).isEqualTo("2025-12-31");
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        Account account1 = TestDataBuilder.createTestAccount();
        Account account2 = TestDataBuilder.createTestAccount();
        
        assertThat(account1).isEqualTo(account2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        Account account1 = TestDataBuilder.createTestAccount();
        Account account2 = TestDataBuilder.createTestAccount();
        account2.setAcctId(99999999999L);
        
        assertThat(account1).isNotEqualTo(account2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        Account account = TestDataBuilder.createTestAccount();
        
        int hashCode1 = account.hashCode();
        int hashCode2 = account.hashCode();
        
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void toString_ShouldContainKeyFields() {
        Account account = TestDataBuilder.createTestAccount();
        
        String result = account.toString();
        
        assertThat(result).contains("12345678901");
        assertThat(result).contains("Y");
        assertThat(result).contains("1500.00");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Account account = new Account();
        
        account.setAcctId(12345L);
        account.setAcctActiveStatus("N");
        account.setAcctCurrBal(new BigDecimal("2000.00"));
        
        assertThat(account.getAcctId()).isEqualTo(12345L);
        assertThat(account.getAcctActiveStatus()).isEqualTo("N");
        assertThat(account.getAcctCurrBal()).isEqualTo(new BigDecimal("2000.00"));
    }
}
