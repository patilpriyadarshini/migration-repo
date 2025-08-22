package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
import com.modernized.entities.TransactionCategoryBalance;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.TransactionCategoryBalanceRepository;
import com.modernized.utils.BaseServiceTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionProcessingServiceTest extends BaseServiceTest {

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
        testAccount = TestDataBuilder.createTestAccount();
        testTransaction = TestDataBuilder.createTestTransaction();
    }

    @Test
    void updateAccountBalance_ShouldIncreaseBalance_WhenPositiveAmount() {
        BigDecimal originalBalance = testAccount.getAcctCurrBal();
        BigDecimal transactionAmount = new BigDecimal("100.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertThat(result.getAcctCurrBal()).isEqualTo(originalBalance.add(transactionAmount));
    }

    @Test
    void updateAccountBalance_ShouldDecreaseBalance_WhenNegativeAmount() {
        BigDecimal originalBalance = testAccount.getAcctCurrBal();
        BigDecimal transactionAmount = new BigDecimal("-50.00");
        
        Account result = transactionProcessingService.updateAccountBalance(testAccount, transactionAmount);
        
        assertThat(result.getAcctCurrBal()).isEqualTo(originalBalance.add(transactionAmount));
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnCurrentBalance() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(testAccount);
        
        assertThat(result).isEqualTo(testAccount.getAcctCurrBal());
    }

    @Test
    void calculateBillPaymentAmount_ShouldReturnZero_WhenAccountIsNull() {
        BigDecimal result = transactionProcessingService.calculateBillPaymentAmount(null);
        
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void processBillPayment_ShouldSetBalanceToZero() {
        BigDecimal originalBalance = testAccount.getAcctCurrBal();
        
        BigDecimal paymentAmount = transactionProcessingService.processBillPayment(testAccount);
        
        assertThat(paymentAmount).isEqualTo(originalBalance);
        assertThat(testAccount.getAcctCurrBal()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void processTransaction_ShouldReturnTrue_WhenValidTransaction() {
        boolean result = transactionProcessingService.processTransaction(testAccount, testTransaction);
        
        assertThat(result).isTrue();
    }

    @Test
    void processTransaction_ShouldReturnFalse_WhenTransactionIsNull() {
        boolean result = transactionProcessingService.processTransaction(testAccount, null);
        
        assertThat(result).isFalse();
    }

    @Test
    void aggregateTransactionCategoryBalance_ShouldAddAmount() {
        TransactionCategoryBalance balance = new TransactionCategoryBalance();
        balance.setTranCatBal(new BigDecimal("100.00"));
        BigDecimal additionalAmount = new BigDecimal("50.00");
        
        TransactionCategoryBalance result = transactionProcessingService
                .aggregateTransactionCategoryBalance(balance, additionalAmount);
        
        assertThat(result.getTranCatBal()).isEqualTo(new BigDecimal("150.00"));
    }
}
