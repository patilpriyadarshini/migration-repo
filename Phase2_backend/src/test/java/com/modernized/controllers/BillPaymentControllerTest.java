package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.BillPaymentRequest;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillPaymentController.class)
class BillPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountValidationService accountValidationService;

    @MockBean
    private TransactionProcessingService transactionProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCurrentBalance_ShouldReturnBalance_WhenAccountEligible() throws Exception {
        Account account = createTestAccount();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(account)).thenReturn(true);

        mockMvc.perform(get("/api/bill-payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getCurrentBalance_ShouldReturnNotEligible_WhenAccountNotEligible() throws Exception {
        Account account = createTestAccount();
        account.setAcctCurrBal(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(account)).thenReturn(false);

        mockMvc.perform(get("/api/bill-payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You have nothing to pay..."));
    }

    @Test
    void processBillPayment_ShouldProcessPayment_WhenConfirmed() throws Exception {
        Account account = createTestAccount();
        BillPaymentRequest request = new BillPaymentRequest();
        request.setAccountId(1L);
        request.setConfirmation("Y");
        request.setPaymentAmount(new BigDecimal("500.00"));
        request.setPaymentDate("2023-01-01");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(account)).thenReturn(true);
        when(transactionProcessingService.processBillPayment(account)).thenReturn(new BigDecimal("500.00"));

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment processed successfully"));
    }

    @Test
    void processBillPayment_ShouldCancelPayment_WhenNotConfirmed() throws Exception {
        BillPaymentRequest request = new BillPaymentRequest();
        request.setAccountId(1L);
        request.setConfirmation("N");
        request.setPaymentAmount(new BigDecimal("500.00"));
        request.setPaymentDate("2023-01-01");

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Payment cancelled"));
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctCurrBal(new BigDecimal("500.00"));
        return account;
    }
}
