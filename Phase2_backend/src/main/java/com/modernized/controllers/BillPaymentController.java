package com.modernized.controllers;

import com.modernized.dto.BillPaymentRequest;
import com.modernized.dto.BillPaymentResponse;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

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
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        
        if (accountOpt.isEmpty()) {
            throw new EntityNotFoundException("Account ID NOT found");
        }
        
        Account account = accountOpt.get();
        
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
        
        Optional<Account> accountOpt = accountRepository.findById(paymentRequest.getAccountId());
        
        if (accountOpt.isEmpty()) {
            throw new EntityNotFoundException("Account ID NOT found");
        }
        
        Account account = accountOpt.get();
        
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
