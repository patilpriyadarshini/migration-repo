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
        testAccount.setAcctId(1L);
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("1000.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("500.00"));
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctExpiraionDate("2025-12-31");
    }

    @Test
    void testRuleDecision002_ValidCreditLimit_ReturnsTrue() {
        BigDecimal transactionAmount = new BigDecimal("2000.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision002_ExceedsCreditLimit_ReturnsFalse() {
        BigDecimal transactionAmount = new BigDecimal("4600.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision002_ExactCreditLimit_ReturnsTrue() {
        BigDecimal transactionAmount = new BigDecimal("4500.00");
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision002_NullAccount_ReturnsFalse() {
        BigDecimal transactionAmount = new BigDecimal("1000.00");
        
        boolean result = accountValidationService.validateCreditLimit(null, transactionAmount);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision002_NullTransactionAmount_ReturnsFalse() {
        boolean result = accountValidationService.validateCreditLimit(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision002_ZeroTransactionAmount_ReturnsTrue() {
        BigDecimal transactionAmount = BigDecimal.ZERO;
        
        boolean result = accountValidationService.validateCreditLimit(testAccount, transactionAmount);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision003_ValidExpirationDate_ReturnsTrue() {
        String transactionTimestamp = "2025-06-15T10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision003_ExpiredAccount_ReturnsFalse() {
        testAccount.setAcctExpiraionDate("2024-12-31");
        String transactionTimestamp = "2025-06-15T10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision003_ExactExpirationDate_ReturnsTrue() {
        String transactionTimestamp = "2025-12-31T23:59:59";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, transactionTimestamp);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision003_NullAccount_ReturnsFalse() {
        String transactionTimestamp = "2025-06-15T10:30:00";
        
        boolean result = accountValidationService.validateAccountExpiration(null, transactionTimestamp);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision003_NullTimestamp_ReturnsFalse() {
        boolean result = accountValidationService.validateAccountExpiration(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision003_InvalidDateFormat_ReturnsFalse() {
        String invalidTimestamp = "invalid-date";
        
        boolean result = accountValidationService.validateAccountExpiration(testAccount, invalidTimestamp);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision007_PositiveBalance_ReturnsTrue() {
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertTrue(result);
    }

    @Test
    void testRuleDecision007_ZeroBalance_ReturnsFalse() {
        testAccount.setAcctCurrBal(BigDecimal.ZERO);
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision007_NegativeBalance_ReturnsFalse() {
        testAccount.setAcctCurrBal(new BigDecimal("-100.00"));
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision007_NullAccount_ReturnsFalse() {
        boolean result = accountValidationService.validateBillPaymentEligibility(null);
        
        assertFalse(result);
    }

    @Test
    void testRuleDecision007_NullBalance_ReturnsFalse() {
        testAccount.setAcctCurrBal(null);
        
        boolean result = accountValidationService.validateBillPaymentEligibility(testAccount);
        
        assertFalse(result);
    }

    @Test
    void testGetCreditLimitFailureCode_Returns102() {
        int result = accountValidationService.getCreditLimitFailureCode();
        
        assertEquals(102, result);
    }

    @Test
    void testGetAccountExpirationFailureCode_Returns103() {
        int result = accountValidationService.getAccountExpirationFailureCode();
        
        assertEquals(103, result);
    }
}
