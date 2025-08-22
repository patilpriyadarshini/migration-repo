package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTransactions_ShouldReturnPagedTransactions() throws Exception {
        Transaction transaction = createTestTransaction();
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(transaction), PageRequest.of(0, 10), 1);
        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(transactionPage);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tranId").value("T123"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTransaction_ShouldReturnTransaction_WhenTransactionExists() throws Exception {
        Transaction transaction = createTestTransaction();
        when(transactionRepository.findById("T123")).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/T123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").value("T123"))
                .andExpect(jsonPath("$.tranAmt").value(50.00));
    }

    @Test
    void createTransaction_ShouldCreateTransaction_WhenValidRequest() throws Exception {
        Card card = createTestCard();
        Account account = createTestAccount();
        Transaction transaction = createTestTransaction();
        TransactionCreateRequest request = createTestCreateRequest();

        when(cardRepository.findById("1234567890123456")).thenReturn(Optional.of(card));
        when(accountValidationService.validateCreditLimit(any(), any())).thenReturn(true);
        when(accountValidationService.validateAccountExpiration(any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").value("T123"));
    }

    @Test
    void createTransaction_ShouldRejectTransaction_WhenNotConfirmed() throws Exception {
        TransactionCreateRequest request = createTestCreateRequest();
        request.setConfirmation("N");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(100);
        transaction.setTranAmt(new BigDecimal("50.00"));
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:01:00");
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Store");
        transaction.setTranMerchantCity("Test City");
        transaction.setTranMerchantZip("12345");
        return transaction;
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setAccount(createTestAccount());
        return card;
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctExpiraionDate("2025-12-31");
        return account;
    }

    private TransactionCreateRequest createTestCreateRequest() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setCardNum("1234567890123456");
        request.setTranTypeCd("01");
        request.setTranCatCd("100");
        request.setTranSource("POS");
        request.setTranDesc("Purchase");
        request.setTranAmt(new BigDecimal("50.00"));
        request.setOrigDate("2023-01-01");
        request.setProcDate("2023-01-01");
        request.setMerchantId("12345");
        request.setMerchantName("Test Store");
        request.setMerchantCity("Test City");
        request.setMerchantZip("12345");
        request.setConfirmation("Y");
        return request;
    }
}
