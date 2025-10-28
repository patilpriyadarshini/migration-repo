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
import com.modernized.utils.EntityMapper;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    private Transaction testTransaction;
    private TransactionResponse testTransactionResponse;
    private TransactionCreateRequest testCreateRequest;
    private Card testCard;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setTranId("T123456");
        testTransaction.setTranCardNum("1234567890123456");
        testTransaction.setTranTypeCd("01");
        testTransaction.setTranCatCd(1);
        testTransaction.setTranAmt(new BigDecimal("100.00"));
        testTransaction.setTranDesc("Test Transaction");

        testTransactionResponse = new TransactionResponse();
        testTransactionResponse.setTranId("T123456");
        testTransactionResponse.setCardNum("1234567890123456");
        testTransactionResponse.setTranTypeCd("01");
        testTransactionResponse.setTranCatCd("1");
        testTransactionResponse.setTranAmt(new BigDecimal("100.00"));

        testCreateRequest = new TransactionCreateRequest();
        testCreateRequest.setCardNum("1234567890123456");
        testCreateRequest.setTranTypeCd("01");
        testCreateRequest.setTranCatCd("1");
        testCreateRequest.setTranAmt(new BigDecimal("100.00"));
        testCreateRequest.setOrigDate("2023-01-01");
        testCreateRequest.setProcDate("2023-01-01");
        testCreateRequest.setMerchantName("Test Merchant");
        testCreateRequest.setMerchantCity("Test City");
        testCreateRequest.setMerchantZip("12345");
        testCreateRequest.setConfirmation("Y");
        testCreateRequest.setTranDesc("Test Transaction");
        testCreateRequest.setConfirmation("Y");
        testCreateRequest.setOrigDate("2024-01-15");
        testCreateRequest.setProcDate("2024-01-15");
        testCreateRequest.setMerchantId("12345");
        testCreateRequest.setMerchantName("Test Merchant");

        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));

        testCard = new Card();
        testCard.setCardNum("1234567890123456");
        testCard.setAccount(testAccount);
    }

    @Test
    void getTransactions_NoFilters_ReturnsPagedResponse() throws Exception {
        List<Transaction> transactions = Arrays.asList(testTransaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);
        
        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(transactionPage);
        when(entityMapper.mapToTransactionResponse(any(Transaction.class))).thenReturn(testTransactionResponse);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].tranId").value("T123456"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(transactionRepository).findAll(any(PageRequest.class));
        verify(entityMapper).mapToTransactionResponse(testTransaction);
    }

    @Test
    void getTransaction_ValidId_ReturnsTransactionResponse() throws Exception {
        when(transactionRepository.findById(anyString())).thenReturn(Optional.of(testTransaction));
        when(entityMapper.mapToTransactionResponse(any(Transaction.class))).thenReturn(testTransactionResponse);

        mockMvc.perform(get("/api/transactions/T123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tranId").value("T123456"))
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"))
                .andExpect(jsonPath("$.tranAmt").value(100.00));

        verify(transactionRepository).findById("T123456");
        verify(entityMapper).mapToTransactionResponse(testTransaction);
    }

    @Test
    void getTransaction_InvalidId_ThrowsEntityNotFoundException() throws Exception {
        when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/INVALID"))
                .andExpect(status().isNotFound());

        verify(transactionRepository).findById("INVALID");
        verify(entityMapper, never()).mapToTransactionResponse(any());
    }

    @Test
    void createTransaction_ValidRequest_ReturnsCreatedTransaction() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.of(testCard));
        when(accountValidationService.validateCreditLimit(any(), any())).thenReturn(true);
        when(accountValidationService.validateAccountExpiration(any(), any())).thenReturn(true);
        when(transactionProcessingService.processTransaction(any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(entityMapper.mapToTransactionResponse(any(Transaction.class))).thenReturn(testTransactionResponse);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tranId").value("T123456"));

        verify(cardRepository).findById("1234567890123456");
        verify(accountValidationService).validateCreditLimit(testAccount, new BigDecimal("100.00"));
        verify(accountValidationService).validateAccountExpiration(eq(testAccount), anyString());
        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(testAccount);
    }

    @Test
    void createTransaction_NoConfirmation_ThrowsIllegalArgumentException() throws Exception {
        testCreateRequest.setConfirmation("N");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(cardRepository, never()).findById(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_ExceedsCreditLimit_ThrowsIllegalArgumentException() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.of(testCard));
        when(accountValidationService.validateCreditLimit(any(), any())).thenReturn(false);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(cardRepository).findById("1234567890123456");
        verify(accountValidationService).validateCreditLimit(testAccount, new BigDecimal("100.00"));
        verify(transactionRepository, never()).save(any());
    }
}
