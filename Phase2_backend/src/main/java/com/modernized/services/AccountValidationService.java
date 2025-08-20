package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.entities.Transaction;
import com.modernized.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Business validation rules for Account entity.
 * Implements account-related business rules from the CardDemo COBOL system.
 */
@Service
public class AccountValidationService {

    private final AccountRepository accountRepository;

    public AccountValidationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * RULE-DECISION-002: Credit Limit Validation
     * Reject transactions that would cause account balance to exceed the established credit limit.
     * 
     * Logic: IF CREDIT-LIMIT >= (CURRENT-CYCLE-CREDIT - CURRENT-CYCLE-DEBIT + TRANSACTION-AMOUNT)
     *        THEN APPROVE ELSE REJECT WITH "OVERLIMIT TRANSACTION"
     * 
     * @param account The account to validate
     * @param transactionAmount The transaction amount to validate
     * @return true if transaction is within credit limit, false otherwise
     */
    public boolean validateCreditLimit(Account account, BigDecimal transactionAmount) {
        if (account == null || transactionAmount == null) {
            return false;
        }
        
        BigDecimal currentBalance = account.getAcctCurrCycCredit()
            .subtract(account.getAcctCurrCycDebit())
            .add(transactionAmount);
            
        return account.getAcctCreditLimit().compareTo(currentBalance) >= 0;
    }

    /**
     * RULE-DECISION-003: Account Expiration Validation
     * Reject transactions received after the account expiration date.
     * 
     * Logic: IF ACCOUNT-EXPIRATION-DATE >= TRANSACTION-ORIGINAL-TIMESTAMP
     *        THEN APPROVE ELSE REJECT WITH "TRANSACTION RECEIVED AFTER ACCT EXPIRATION"
     * 
     * @param account The account to validate
     * @param transactionTimestamp The transaction timestamp
     * @return true if account is not expired, false otherwise
     */
    public boolean validateAccountExpiration(Account account, String transactionTimestamp) {
        if (account == null || transactionTimestamp == null) {
            return false;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expirationDate = LocalDate.parse(account.getAcctExpiraionDate(), formatter);
            LocalDate transactionDate = LocalDate.parse(transactionTimestamp.substring(0, 10), formatter);
            
            return !expirationDate.isBefore(transactionDate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * RULE-DECISION-007: Bill Payment Eligibility
     * Only allow bill payments when account has a positive balance to pay.
     * 
     * Logic: IF ACCOUNT-CURRENT-BALANCE <= 0 THEN REJECT WITH "You have nothing to pay..."
     * 
     * @param account The account to validate for bill payment
     * @return true if account has positive balance, false otherwise
     */
    public boolean validateBillPaymentEligibility(Account account) {
        if (account == null || account.getAcctCurrBal() == null) {
            return false;
        }
        
        return account.getAcctCurrBal().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Get validation failure reason code for credit limit validation.
     * 
     * @return 102 for overlimit transaction
     */
    public int getCreditLimitFailureCode() {
        return 102;
    }

    /**
     * Get validation failure reason code for account expiration validation.
     * 
     * @return 103 for expired account
     */
    public int getAccountExpirationFailureCode() {
        return 103;
    }
}
