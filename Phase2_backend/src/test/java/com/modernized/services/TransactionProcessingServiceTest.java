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
    private TransactionCategoryBalance testCategoryBalance;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("1000.00"));

        testTransaction = new Transaction();
        testTransaction.setTranId("T123456789");
        testTransaction.setTranAmt(new BigDecimal("100.00"));

        testCategoryBalance = new TransactionCategoryBalance();
        testCategoryBalance.setTranCatBal(new BigDecimal("500.00"));
    }

    @Test
    void updateAccountBalance_ShouldIncreaseBalance_WhenPositiveAmount() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("1600.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("2600.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("1000.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_ShouldDecreaseBalance_WhenNegativeAmount() {
        BigDecimal transactionAmount = new BigDecimal("-50.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertEquals(new BigDecimal("1450.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("2500.00"), result.getAcctCurrCycCredit());
        assertEquals(new BigDecimal("950.00"), result.getAcctCurrCycDebit());
    }

    @Test
    void updateAccountBalance_ShouldReturnSameAccount_WhenAccountIsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void updateAccountBalance_ShouldReturnSameAccount_WhenAmountIsNull() {
        Account result = transactionProcessingService.updateAccountBalance(testAccount, null);
        
        assertEquals(testAccount, result);
        assertEquals(new BigDecimal("1500.00"), result.getAcctCurrBal());
    }

    @Test
    void aggregateTransactionCategoryBalance_ShouldAddAmount_WhenValidInputs() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        TransactionCategoryBalance result = transactionProcessingService
                .aggregateTransactionCategoryBalance(testCategoryBalance, transactionAmount);
        
        assertEquals(new BigDecimal("600.00"), result.getTranCatBal());
    }

    @Test
    void aggregateTransactionCategoryBalance_ShouldHandleNullBalance_WhenBalanceIsNull() {
        testCategoryBalance.setTranCatBal(null);
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        TransactionCategoryBalance result = transactionProcessingService
                .aggregateTransactionCategoryBalance(testCategoryBalance, transactionAmount);
        
        assertEquals(new BigDecimal("100.00"), result.getTranCatBal());
    }

    @Test
    void aggregateTransactionCategoryBalance_ShouldReturnSame_WhenCategoryBalanceIsNull() {
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        TransactionCategoryBalance result = transactionProcessingService
                .aggregateTransactionCategoryBalance(null, transactionAmount);
        
        assertNull(result);
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnCurrentBalance_WhenValidAccount() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(new BigDecimal("1500.00"), result);
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnZero_WhenAccountIsNull() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnZero_WhenBalanceIsNull() {
        testAccount.setAcctCurrBal(null);
        
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void processBillPayment_ShouldSetBalanceToZero_WhenValidAccount() {
        BigDecimal result = transactionProcessingService.processBillPayment(testAccount);
        
        assertEquals(new BigDecimal("1500.00"), result);
        assertEquals(BigDecimal.ZERO, testAccount.getAcctCurrBal());
    }

    @Test
    void processBillPayment_ShouldReturnZero_WhenAccountIsNull() {
        BigDecimal result = transactionProcessingService.processBillPayment(null);
        
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void processTransaction_ShouldReturnTrue_WhenValidInputs() {
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertTrue(result);
        assertEquals(new BigDecimal("1600.00"), testAccount.getAcctCurrBal());
    }

    @Test
    void processTransaction_ShouldReturnFalse_WhenAccountIsNull() {
        boolean result = transactionProcessingService.processTransaction(null, testTransaction);
        
        assertFalse(result);
    }

    @Test
    void processTransaction_ShouldReturnFalse_WhenTransactionIsNull() {
        boolean result = transactionProcessingService.processTransaction(testAccount, null);
        
        assertFalse(result);
    }

    @Test
    void processTransaction_ShouldReturnFalse_WhenTransactionAmountIsNull() {
        testTransaction.setTranAmt(null);
        
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertFalse(result);
    }
}
