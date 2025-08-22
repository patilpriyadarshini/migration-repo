package com.modernized.controllers;

import com.modernized.dto.TransactionCreateRequest;
import com.modernized.dto.TransactionResponse;
import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Transaction;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.CardRepository;
import com.modernized.repositories.TransactionRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.modernized.utils.EntityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionProcessingService transactionProcessingService;

    @MockBean
    private AccountValidationService accountValidationService;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTransactions_ShouldReturnPagedTransactions() throws Exception {
        Transaction transaction = createTestTransaction();
        TransactionResponse response = createTestTransactionResponse();
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(transaction), PageRequest.of(0, 10), 1);

        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(transactionPage);
        when(entityMapper.mapToTransactionResponse(any(Transaction.class))).thenReturn(response);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction_WhenValidRequest() throws Exception {
        TransactionCreateRequest request = createTestCreateRequest();
        Transaction transaction = createTestTransaction();
        TransactionResponse response = createTestTransactionResponse();
        Card card = createTestCard();
        Account account = createTestAccount();

        when(cardRepository.findById(anyString())).thenReturn(Optional.of(card));
        when(accountValidationService.validateCreditLimit(any(), any())).thenReturn(true);
        when(accountValidationService.validateAccountExpiration(any(), any())).thenReturn(true);
        when(transactionProcessingService.processTransaction(any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(entityMapper.mapToTransactionResponse(any(Transaction.class))).thenReturn(response);

        card.setAccount(account);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").value("T123"));
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranAmt(new BigDecimal("100.00"));
        transaction.setTranDesc("Test Transaction");
        return transaction;
    }

    private TransactionResponse createTestTransactionResponse() {
        TransactionResponse response = new TransactionResponse();
        response.setTranId("T123");
        response.setCardNum("1234567890123456");
        response.setTranAmt(new BigDecimal("100.00"));
        response.setTranDesc("Test Transaction");
        return response;
    }

    private TransactionCreateRequest createTestCreateRequest() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setCardNum("1234567890123456");
        request.setTranTypeCd("01");
        request.setTranCatCd("1");
        request.setTranAmt(new BigDecimal("100.00"));
        request.setTranDesc("Test Transaction");
        request.setConfirmation("Y");
        request.setOrigDate("2023-01-01");
        request.setProcDate("2023-01-01");
        request.setMerchantId("12345");
        request.setMerchantName("Test Merchant");
        request.setMerchantCity("Test City");
        request.setMerchantZip("12345");
        return request;
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        return card;
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctExpiraionDate("2025-12-31");
        return account;
    }
}
