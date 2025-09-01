package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
import com.modernized.entities.TransactionCategoryBalance;
import com.modernized.entities.TransactionCategoryBalanceId;
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
class TransactionProcessingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    @InjectMocks
    private TransactionProcessingService transactionProcessingService;

    private Account testAccount;
    private Transaction testTransaction;
    private TransactionCategoryBalance testCategoryBalance;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(1L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("200.00"));

        testTransaction = new Transaction();
        testTransaction.setTranAmt(new BigDecimal("100.00"));

        TransactionCategoryBalanceId balanceId = new TransactionCategoryBalanceId();
        testCategoryBalance = new TransactionCategoryBalance();
        testCategoryBalance.setId(balanceId);
        testCategoryBalance.setTranCatBal(new BigDecimal("250.00"));
    }

    @Test
    void testRuleCalc004_PositiveTransaction_UpdatesBalanceAndCredit() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        BigDecimal expectedBalance = new BigDecimal("1100.00");
        BigDecimal expectedCredit = new BigDecimal("600.00");
        BigDecimal originalDebit = testAccount.getAcctCurrCycDebit();
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(expectedBalance, result.getAcctCurrBal());
        assertEquals(expectedCredit, result.getAcctCurrCycCredit());
        assertEquals(originalDebit, result.getAcctCurrCycDebit());
    }

    @Test
    void testRuleCalc004_NegativeTransaction_UpdatesBalanceAndDebit() {
        BigDecimal transactionAmount = new BigDecimal("-50.00");
        BigDecimal expectedBalance = new BigDecimal("950.00");
        BigDecimal expectedDebit = new BigDecimal("150.00");
        BigDecimal originalCredit = testAccount.getAcctCurrCycCredit();
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(expectedBalance, result.getAcctCurrBal());
        assertEquals(originalCredit, result.getAcctCurrCycCredit());
        assertEquals(expectedDebit, result.getAcctCurrCycDebit());
    }

    @Test
    void testRuleCalc004_ZeroTransaction_UpdatesBalanceAndCredit() {
        BigDecimal transactionAmount = BigDecimal.ZERO;
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        BigDecimal expectedCredit = new BigDecimal("500.00");
        BigDecimal originalDebit = testAccount.getAcctCurrCycDebit();
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(expectedBalance, result.getAcctCurrBal());
        assertEquals(expectedCredit, result.getAcctCurrCycCredit());
        assertEquals(originalDebit, result.getAcctCurrCycDebit());
    }

    @Test
    void testRuleCalc004_NullAccount_ReturnsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void testRuleCalc004_NullTransactionAmount_ReturnsOriginalAccount() {
        Account result = transactionProcessingService.updateAccountBalance(testAccount, null);
        
        assertEquals(testAccount, result);
    }

    @Test
    void testRuleCalc005_ValidAggregation_UpdatesCategoryBalance() {
        BigDecimal transactionAmount = new BigDecimal("75.00");
        BigDecimal expectedBalance = new BigDecimal("325.00");
        
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            testCategoryBalance, transactionAmount);
        
        assertEquals(expectedBalance, result.getTranCatBal());
    }

    @Test
    void testRuleCalc005_NegativeTransaction_UpdatesCategoryBalance() {
        BigDecimal transactionAmount = new BigDecimal("-25.00");
        BigDecimal expectedBalance = new BigDecimal("225.00");
        
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            testCategoryBalance, transactionAmount);
        
        assertEquals(expectedBalance, result.getTranCatBal());
    }

    @Test
    void testRuleCalc005_ZeroTransaction_KeepsCategoryBalance() {
        BigDecimal transactionAmount = BigDecimal.ZERO;
        BigDecimal expectedBalance = new BigDecimal("250.00");
        
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            testCategoryBalance, transactionAmount);
        
        assertEquals(expectedBalance, result.getTranCatBal());
    }

    @Test
    void testRuleCalc005_NullCategoryBalance_ReturnsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void testRuleCalc005_NullTransactionAmount_ReturnsOriginalBalance() {
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            testCategoryBalance, null);
        
        assertEquals(testCategoryBalance, result);
    }

    @Test
    void testRuleCalc005_NullCurrentBalance_HandlesGracefully() {
        testCategoryBalance.setTranCatBal(null);
        BigDecimal transactionAmount = new BigDecimal("100.00");
        BigDecimal expectedBalance = new BigDecimal("100.00");
        
        TransactionCategoryBalance result = transactionProcessingService.aggregateTransactionCategoryBalance(
            testCategoryBalance, transactionAmount);
        
        assertEquals(expectedBalance, result.getTranCatBal());
    }

    @Test
    void testRuleCalc008_ValidAccount_ReturnsFullBalance() {
        BigDecimal expectedAmount = new BigDecimal("1000.00");
        
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(expectedAmount, result);
    }

    @Test
    void testRuleCalc008_ZeroBalance_ReturnsZero() {
        testAccount.setAcctCurrBal(BigDecimal.ZERO);
        
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc008_NullAccount_ReturnsZero() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testRuleCalc008_NullBalance_ReturnsZero() {
        testAccount.setAcctCurrBal(null);
        
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testProcessBillPayment_ValidAccount_SetsBalanceToZero() {
        BigDecimal originalBalance = testAccount.getAcctCurrBal();
        
        BigDecimal result = transactionProcessingService.processBillPayment(testAccount);
        
        assertEquals(originalBalance, result);
        assertEquals(BigDecimal.ZERO, testAccount.getAcctCurrBal());
    }

    @Test
    void testProcessBillPayment_NullAccount_ReturnsZero() {
        BigDecimal result = transactionProcessingService.processBillPayment(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testProcessTransaction_ValidInputs_ReturnsTrue() {
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertTrue(result);
    }

    @Test
    void testProcessTransaction_NullAccount_ReturnsFalse() {
        boolean result = transactionProcessingService.processTransaction(null, testTransaction);
        
        assertFalse(result);
    }

    @Test
    void testProcessTransaction_NullTransaction_ReturnsFalse() {
        boolean result = transactionProcessingService.processTransaction(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void testProcessTransaction_NullTransactionAmount_ReturnsFalse() {
        testTransaction.setTranAmt(null);
        
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertFalse(result);
    }
}
