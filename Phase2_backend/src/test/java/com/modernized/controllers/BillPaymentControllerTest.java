package com.modernized.controllers;

import com.modernized.dto.BillPaymentRequest;
import com.modernized.dto.BillPaymentResponse;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    void getCurrentBalance_ShouldReturnBalance_WhenAccountExists() throws Exception {
        Account account = createTestAccount();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(any())).thenReturn(true);

        mockMvc.perform(get("/api/bill-payment/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(123456789L))
                .andExpect(jsonPath("$.currentBalance").value(1000.00))
                .andExpect(jsonPath("$.eligible").value(true));
    }

    @Test
    void processBillPayment_ShouldProcessPayment_WhenConfirmed() throws Exception {
        Account account = createTestAccount();
        BillPaymentRequest request = createTestPaymentRequest();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(any())).thenReturn(true);
        when(transactionProcessingService.processBillPayment(any())).thenReturn(new BigDecimal("1000.00"));

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentAmount").value(1000.00))
                .andExpect(jsonPath("$.success").value(true));
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        return account;
    }

    private BillPaymentRequest createTestPaymentRequest() {
        BillPaymentRequest request = new BillPaymentRequest();
        request.setAccountId(123456789L);
        request.setConfirmation("Y");
        return request;
    }
}
