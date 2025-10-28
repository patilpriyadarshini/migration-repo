package com.modernized.controllers;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.utils.EntityMapper;
import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountValidationService accountValidationService;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private Customer testCustomer;
    private AccountResponse testAccountResponse;
    private AccountUpdateRequest testUpdateRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(1L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);

        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setCustomer(testCustomer);

        testAccountResponse = new AccountResponse();
        testAccountResponse.setAcctId(123456789L);
        testAccountResponse.setAcctActiveStatus("Y");
        testAccountResponse.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccountResponse.setCustomerFirstName("John");
        testAccountResponse.setCustomerLastName("Doe");

        testUpdateRequest = new AccountUpdateRequest();
        testUpdateRequest.setAcctActiveStatus("Y");
        testUpdateRequest.setAcctCurrBal(new BigDecimal("1500.00"));
        testUpdateRequest.setAcctCreditLimit(new BigDecimal("6000.00"));
        testUpdateRequest.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testUpdateRequest.setAcctOpenDate("2023-01-01");
        testUpdateRequest.setAcctExpiraionDate("2025-12-31");
        testUpdateRequest.setAcctCurrCycCredit(new BigDecimal("0.00"));
        testUpdateRequest.setAcctCurrCycDebit(new BigDecimal("0.00"));
        testUpdateRequest.setCustomerFirstName("Jane");
        testUpdateRequest.setCustomerLastName("Smith");
        testUpdateRequest.setCustomerSsn("123-45-6789");
    }

    @Test
    void getAccount_ValidId_ReturnsAccountResponse() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        when(entityMapper.mapToAccountResponse(any(Account.class))).thenReturn(testAccountResponse);

        mockMvc.perform(get("/api/accounts/123456789"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.acctId").value(123456789L))
                .andExpect(jsonPath("$.acctActiveStatus").value("Y"))
                .andExpect(jsonPath("$.acctCurrBal").value(1000.00))
                .andExpect(jsonPath("$.customerFirstName").value("John"))
                .andExpect(jsonPath("$.customerLastName").value("Doe"));

        verify(accountRepository).findById(123456789L);
        verify(entityMapper).mapToAccountResponse(testAccount);
    }

    @Test
    void getAccount_InvalidId_ThrowsEntityNotFoundException() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/999999999"))
                .andExpect(status().isNotFound());

        verify(accountRepository).findById(999999999L);
        verify(entityMapper, never()).mapToAccountResponse(any());
    }

    @Test
    void updateAccount_ValidRequest_ReturnsUpdatedAccount() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(entityMapper.mapToAccountResponse(any(Account.class))).thenReturn(testAccountResponse);
        doNothing().when(entityMapper).updateAccountFromRequest(any(Account.class), any(AccountUpdateRequest.class));

        mockMvc.perform(put("/api/accounts/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.acctId").value(123456789L));

        verify(accountRepository).findById(123456789L);
        verify(entityMapper).updateAccountFromRequest(eq(testAccount), any(AccountUpdateRequest.class));
        verify(accountRepository).save(testAccount);
        verify(entityMapper).mapToAccountResponse(testAccount);
    }

    @Test
    void updateAccount_InvalidId_ThrowsEntityNotFoundException() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/accounts/999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(accountRepository).findById(999999999L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateAccount_InvalidRequestBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/accounts/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        verify(accountRepository, never()).findById(any());
        verify(accountRepository, never()).save(any());
    }
}
