package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
import com.modernized.entities.TransactionCategoryBalance;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.TransactionCategoryBalanceRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Business processing rules for transaction handling.
 * Implements transaction-related business rules from the CardDemo COBOL system.
 */
@Service
public class TransactionProcessingService {

    private final AccountRepository accountRepository;
    private final TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    public TransactionProcessingService(AccountRepository accountRepository,
                                      TransactionCategoryBalanceRepository transactionCategoryBalanceRepository) {
        this.accountRepository = accountRepository;
        this.transactionCategoryBalanceRepository = transactionCategoryBalanceRepository;
    }

    /**
     * RULE-CALC-004: Account Balance Update
     * Update account current balance and cycle totals based on transaction amounts.
     * 
     * Logic: CURRENT-BALANCE = CURRENT-BALANCE + TRANSACTION-AMOUNT
     *        IF TRANSACTION-AMOUNT >= 0 THEN
     *           CURRENT-CYCLE-CREDIT = CURRENT-CYCLE-CREDIT + TRANSACTION-AMOUNT
     *        ELSE
     *           CURRENT-CYCLE-DEBIT = CURRENT-CYCLE-DEBIT + TRANSACTION-AMOUNT
     * 
     * @param account The account to update
     * @param transactionAmount The transaction amount
     * @return Updated account
     */
    public Account updateAccountBalance(Account account, BigDecimal transactionAmount) {
        if (account == null || transactionAmount == null) {
            return account;
        }
        
        BigDecimal newBalance = account.getAcctCurrBal().add(transactionAmount);
        account.setAcctCurrBal(newBalance);
        
        if (transactionAmount.compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal newCredit = account.getAcctCurrCycCredit().add(transactionAmount);
            account.setAcctCurrCycCredit(newCredit);
        } else {
            BigDecimal newDebit = account.getAcctCurrCycDebit().add(transactionAmount);
            account.setAcctCurrCycDebit(newDebit);
        }
        
        return account;
    }

    /**
     * RULE-CALC-005: Transaction Category Balance Aggregation
     * Accumulate transaction amounts by account, transaction type, and category for interest calculation.
     * 
     * Logic: TRANSACTION-CATEGORY-BALANCE = TRANSACTION-CATEGORY-BALANCE + TRANSACTION-AMOUNT
     * 
     * @param transactionCategoryBalance The category balance to update
     * @param transactionAmount The transaction amount to add
     * @return Updated transaction category balance
     */
    public TransactionCategoryBalance aggregateTransactionCategoryBalance(
            TransactionCategoryBalance transactionCategoryBalance, 
            BigDecimal transactionAmount) {
        
        if (transactionCategoryBalance == null || transactionAmount == null) {
            return transactionCategoryBalance;
        }
        
        BigDecimal currentBalance = transactionCategoryBalance.getTranCatBal();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        
        BigDecimal newBalance = currentBalance.add(transactionAmount);
        transactionCategoryBalance.setTranCatBal(newBalance);
        
        return transactionCategoryBalance;
    }

    /**
     * RULE-CALC-008: Bill Payment Transaction Amount
     * Set bill payment transaction amount equal to the full current account balance.
     * 
     * Logic: TRANSACTION-AMOUNT = ACCOUNT-CURRENT-BALANCE
     *        NEW-ACCOUNT-BALANCE = ACCOUNT-CURRENT-BALANCE - TRANSACTION-AMOUNT
     * 
     * @param account The account for bill payment
     * @return Bill payment transaction amount (full balance)
     */
    public BigDecimal calculateBillPaymentAmount(Account account) {
        if (account == null || account.getAcctCurrBal() == null) {
            return BigDecimal.ZERO;
        }
        
        return account.getAcctCurrBal();
    }

    /**
     * Process bill payment transaction and update account balance to zero.
     * 
     * @param account The account to process payment for
     * @return The payment amount processed
     */
    public BigDecimal processBillPayment(Account account) {
        if (account == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal paymentAmount = calculateBillPaymentAmount(account);
        
        account.setAcctCurrBal(BigDecimal.ZERO);
        
        return paymentAmount;
    }

    /**
     * Validate and process a transaction against an account.
     * 
     * @param account The account to process transaction against
     * @param transaction The transaction to process
     * @return true if transaction was successfully processed, false otherwise
     */
    public boolean processTransaction(Account account, Transaction transaction) {
        if (account == null || transaction == null || transaction.getTranAmt() == null) {
            return false;
        }
        
        updateAccountBalance(account, transaction.getTranAmt());
        
        return true;
    }
}
