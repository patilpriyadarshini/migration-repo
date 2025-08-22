package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.DisclosureGroup;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.TransactionCategoryBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InterestCalculationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    @InjectMocks
    private InterestCalculationService interestCalculationService;

    private Account testAccount;
    private DisclosureGroup testDisclosureGroup;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));

        testDisclosureGroup = new DisclosureGroup();
        testDisclosureGroup.setDisIntRate(new BigDecimal("18.00"));
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnCorrectAmount_WhenValidInputs() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, interestRate);
        
        assertEquals(new BigDecimal("15.00"), result);
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnZero_WhenBalanceIsNull() {
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(null, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnZero_WhenInterestRateIsNull() {
        BigDecimal balance = new BigDecimal("1000.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnZero_WhenInterestRateIsZero() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal interestRate = BigDecimal.ZERO;
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void updateInterestBalance_ShouldAddInterestToBalance_WhenValidInputs() {
        BigDecimal monthlyInterest = new BigDecimal("15.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, monthlyInterest);
        
        assertEquals(new BigDecimal("1515.00"), result);
        assertEquals(new BigDecimal("1515.00"), testAccount.getAcctCurrBal());
    }

    @Test
    void updateInterestBalance_ShouldReturnZero_WhenAccountIsNull() {
        BigDecimal monthlyInterest = new BigDecimal("15.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(null, monthlyInterest);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void updateInterestBalance_ShouldReturnZero_WhenInterestIsNull() {
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldApplyInterest_ShouldReturnTrue_WhenNonZeroRate() {
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertTrue(result);
    }

    @Test
    void shouldApplyInterest_ShouldReturnFalse_WhenZeroRate() {
        testDisclosureGroup.setDisIntRate(BigDecimal.ZERO);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void shouldApplyInterest_ShouldReturnFalse_WhenDisclosureGroupIsNull() {
        boolean result = interestCalculationService.shouldApplyInterest(null);
        
        assertFalse(result);
    }

    @Test
    void shouldApplyInterest_ShouldReturnFalse_WhenInterestRateIsNull() {
        testDisclosureGroup.setDisIntRate(null);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void calculateTotalAccountInterest_ShouldReturnZero_WhenAccountIdIsNull() {
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateTotalAccountInterest_ShouldReturnZero_WhenValidAccountId() {
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(12345678901L);
        
        assertEquals(BigDecimal.ZERO, result);
    }
}
