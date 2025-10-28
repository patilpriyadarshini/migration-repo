package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
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
class TransactionProcessingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    private TransactionProcessingService transactionProcessingService;

    @BeforeEach
    void setUp() {
        transactionProcessingService = new TransactionProcessingService(accountRepository, transactionCategoryBalanceRepository);
    }

    @Test
    void processTransaction_ShouldUpdateAccountBalance() {
        Account account = createTestAccount();
        Transaction transaction = createTestTransaction();
        BigDecimal originalBalance = account.getAcctCurrBal();

        transactionProcessingService.processTransaction(account, transaction);

        BigDecimal expectedBalance = originalBalance.add(transaction.getTranAmt());
        assertEquals(expectedBalance, account.getAcctCurrBal());
    }

    @Test
    void processBillPayment_ShouldZeroBalance() {
        Account account = createTestAccount();
        BigDecimal originalBalance = account.getAcctCurrBal();

        BigDecimal paymentAmount = transactionProcessingService.processBillPayment(account);

        assertEquals(BigDecimal.ZERO, account.getAcctCurrBal());
        assertEquals(originalBalance, paymentAmount);
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctCurrBal(new BigDecimal("500.00"));
        account.setAcctCurrCycCredit(new BigDecimal("1000.00"));
        account.setAcctCurrCycDebit(new BigDecimal("500.00"));
        return account;
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranAmt(new BigDecimal("100.00"));
        return transaction;
    }
}
