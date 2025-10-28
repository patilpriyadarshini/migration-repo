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
import static org.mockito.Mockito.*;

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
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));

        testDisclosureGroup = new DisclosureGroup();
        testDisclosureGroup.setDisIntRate(new BigDecimal("18.00"));
    }

    @Test
    void calculateMonthlyInterest_ValidInputs_ReturnsCorrectAmount() {
        BigDecimal balance = new BigDecimal("1200.00");
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, interestRate);
        
        BigDecimal expected = balance.multiply(interestRate)
            .divide(new BigDecimal("1200"), 2, RoundingMode.HALF_UP);
        assertEquals(expected, result);
        assertEquals(new BigDecimal("18.00"), result);
    }

    @Test
    void calculateMonthlyInterest_ZeroInterestRate_ReturnsZero() {
        BigDecimal balance = new BigDecimal("1200.00");
        BigDecimal interestRate = BigDecimal.ZERO;
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMonthlyInterest_NullBalance_ReturnsZero() {
        BigDecimal interestRate = new BigDecimal("18.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(null, interestRate);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMonthlyInterest_NullInterestRate_ReturnsZero() {
        BigDecimal balance = new BigDecimal("1200.00");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMonthlyInterest_SmallAmount_RoundsCorrectly() {
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal interestRate = new BigDecimal("15.99");
        
        BigDecimal result = interestCalculationService.calculateMonthlyInterest(balance, interestRate);
        
        assertEquals(new BigDecimal("1.33"), result);
    }

    @Test
    void updateInterestBalance_ValidInputs_UpdatesCorrectly() {
        BigDecimal monthlyInterest = new BigDecimal("25.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, monthlyInterest);
        
        assertEquals(new BigDecimal("1025.00"), result);
        assertEquals(new BigDecimal("1025.00"), testAccount.getAcctCurrBal());
    }

    @Test
    void updateInterestBalance_NullAccount_ReturnsZero() {
        BigDecimal monthlyInterest = new BigDecimal("25.00");
        
        BigDecimal result = interestCalculationService.updateInterestBalance(null, monthlyInterest);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void updateInterestBalance_NullInterest_ReturnsZero() {
        BigDecimal result = interestCalculationService.updateInterestBalance(testAccount, null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldApplyInterest_NonZeroRate_ReturnsTrue() {
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertTrue(result);
    }

    @Test
    void shouldApplyInterest_ZeroRate_ReturnsFalse() {
        testDisclosureGroup.setDisIntRate(BigDecimal.ZERO);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void shouldApplyInterest_NullDisclosureGroup_ReturnsFalse() {
        boolean result = interestCalculationService.shouldApplyInterest(null);
        
        assertFalse(result);
    }

    @Test
    void shouldApplyInterest_NullInterestRate_ReturnsFalse() {
        testDisclosureGroup.setDisIntRate(null);
        
        boolean result = interestCalculationService.shouldApplyInterest(testDisclosureGroup);
        
        assertFalse(result);
    }

    @Test
    void calculateTotalAccountInterest_ValidAccountId_ReturnsZero() {
        Long accountId = 123456789L;
        
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(accountId);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateTotalAccountInterest_NullAccountId_ReturnsZero() {
        BigDecimal result = interestCalculationService.calculateTotalAccountInterest(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }
}
