package com.modernized.controllers;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.services.TransactionProcessingService;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillPaymentController.class)
class BillPaymentControllerTest extends BaseControllerTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountValidationService accountValidationService;

    @MockBean
    private TransactionProcessingService transactionProcessingService;

    @Test
    void getCurrentBalance_ShouldReturnBalance_WhenAccountExists() throws Exception {
        Account account = TestDataBuilder.createTestAccount();

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(account)).thenReturn(true);

        mockMvc.perform(get("/api/bill-payment/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678901L))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void processBillPayment_ShouldProcessPayment_WhenValidRequest() throws Exception {
        Account account = TestDataBuilder.createTestAccount();

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(account));
        when(accountValidationService.validateBillPaymentEligibility(account)).thenReturn(true);
        when(transactionProcessingService.processBillPayment(account)).thenReturn(new BigDecimal("1500.00"));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String paymentRequest = asJsonString(TestDataBuilder.createBillPaymentRequest());

        mockMvc.perform(post("/api/bill-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment processed successfully"));
    }
}
