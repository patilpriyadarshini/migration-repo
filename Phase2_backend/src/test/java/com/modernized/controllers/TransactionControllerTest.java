package com.modernized.controllers;

import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Transaction;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.CardRepository;
import com.modernized.repositories.TransactionRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest extends BaseControllerTest {

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

    @Test
    void getTransactions_ShouldReturnPagedTransactions() throws Exception {
        Transaction transaction = TestDataBuilder.createTestTransaction();
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(transaction), PageRequest.of(0, 10), 1);

        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(transactionPage);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tranId").value("T123456789"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTransaction_ShouldReturnTransaction_WhenExists() throws Exception {
        Transaction transaction = TestDataBuilder.createTestTransaction();

        when(transactionRepository.findById("T123456789")).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/T123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").value("T123456789"))
                .andExpect(jsonPath("$.tranDesc").value("Test Purchase"));
    }

    @Test
    void createTransaction_ShouldCreateTransaction_WhenValidRequest() throws Exception {
        Card card = TestDataBuilder.createTestCard();
        Account account = TestDataBuilder.createTestAccount();
        card.setAccount(account);

        when(cardRepository.findById("4000123456789012")).thenReturn(Optional.of(card));
        when(accountValidationService.validateCreditLimit(any(), any())).thenReturn(true);
        when(accountValidationService.validateAccountExpiration(any(), any())).thenReturn(true);
        when(transactionProcessingService.processTransaction(any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(TestDataBuilder.createTestTransaction());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String createRequest = asJsonString(TestDataBuilder.createTransactionCreateRequest());

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranId").exists());
    }
}
