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
import java.math.RoundingMode;

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
        testAccount.setAcctId(1L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));

        testDisclosureGroup = new DisclosureGroup();
        testDisclosureGroup.setDisIntRate(new BigDecimal("18.00"));
    }

    @Test
    void testRuleCalc001_ValidInterestCalculation_ReturnsCorrectAmount() {
        BigDecimal transactionCategoryBalance = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("18.00");
        BigDecimal expectedInterest = new BigDecimal("15.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, interestRate);
        
        assertEquals(expectedInterest, result);
    }

    @Test
    void testRuleCalc001_HighPrecisionCalculation_ReturnsRoundedAmount() {
        BigDecimal transactionCategoryBalance = new BigDecimal("1234.56");
        BigDecimal interestRate = new BigDecimal("15.75");
        BigDecimal expectedInterest = new BigDecimal("16.20");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, interestRate);
        
        assertEquals(expectedInterest, result);
    }

    @Test
    void testRuleCalc001_ZeroBalance_ReturnsZero() {
        BigDecimal transactionCategoryBalance = BigDecimal.ZERO;
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, interestRate);
        
        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    void testRuleCalc001_ZeroInterestRate_ReturnsZero() {
        BigDecimal transactionCategoryBalance = new BigDecimal("1000.00");
        BigDecimal interestRate = BigDecimal.ZERO;
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc001_NullBalance_ReturnsZero() {
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(null, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc001_NullInterestRate_ReturnsZero() {
        BigDecimal transactionCategoryBalance = new BigDecimal("1000.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc001_SmallAmount_ReturnsCorrectPrecision() {
        BigDecimal transactionCategoryBalance = new BigDecimal("10.00");
        BigDecimal interestRate = new BigDecimal("12.00");
        BigDecimal expectedInterest = new BigDecimal("0.10");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(transactionCategoryBalance, interestRate);
        
        assertEquals(expectedInterest, result);
    }

    @Test
    void testRuleCalc006_ValidInterestUpdate_ReturnsUpdatedBalance() {
        BigDecimal monthlyInterest = new BigDecimal("15.00");
        BigDecimal expectedBalance = new BigDecimal("1015.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, monthlyInterest);
        
        assertEquals(expectedBalance, result);
        assertEquals(expectedBalance, testAccount.getAcctCurrBal());
    }

    @Test
    void testRuleCalc006_ZeroInterest_ReturnsOriginalBalance() {
        BigDecimal originalBalance = testAccount.getAcctCurrBal();
        BigDecimal monthlyInterest = BigDecimal.ZERO;
        
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, monthlyInterest);
        
        assertEquals(originalBalance, result);
        assertEquals(originalBalance, testAccount.getAcctCurrBal());
    }

    @Test
    void testRuleCalc006_NullAccount_ReturnsZero() {
        BigDecimal monthlyInterest = new BigDecimal("15.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(null, monthlyInterest);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc006_NullInterest_ReturnsZero() {
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleThreshold009_NonZeroInterestRate_ReturnsTrue() {
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertTrue(result);
    }

    @Test
    void testRuleThreshold009_ZeroInterestRate_ReturnsFalse() {
        testDisclosureGroup.setDisIntRate(BigDecimal.ZERO);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void testRuleThreshold009_NegativeInterestRate_ReturnsTrue() {
        testDisclosureGroup.setDisIntRate(new BigDecimal("-5.00"));
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertTrue(result);
    }

    @Test
    void testRuleThreshold009_NullDisclosureGroup_ReturnsFalse() {
        boolean result = interestCalculationService.shouldApplyInterest(null);
        
        assertFalse(result);
    }

    @Test
    void testRuleThreshold009_NullInterestRate_ReturnsFalse() {
        testDisclosureGroup.setDisIntRate(null);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void testCalculateTotalAccountInterest_ValidAccountId_ReturnsZero() {
        Long accountId = 1L;
        
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(accountId);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testCalculateTotalAccountInterest_NullAccountId_ReturnsZero() {
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }
}
