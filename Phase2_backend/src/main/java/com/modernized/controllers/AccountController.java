package com.modernized.controllers;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.utils.ResponseMapperUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Account Management Controller
 * Handles account viewing and updating based on SCREEN-004 (Account View) and SCREEN-005 (Account Update)
 * Provides comprehensive account and customer information management
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController extends BaseController {

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;

    public AccountController(AccountRepository accountRepository, 
                           AccountValidationService accountValidationService) {
        this.accountRepository = accountRepository;
        this.accountValidationService = accountValidationService;
    }

    /**
     * Get Account Details
     * GET /api/accounts/{id}
     * 
     * Retrieves comprehensive account and customer information for display.
     * Based on SCREEN-004 (Account View) functionality.
     * 
     * @param accountId 11-digit account number
     * @return AccountResponse with account and customer details
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        Account account = findAccountById(accountRepository, accountId);
        AccountResponse response = ResponseMapperUtil.mapToAccountResponse(account);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Update Account Information
     * PUT /api/accounts/{id}
     * 
     * Updates account and customer information with field-level validation.
     * Based on SCREEN-005 (Account Update) functionality.
     * 
     * @param accountId Account ID to update
     * @param updateRequest Updated account and customer data
     * @return AccountResponse with updated information
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountUpdateRequest updateRequest) {
        
        Account account = findAccountById(accountRepository, accountId);
        updateAccountFromRequest(account, updateRequest);
        
        Account savedAccount = accountRepository.save(account);
        AccountResponse response = ResponseMapperUtil.mapToAccountResponse(savedAccount);
        
        return ResponseEntity.ok(response);
    }


    private void updateAccountFromRequest(Account account, AccountUpdateRequest request) {
        account.setAcctActiveStatus(request.getAcctActiveStatus());
        account.setAcctCurrBal(request.getAcctCurrBal());
        account.setAcctCreditLimit(request.getAcctCreditLimit());
        account.setAcctCashCreditLimit(request.getAcctCashCreditLimit());
        account.setAcctOpenDate(request.getAcctOpenDate());
        account.setAcctExpiraionDate(request.getAcctExpiraionDate());
        account.setAcctReissueDate(request.getAcctReissueDate());
        account.setAcctCurrCycCredit(request.getAcctCurrCycCredit());
        account.setAcctCurrCycDebit(request.getAcctCurrCycDebit());
        account.setAcctAddrZip(request.getAcctAddrZip());
        account.setAcctGroupId(request.getAcctGroupId());
        
        if (account.getCustomer() != null) {
            Customer customer = account.getCustomer();
            customer.setCustFirstName(request.getCustomerFirstName());
            customer.setCustLastName(request.getCustomerLastName());
            String ssnString = request.getCustomerSsn();
            if (ssnString != null && !ssnString.trim().isEmpty()) {
                String cleanSsn = ssnString.replace("-", "").trim();
                customer.setCustSsn(Long.parseLong(cleanSsn));
            }
            customer.setCustDobYyyyMmDd(request.getCustomerDateOfBirth());
            customer.setCustFicoCreditScore(request.getCustomerFicoScore());
            customer.setCustPhoneNum1(request.getCustomerPhone1());
            customer.setCustPhoneNum2(request.getCustomerPhone2());
            customer.setCustAddrLine1(request.getCustomerAddress1());
            customer.setCustAddrLine2(request.getCustomerAddress2());
            customer.setCustAddrLine3(request.getCustomerCity());
            customer.setCustAddrStateCd(request.getCustomerState());
            customer.setCustAddrZip(request.getCustomerZipCode());
            customer.setCustAddrCountryCd(request.getCustomerCountry());
            customer.setCustGovtIssuedId(request.getCustomerGovtIssuedId());
            customer.setCustEftAccountId(request.getCustomerEftAccountId());
            customer.setCustPriCardHolderInd(request.getCustomerPriCardHolderInd());
        }
    }
}
