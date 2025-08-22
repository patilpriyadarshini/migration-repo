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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        Account account = createTestAccount();
        AccountResponse response = createTestAccountResponse();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(entityMapper.mapToAccountResponse(any(Account.class))).thenReturn(response);

        mockMvc.perform(get("/api/accounts/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(123456789L))
                .andExpect(jsonPath("$.acctCurrBal").value(1000.00));
    }

    @Test
    void getAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount_WhenValidRequest() throws Exception {
        Account account = createTestAccount();
        AccountResponse response = createTestAccountResponse();
        AccountUpdateRequest updateRequest = createTestUpdateRequest();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(entityMapper.mapToAccountResponse(any(Account.class))).thenReturn(response);

        mockMvc.perform(put("/api/accounts/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(123456789L));
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctActiveStatus("Y");
        
        Customer customer = new Customer();
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        account.setCustomer(customer);
        
        return account;
    }

    private AccountResponse createTestAccountResponse() {
        AccountResponse response = new AccountResponse();
        response.setAcctId(123456789L);
        response.setAcctCurrBal(new BigDecimal("1000.00"));
        response.setCustomerFirstName("John");
        response.setCustomerLastName("Doe");
        return response;
    }

    private AccountUpdateRequest createTestUpdateRequest() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAcctCurrBal(new BigDecimal("1500.00"));
        request.setCustomerFirstName("John");
        request.setCustomerLastName("Smith");
        return request;
    }
}
