package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.DisclosureGroup;
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
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("1000.00"));
        testAccount.setAcctExpirationDate("2027-01-15");
    }

    @Test
    void validateCreditLimit_ShouldReturnTrue_WhenWithinLimit() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertTrue(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenExceedsLimit() {
        BigDecimal transactionAmount = new BigDecimal("3000.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertFalse(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenAccountIsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        boolean result = accountValidationService.validateCreditLimit(null, transactionAmount);
        
        assertFalse(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenTransactionAmountIsNull() {
        boolean result = accountValidationService.validateCreditLimit(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnTrue_WhenNotExpired() {
        String transactionTimestamp = "2025-01-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertTrue(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenExpired() {
        String transactionTimestamp = "2028-01-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertFalse(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenAccountIsNull() {
        String transactionTimestamp = "2025-01-15 10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(null, transactionTimestamp);
        
        assertFalse(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenTimestampIsNull() {
        boolean result = accountValidationService.validateAccountExpiration(testAccount, null);
        
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
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenNegativeBalance() {
        testAccount.setAcctCurrBal(new BigDecimal("-100.00"));
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertFalse(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenAccountIsNull() {
        boolean result = accountValidationService.validateBillPaymentEligibility(null);
        
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
