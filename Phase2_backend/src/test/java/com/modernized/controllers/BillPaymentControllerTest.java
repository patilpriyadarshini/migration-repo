package com.modernized.controllers;

import com.modernized.dto.BillPaymentRequest;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctActiveStatus("Y");
    }

    @Test
    void getCurrentBalance_ShouldReturnBalance_WhenAccountEligible() throws Exception {
        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(testAccount));
        when(accountValidationService.validateBillPaymentEligibility(testAccount)).thenReturn(true);

        mockMvc.perform(get("/api/bill-payment/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678901L))
                .andExpect(jsonPath("$.currentBalance").value(1500.00))
                .andExpect(jsonPath("$.paymentAmount").value(1500.00))
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.message").value("Ready for payment confirmation"));
    }

    @Test
    void getCurrentBalance_ShouldReturnNotEligible_WhenAccountNotEligible() throws Exception {
        testAccount.setAcctCurrBal(BigDecimal.ZERO);
        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(testAccount));
        when(accountValidationService.validateBillPaymentEligibility(testAccount)).thenReturn(false);

        mockMvc.perform(get("/api/bill-payment/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678901L))
                .andExpect(jsonPath("$.eligible").value(false))
                .andExpect(jsonPath("$.message").value("You have nothing to pay..."));
    }

    @Test
    void getCurrentBalance_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bill-payment/99999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void processBillPayment_ShouldProcessPayment_WhenConfirmedAndEligible() throws Exception {
        BillPaymentRequest paymentRequest = new BillPaymentRequest();
        paymentRequest.setAccountId(12345678901L);
        paymentRequest.setConfirmation("Y");

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(testAccount));
        when(accountValidationService.validateBillPaymentEligibility(testAccount)).thenReturn(true);
        when(transactionProcessingService.processBillPayment(testAccount)).thenReturn(new BigDecimal("1500.00"));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678901L))
                .andExpect(jsonPath("$.paymentAmount").value(1500.00))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment processed successfully"));
    }

    @Test
    void processBillPayment_ShouldReturnCancelled_WhenNotConfirmed() throws Exception {
        BillPaymentRequest paymentRequest = new BillPaymentRequest();
        paymentRequest.setAccountId(12345678901L);
        paymentRequest.setConfirmation("N");

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678901L))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Payment cancelled"));
    }
}
