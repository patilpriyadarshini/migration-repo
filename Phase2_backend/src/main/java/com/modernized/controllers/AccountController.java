package com.modernized.controllers;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.utils.ControllerUtils;
import com.modernized.utils.EntityMapper;
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
public class AccountController {

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
        Account account = ControllerUtils.findEntityOrThrow(
            accountRepository.findById(accountId), 
            "Account ID NOT found"
        );
        
        AccountResponse response = EntityMapper.mapToAccountResponse(account);
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
        
        Account account = ControllerUtils.findEntityOrThrow(
            accountRepository.findById(accountId), 
            "Account ID NOT found"
        );
        
        EntityMapper.updateAccountFromRequest(account, updateRequest);
        
        Account savedAccount = accountRepository.save(account);
        AccountResponse response = EntityMapper.mapToAccountResponse(savedAccount);
        
        return ResponseEntity.ok(response);
    }

}
