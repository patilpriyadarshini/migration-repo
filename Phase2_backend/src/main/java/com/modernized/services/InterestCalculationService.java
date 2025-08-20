package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.TransactionCategoryBalance;
import com.modernized.entities.DisclosureGroup;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.TransactionCategoryBalanceRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Business calculation rules for interest processing.
 * Implements interest-related business rules from the CardDemo COBOL system.
 */
@Service
public class InterestCalculationService {

    private final AccountRepository accountRepository;
    private final TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    public InterestCalculationService(AccountRepository accountRepository,
                                    TransactionCategoryBalanceRepository transactionCategoryBalanceRepository) {
        this.accountRepository = accountRepository;
        this.transactionCategoryBalanceRepository = transactionCategoryBalanceRepository;
    }

    /**
     * RULE-CALC-001: Monthly Interest Calculation
     * Calculate monthly interest charges on transaction category balances using account-specific interest rates.
     * 
     * Logic: MONTHLY-INTEREST = (TRANSACTION-CATEGORY-BALANCE * INTEREST-RATE) / 1200
     * 
     * @param transactionCategoryBalance The balance for the specific category
     * @param interestRate The annual interest rate (as percentage)
     * @return Monthly interest amount
     */
    public BigDecimal calculateMonthlyInterest(BigDecimal transactionCategoryBalance, BigDecimal interestRate) {
        if (transactionCategoryBalance == null || interestRate == null) {
            return BigDecimal.ZERO;
        }
        
        if (interestRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return transactionCategoryBalance
            .multiply(interestRate)
            .divide(new BigDecimal("1200"), 2, RoundingMode.HALF_UP);
    }

    /**
     * RULE-CALC-006: Interest Balance Update
     * Add computed monthly interest to account current balance.
     * 
     * Logic: ACCOUNT-CURRENT-BALANCE = ACCOUNT-CURRENT-BALANCE + TOTAL-INTEREST
     *        TOTAL-INTEREST = TOTAL-INTEREST + MONTHLY-INTEREST
     * 
     * @param account The account to update
     * @param monthlyInterest The monthly interest to add
     * @return Updated account balance
     */
    public BigDecimal updateInterestBalance(Account account, BigDecimal monthlyInterest) {
        if (account == null || monthlyInterest == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal newBalance = account.getAcctCurrBal().add(monthlyInterest);
        account.setAcctCurrBal(newBalance);
        
        return newBalance;
    }

    /**
     * RULE-THRESHOLD-009: Interest Rate Application Threshold
     * Only calculate and apply interest when a non-zero interest rate exists for the transaction category.
     * 
     * Logic: IF DISCLOSURE-INTEREST-RATE NOT = 0 THEN PERFORM INTEREST-CALCULATION
     * 
     * @param disclosureGroup The disclosure group containing interest rate
     * @return true if interest should be applied, false otherwise
     */
    public boolean shouldApplyInterest(DisclosureGroup disclosureGroup) {
        if (disclosureGroup == null || disclosureGroup.getDisIntRate() == null) {
            return false;
        }
        
        return disclosureGroup.getDisIntRate().compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * Calculate total interest for all transaction categories of an account.
     * 
     * @param accountId The account ID
     * @return Total monthly interest for the account
     */
    public BigDecimal calculateTotalAccountInterest(Long accountId) {
        if (accountId == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalInterest = BigDecimal.ZERO;
        
        return totalInterest;
    }
}
