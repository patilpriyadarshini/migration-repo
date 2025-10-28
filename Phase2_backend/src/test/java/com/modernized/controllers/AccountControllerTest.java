package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.AccountResponse;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
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

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountValidationService accountValidationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        Account account = createTestAccount();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(1L))
                .andExpect(jsonPath("$.acctActiveStatus").value("Y"));
    }

    @Test
    void getAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_ShouldUpdateAndReturnAccount_WhenValidRequest() throws Exception {
        Account account = createTestAccount();
        AccountUpdateRequest updateRequest = createTestUpdateRequest();
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(1L));
    }

    @Test
    void updateAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        AccountUpdateRequest updateRequest = createTestUpdateRequest();
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(1L);
        account.setAcctActiveStatus("Y");
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        account.setAcctOpenDate("2023-01-01");
        account.setAcctExpiraionDate("2025-12-31");
        account.setAcctCurrCycCredit(new BigDecimal("0.00"));
        account.setAcctCurrCycDebit(new BigDecimal("0.00"));
        
        Customer customer = new Customer();
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        account.setCustomer(customer);
        
        return account;
    }

    private AccountUpdateRequest createTestUpdateRequest() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAcctActiveStatus("Y");
        request.setAcctCurrBal(new BigDecimal("1500.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        request.setAcctOpenDate("2023-01-01");
        request.setAcctExpiraionDate("2025-12-31");
        request.setAcctCurrCycCredit(new BigDecimal("0.00"));
        request.setAcctCurrCycDebit(new BigDecimal("0.00"));
        request.setCustomerFirstName("Jane");
        request.setCustomerLastName("Doe");
        request.setCustomerSsn("987-65-4321");
        return request;
    }
}
