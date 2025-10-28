package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.utils.BaseServiceTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountValidationServiceTest extends BaseServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountValidationService accountValidationService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = TestDataBuilder.createTestAccount();
    }

    @Test
    void validateCreditLimit_ShouldReturnTrue_WhenWithinLimit() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertThat(result).isTrue();
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenExceedsLimit() {
        BigDecimal transactionAmount = new BigDecimal("10000.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertThat(result).isFalse();
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenAccountIsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        boolean result = accountValidationService.validateCreditLimit(null, transactionAmount);
        
        assertThat(result).isFalse();
    }

    @Test
    void validateAccountExpiration_ShouldReturnTrue_WhenNotExpired() {
        String transactionTimestamp = "2024-06-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertThat(result).isTrue();
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenExpired() {
        String transactionTimestamp = "2026-01-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertThat(result).isFalse();
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnTrue_WhenPositiveBalance() {
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertThat(result).isTrue();
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenZeroBalance() {
        testAccount.setAcctCurrBal(BigDecimal.ZERO);
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertThat(result).isFalse();
    }

    @Test
    void getCreditLimitFailureCode_ShouldReturn102() {
        int result = accountValidationService.getCreditLimitFailureCode();
        
        assertThat(result).isEqualTo(102);
    }

    @Test
    void getAccountExpirationFailureCode_ShouldReturn103() {
        int result = accountValidationService.getAccountExpirationFailureCode();
        
        assertThat(result).isEqualTo(103);
    }
}
