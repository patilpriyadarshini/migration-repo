package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountValidationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountValidationService accountValidationService;

    @BeforeEach
    void setUp() {
        accountValidationService = new AccountValidationService(accountRepository);
    }

    @Test
    void validateCreditLimit_ShouldReturnTrue_WhenWithinLimit() {
        Account account = createTestAccount();
        BigDecimal transactionAmount = new BigDecimal("100.00");

        boolean result = accountValidationService.validateCreditLimit(account, transactionAmount);

        assertTrue(result);
    }

    @Test
    void validateCreditLimit_ShouldReturnFalse_WhenExceedsLimit() {
        Account account = createTestAccount();
        BigDecimal transactionAmount = new BigDecimal("6000.00");

        boolean result = accountValidationService.validateCreditLimit(account, transactionAmount);

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
        Account account = createTestAccount();

        boolean result = accountValidationService.validateCreditLimit(account, null);

        assertFalse(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnTrue_WhenNotExpired() {
        Account account = createTestAccount();
        String transactionTimestamp = "2024-06-01 10:00:00";

        boolean result = accountValidationService.validateAccountExpiration(account, transactionTimestamp);

        assertTrue(result);
    }

    @Test
    void validateAccountExpiration_ShouldReturnFalse_WhenExpired() {
        Account account = createTestAccount();
        String transactionTimestamp = "2026-01-01 10:00:00";

        boolean result = accountValidationService.validateAccountExpiration(account, transactionTimestamp);

        assertFalse(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnTrue_WhenPositiveBalance() {
        Account account = createTestAccount();

        boolean result = accountValidationService.validateBillPaymentEligibility(account);

        assertTrue(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenZeroBalance() {
        Account account = createTestAccount();
        account.setAcctCurrBal(BigDecimal.ZERO);

        boolean result = accountValidationService.validateBillPaymentEligibility(account);

        assertFalse(result);
    }

    @Test
    void validateBillPaymentEligibility_ShouldReturnFalse_WhenNegativeBalance() {
        Account account = createTestAccount();
        account.setAcctCurrBal(new BigDecimal("-100.00"));

        boolean result = accountValidationService.validateBillPaymentEligibility(account);

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

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctCurrCycCredit(new BigDecimal("1000.00"));
        account.setAcctCurrCycDebit(new BigDecimal("500.00"));
        account.setAcctExpiraionDate("2025-12-31");
        account.setAcctCurrBal(new BigDecimal("500.00"));
        return account;
    }
}
