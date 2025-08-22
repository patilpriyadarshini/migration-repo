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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction testTransaction;
    private Card testCard;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));

        testCard = new Card();
        testCard.setCardNum("4111111111111111");
        testCard.setCardAcctId(12345678901L);
        testCard.setAccount(testAccount);

        testTransaction = new Transaction();
        testTransaction.setTranId("T123456789");
        testTransaction.setTranCardNum("4111111111111111");
        testTransaction.setTranTypeCd("01");
        testTransaction.setTranCatCd(1);
        testTransaction.setTranSource("POS");
        testTransaction.setTranDesc("Purchase");
        testTransaction.setTranAmt(new BigDecimal("100.00"));
        testTransaction.setTranOrigTs("2023-01-15 10:30:00");
        testTransaction.setTranProcTs("2023-01-15 10:30:00");
        testTransaction.setTranMerchantId(12345L);
        testTransaction.setTranMerchantName("Test Store");
        testTransaction.setTranMerchantCity("New York");
        testTransaction.setTranMerchantZip("10001");
    }

    @Test
    void getTransactions_ShouldReturnPagedTransactions_WhenNoFilters() throws Exception {
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(testTransaction), PageRequest.of(0, 10), 1);
        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(transactionPage);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].tranId").value("T123456789"))
                .andExpect(jsonPath("$.content[0].tranDesc").value("Purchase"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTransaction_ShouldReturnTransaction_WhenTransactionExists() throws Exception {
        when(transactionRepository.findById("T123456789")).thenReturn(Optional.of(testTransaction));

        mockMvc.perform(get("/api/transactions/T123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").value("T123456789"))
                .andExpect(jsonPath("$.tranDesc").value("Purchase"))
                .andExpect(jsonPath("$.tranAmt").value(100.00));
    }

    @Test
    void getTransaction_ShouldReturnNotFound_WhenTransactionDoesNotExist() throws Exception {
        when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/T999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction_WhenValidRequest() throws Exception {
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setCardNum("4111111111111111");
        createRequest.setTranTypeCd("01");
        createRequest.setTranCatCd("1");
        createRequest.setTranSource("POS");
        createRequest.setTranDesc("Purchase");
        createRequest.setTranAmt(new BigDecimal("100.00"));
        createRequest.setOrigDate("2023-01-15");
        createRequest.setProcDate("2023-01-15");
        createRequest.setMerchantId("12345");
        createRequest.setMerchantName("Test Store");
        createRequest.setMerchantCity("New York");
        createRequest.setMerchantZip("10001");
        createRequest.setConfirmation("Y");

        when(cardRepository.findById("4111111111111111")).thenReturn(Optional.of(testCard));
        when(accountValidationService.validateCreditLimit(any(Account.class), any(BigDecimal.class))).thenReturn(true);
        when(accountValidationService.validateAccountExpiration(any(Account.class), anyString())).thenReturn(true);
        when(transactionProcessingService.processTransaction(any(Account.class), any(Transaction.class))).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranDesc").value("Purchase"))
                .andExpect(jsonPath("$.tranAmt").value(100.00));
    }

    @Test
    void createTransaction_ShouldReturnBadRequest_WhenNoCardOrAccount() throws Exception {
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setTranTypeCd("01");
        createRequest.setTranCatCd("1");
        createRequest.setTranSource("POS");
        createRequest.setTranDesc("Purchase");
        createRequest.setTranAmt(new BigDecimal("100.00"));
        createRequest.setConfirmation("Y");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_ShouldReturnBadRequest_WhenNotConfirmed() throws Exception {
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setCardNum("4111111111111111");
        createRequest.setTranTypeCd("01");
        createRequest.setTranCatCd("1");
        createRequest.setTranSource("POS");
        createRequest.setTranDesc("Purchase");
        createRequest.setTranAmt(new BigDecimal("100.00"));
        createRequest.setConfirmation("N");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }
}
