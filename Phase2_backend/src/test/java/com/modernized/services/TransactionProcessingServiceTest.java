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

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("200.00"));

        testTransaction = new Transaction();
        testTransaction.setTranId("T123");
        testTransaction.setTranAmt(new BigDecimal("100.00"));
    }

    @Test
    void updateAccountBalance_ShouldIncreaseBalance_WhenPositiveTransaction() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("1100.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("600.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("200.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_ShouldDecreaseBalance_WhenNegativeTransaction() {
        BigDecimal transactionAmount = new BigDecimal("-50.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("950.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("500.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("250.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnCurrentBalance() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    void processBillPayment_ShouldSetBalanceToZero() {
        BigDecimal paymentAmount = transactionProcessingService.processBillPayment(testAccount);
        
        assertEquals(new BigDecimal("1000.00"), paymentAmount);
        assertEquals(BigDecimal.ZERO, testAccount.getAcctCurrBal());
    }

    @Test
    void processTransaction_ShouldReturnTrue_WhenValidTransaction() {
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertTrue(result);
        assertEquals(new BigDecimal("1100.00"), testAccount.getAcctCurrBal());
    }

    @Test
    void processTransaction_ShouldReturnFalse_WhenNullInputs() {
        boolean result1 = transactionProcessingService.processTransaction(null, testTransaction);
        boolean result2 = transactionProcessingService.processTransaction(testAccount, null);
        
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    void aggregateTransactionCategoryBalance_ShouldUpdateBalance() {
        TransactionCategoryBalance categoryBalance = new TransactionCategoryBalance();
        categoryBalance.setTranCatBal(new BigDecimal("500.00"));
        
        TransactionCategoryBalance result = transactionProcessingService
                .aggregateTransactionCategoryBalance(categoryBalance, new BigDecimal("100.00"));
        
        assertEquals(new BigDecimal("600.00"), result.getTranCatBal());
    }
}
