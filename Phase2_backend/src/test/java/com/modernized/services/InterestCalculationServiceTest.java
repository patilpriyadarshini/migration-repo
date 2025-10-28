package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.TransactionCategoryBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private InterestCalculationService interestCalculationService;

    @BeforeEach
    void setUp() {
        interestCalculationService = new InterestCalculationService(accountRepository, transactionCategoryBalanceRepository);
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnCorrectInterest() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal rate = new BigDecimal("12.00");
        
        BigDecimal interest = interestCalculationService.calculateMonthlyInterest(balance, rate);
        
        assertNotNull(interest);
        assertEquals(new BigDecimal("10.00"), interest);
    }

    @Test
    void calculateMonthlyInterest_ShouldReturnZero_WhenZeroRate() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal rate = BigDecimal.ZERO;
        
        BigDecimal interest = interestCalculationService.calculateMonthlyInterest(balance, rate);
        
        assertEquals(BigDecimal.ZERO, interest);
    }

    @Test
    void updateInterestBalance_ShouldUpdateAccountBalance() {
        Account account = createTestAccount();
        BigDecimal monthlyInterest = new BigDecimal("10.00");
        BigDecimal originalBalance = account.getAcctCurrBal();
        
        BigDecimal newBalance = interestCalculationService.updateInterestBalance(account, monthlyInterest);
        
        assertEquals(originalBalance.add(monthlyInterest), newBalance);
        assertEquals(newBalance, account.getAcctCurrBal());
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        return account;
    }
}
