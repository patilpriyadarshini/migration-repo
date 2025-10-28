package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountValidationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountValidationService accountValidationService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("2000.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("500.00"));
        testAccount.setAcctExpiraionDate("2025-12-31");
    }

    @Test
    void validateCreditLimit_ShouldReturnTrue_WhenWithinLimit() {
        BigDecimal transactionAmount = new BigDecimal("1000.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertTrue(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenExceedsLimit() {
        BigDecimal transactionAmount = new BigDecimal("4000.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertFalse(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenNullInputs() {
        boolean result1 = accountValidationService.validateCreditLimit(null, new BigDecimal("100.00"));
        boolean result2 = accountValidationService.validateCreditLimit(testAccount, null);
        
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    void validateAccountExpiration_ShouldReturnTrue_WhenNotExpired() {
        String transactionTimestamp = "2025-06-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertTrue(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenExpired() {
        String transactionTimestamp = "2026-01-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertFalse(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnTrue_WhenPositiveBalance() {
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertTrue(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenZeroBalance() {
        testAccount.setAcctCurrBal(BigDecimal.ZERO);
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertFalse(result);
    }

    @Test
    void getCreditLimitFailureCode_ShouldReturn102() {
        int result = accountValidationService.getCreditLimitFailureCode();
        
        assertEquals(102, result);
    }

    @Test
    void getAccountExpirationFailureCode_ShouldReturn103() {
        int result = accountValidationService.getAccountExpirationFailureCode();
        
        assertEquals(103, result);
    }
}
