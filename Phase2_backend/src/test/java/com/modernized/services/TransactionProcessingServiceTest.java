package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
import com.modernized.entities.TransactionCategoryBalance;
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
import static org.mockito.Mockito.*;

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
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("200.00"));

        testTransaction = new Transaction();
        testTransaction.setTranId("T123456");
        testTransaction.setTranAmt(new BigDecimal("100.00"));

        testCategoryBalance = new TransactionCategoryBalance();
        testCategoryBalance.setTranCatBal(new BigDecimal("250.00"));
    }

    @Test
    void updateAccountBalance_PositiveTransaction_UpdatesCorrectly() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("1100.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("600.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("200.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_NegativeTransaction_UpdatesCorrectly() {
        BigDecimal transactionAmount = new BigDecimal("-50.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("950.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("500.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("150.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_ZeroTransaction_UpdatesCorrectly() {
        BigDecimal transactionAmount = BigDecimal.ZERO;
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("1000.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("500.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("200.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_NullAccount_ReturnsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void updateAccountBalance_NullTransactionAmount_ReturnsOriginalAccount() {
        Account result = transactionProcessingService.updateAccountBalance(testAccount, null);
        
        assertEquals(testAccount, result);
    }

    @Test
    void aggregateTransactionCategoryBalance_ValidInputs_UpdatesCorrectly() {
        BigDecimal transactionAmount = new BigDecimal("75.00");
        
        TransactionCategoryBalance result = transactionProcessingService
            .aggregateTransactionCategoryBalance(testCategoryBalance, transactionAmount);
        
        assertEquals(new BigDecimal("325.00"), result.getTranCatBal());
    }

    @Test
    void aggregateTransactionCategoryBalance_NullCurrentBalance_SetsToTransactionAmount() {
        testCategoryBalance.setTranCatBal(null);
        BigDecimal transactionAmount = new BigDecimal("75.00");
        
        TransactionCategoryBalance result = transactionProcessingService
            .aggregateTransactionCategoryBalance(testCategoryBalance, transactionAmount);
        
        assertEquals(new BigDecimal("75.00"), result.getTranCatBal());
    }

    @Test
    void aggregateTransactionCategoryBalance_NullCategoryBalance_ReturnsNull() {
        BigDecimal transactionAmount = new BigDecimal("75.00");
        
        TransactionCategoryBalance result = transactionProcessingService
            .aggregateTransactionCategoryBalance(null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void aggregateTransactionCategoryBalance_NullTransactionAmount_ReturnsOriginal() {
        TransactionCategoryBalance result = transactionProcessingService
            .aggregateTransactionCategoryBalance(testCategoryBalance, null);
        
        assertEquals(testCategoryBalance, result);
    }

    @Test
    void calculateBillPaymentAmount_ValidAccount_ReturnsCurrentBalance() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    void calculateBillPaymentAmount_NullAccount_ReturnsZero() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateBillPaymentAmount_NullBalance_ReturnsZero() {
        testAccount.setAcctCurrBal(null);
        
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void processBillPayment_ValidAccount_SetsBalanceToZero() {
        BigDecimal result = transactionProcessingService.processBillPayment(testAccount);
        
        assertEquals(new BigDecimal("1000.00"), result);
        assertEquals(BigDecimal.ZERO, testAccount.getAcctCurrBal());
    }

    @Test
    void processBillPayment_NullAccount_ReturnsZero() {
        BigDecimal result = transactionProcessingService.processBillPayment(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void processTransaction_ValidInputs_ReturnsTrue() {
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertTrue(result);
        assertEquals(new BigDecimal("1100.00"), testAccount.getAcctCurrBal());
    }

    @Test
    void processTransaction_NullAccount_ReturnsFalse() {
        boolean result = transactionProcessingService.processTransaction(null, testTransaction);
        
        assertFalse(result);
    }

    @Test
    void processTransaction_NullTransaction_ReturnsFalse() {
        boolean result = transactionProcessingService.processTransaction(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void processTransaction_NullTransactionAmount_ReturnsFalse() {
        testTransaction.setTranAmt(null);
        
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertFalse(result);
    }
}
