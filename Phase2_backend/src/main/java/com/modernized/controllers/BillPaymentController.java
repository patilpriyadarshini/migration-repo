package com.modernized.controllers;

import com.modernized.dto.BillPaymentRequest;
import com.modernized.dto.BillPaymentResponse;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.modernized.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;

/**
 * Bill Payment Controller
 * Handles bill payment processing based on SCREEN-012 (Bill Payment)
 * Processes full balance payments for credit card accounts
 */
@RestController
@RequestMapping("/api/bill-payment")
public class BillPaymentController {

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;
    private final TransactionProcessingService transactionProcessingService;

    public BillPaymentController(AccountRepository accountRepository,
                               AccountValidationService accountValidationService,
                               TransactionProcessingService transactionProcessingService) {
        this.accountRepository = accountRepository;
        this.accountValidationService = accountValidationService;
        this.transactionProcessingService = transactionProcessingService;
    }

    /**
     * Get Current Balance for Payment
     * GET /api/bill-payment/{accountId}
     * 
     * Retrieves current account balance for bill payment confirmation.
     * Based on SCREEN-012 (Bill Payment) balance display functionality.
     * 
     * @param accountId Account ID for payment
     * @return BillPaymentResponse with current balance
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<BillPaymentResponse> getCurrentBalance(@PathVariable Long accountId) {
        Account account = ControllerUtils.findEntityOrThrow(
            accountRepository.findById(accountId), 
            "Account ID NOT found"
        );
        
        if (!accountValidationService.validateBillPaymentEligibility(account)) {
            return ResponseEntity.ok(new BillPaymentResponse(
                accountId,
                account.getAcctCurrBal(),
                BigDecimal.ZERO,
                false,
                "You have nothing to pay..."
            ));
        }
        
        return ResponseEntity.ok(new BillPaymentResponse(
            accountId,
            account.getAcctCurrBal(),
            account.getAcctCurrBal(),
            true,
            "Ready for payment confirmation"
        ));
    }

    /**
     * Process Bill Payment
     * POST /api/bill-payment
     * 
     * Processes full balance payment with confirmation.
     * Based on SCREEN-012 (Bill Payment) payment processing functionality.
     * 
     * @param paymentRequest Payment confirmation data
     * @return BillPaymentResponse with payment result
     */
    @PostMapping
    public ResponseEntity<BillPaymentResponse> processBillPayment(
            @Valid @RequestBody BillPaymentRequest paymentRequest) {
        
        if (!"Y".equals(paymentRequest.getConfirmation())) {
            return ResponseEntity.ok(new BillPaymentResponse(
                paymentRequest.getAccountId(),
                null,
                BigDecimal.ZERO,
                false,
                "Payment cancelled"
            ));
        }
        
        Account account = ControllerUtils.findEntityOrThrow(
            accountRepository.findById(paymentRequest.getAccountId()), 
            "Account ID NOT found"
        );
        
        if (!accountValidationService.validateBillPaymentEligibility(account)) {
            return ResponseEntity.ok(new BillPaymentResponse(
                paymentRequest.getAccountId(),
                account.getAcctCurrBal(),
                BigDecimal.ZERO,
                false,
                "You have nothing to pay..."
            ));
        }
        
        BigDecimal paymentAmount = transactionProcessingService.processBillPayment(account);
        accountRepository.save(account);
        
        return ResponseEntity.ok(new BillPaymentResponse(
            paymentRequest.getAccountId(),
            BigDecimal.ZERO,
            paymentAmount,
            true,
            "Payment processed successfully"
        ));
    }
}
