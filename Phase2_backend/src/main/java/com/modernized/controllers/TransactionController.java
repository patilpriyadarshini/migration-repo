package com.modernized.controllers;

import com.modernized.dto.TransactionResponse;
import com.modernized.dto.TransactionCreateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Transaction;
import com.modernized.entities.Card;
import com.modernized.entities.Account;
import com.modernized.repositories.TransactionRepository;
import com.modernized.repositories.CardRepository;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.TransactionProcessingService;
import com.modernized.services.AccountValidationService;
import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import com.modernized.utils.EntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction Management Controller
 * Handles transaction listing, viewing, and creation based on SCREEN-009 (Transaction List),
 * SCREEN-010 (Transaction View), and SCREEN-011 (Transaction Add)
 * Provides comprehensive transaction processing with validation
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final TransactionProcessingService transactionProcessingService;
    private final AccountValidationService accountValidationService;
    private final EntityMapper entityMapper;

    public TransactionController(TransactionRepository transactionRepository,
                               CardRepository cardRepository,
                               AccountRepository accountRepository,
                               TransactionProcessingService transactionProcessingService,
                               AccountValidationService accountValidationService,
                               EntityMapper entityMapper) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.transactionProcessingService = transactionProcessingService;
        this.accountValidationService = accountValidationService;
        this.entityMapper = entityMapper;
    }

    /**
     * Get Paginated Transaction List
     * GET /api/transactions
     * 
     * Displays paginated list of transactions with optional filtering.
     * Based on SCREEN-009 (Transaction List) with 10 transactions per page.
     * 
     * @param transactionId Optional transaction ID filter
     * @param page Page number (0-based)
     * @param size Page size (default 10 as per screen flow)
     * @return PagedResponse with transaction list
     */
    @GetMapping
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactions(
            @RequestParam(required = false) String transactionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage;
        
        if (transactionId != null) {
            transactionPage = transactionRepository.findByTranIdContaining(transactionId, pageable);
        } else {
            transactionPage = transactionRepository.findAll(pageable);
        }
        
        List<TransactionResponse> transactionResponses = transactionPage.getContent().stream()
                .map(entityMapper::mapToTransactionResponse)
                .collect(Collectors.toList());
        
        PagedResponse<TransactionResponse> response = new PagedResponse<>(
                transactionResponses,
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get Transaction Details
     * GET /api/transactions/{id}
     * 
     * Displays detailed information for a specific transaction.
     * Based on SCREEN-010 (Transaction View) functionality.
     * 
     * @param transactionId Transaction ID
     * @return TransactionResponse with transaction details
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        
        if (transactionOpt.isEmpty()) {
            throw new EntityNotFoundException("Transaction not found");
        }
        
        Transaction transaction = transactionOpt.get();
        TransactionResponse response = entityMapper.mapToTransactionResponse(transaction);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create New Transaction
     * POST /api/transactions
     * 
     * Creates new transactions with comprehensive validation and confirmation.
     * Based on SCREEN-011 (Transaction Add) functionality.
     * 
     * @param createRequest Transaction creation data
     * @return TransactionResponse with created transaction details
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionCreateRequest createRequest) {
        
        if ((createRequest.getCardNum() == null || createRequest.getCardNum().trim().isEmpty()) && 
            (createRequest.getAcctId() == null || createRequest.getAcctId().trim().isEmpty())) {
            throw new IllegalArgumentException("Either card number or account ID must be provided");
        }
        
        if (!"Y".equals(createRequest.getConfirmation())) {
            throw new IllegalArgumentException("Transaction not confirmed");
        }
        
        Card card = null;
        Account account = null;
        
        if (createRequest.getCardNum() != null && !createRequest.getCardNum().trim().isEmpty()) {
            Optional<Card> cardOpt = cardRepository.findById(createRequest.getCardNum());
            if (cardOpt.isEmpty()) {
                throw new EntityNotFoundException("Card not found");
            }
            card = cardOpt.get();
            account = card.getAccount();
        } else if (createRequest.getAcctId() != null) {
            Optional<Account> accountOpt = accountRepository.findById(Long.valueOf(createRequest.getAcctId()));
            if (accountOpt.isEmpty()) {
                throw new EntityNotFoundException("Account not found");
            }
            account = accountOpt.get();
        } else {
            throw new IllegalArgumentException("Either card number or account ID must be provided");
        }
        
        if (!accountValidationService.validateCreditLimit(account, createRequest.getTranAmt())) {
            throw new IllegalArgumentException("Transaction would exceed credit limit");
        }
        
        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!accountValidationService.validateAccountExpiration(account, currentTimestamp)) {
            throw new IllegalArgumentException("Transaction received after account expiration");
        }
        
        Transaction transaction = new Transaction();
        transaction.setTranId(generateTransactionId());
        transaction.setTranCardNum(createRequest.getCardNum());
        transaction.setTranTypeCd(createRequest.getTranTypeCd());
        transaction.setTranCatCd(Integer.parseInt(createRequest.getTranCatCd()));
        transaction.setTranSource(createRequest.getTranSource());
        transaction.setTranDesc(createRequest.getTranDesc());
        transaction.setTranAmt(createRequest.getTranAmt());
        transaction.setTranOrigTs(createRequest.getOrigDate() + " 00:00:00");
        transaction.setTranProcTs(createRequest.getProcDate() + " 00:00:00");
        transaction.setTranMerchantId(Long.parseLong(createRequest.getMerchantId()));
        transaction.setTranMerchantName(createRequest.getMerchantName());
        transaction.setTranMerchantCity(createRequest.getMerchantCity());
        transaction.setTranMerchantZip(createRequest.getMerchantZip());
        
        transactionProcessingService.processTransaction(account, transaction);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        accountRepository.save(account);
        
        TransactionResponse response = entityMapper.mapToTransactionResponse(savedTransaction);
        return ResponseEntity.ok(response);
    }


    private String generateTransactionId() {
        return "T" + System.currentTimeMillis();
    }
}
