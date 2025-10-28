package com.modernized.controllers;

import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends BaseControllerTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountValidationService accountValidationService;

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        Account account = TestDataBuilder.createTestAccount();
        Customer customer = TestDataBuilder.createTestCustomer();
        account.setCustomer(customer);

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(12345678901L))
                .andExpect(jsonPath("$.acctActiveStatus").value("Y"))
                .andExpect(jsonPath("$.customerFirstName").value("John"));
    }

    @Test
    void getAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(99999999999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/99999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_ShouldUpdateAccount_WhenValidRequest() throws Exception {
        Account account = TestDataBuilder.createTestAccount();
        Customer customer = TestDataBuilder.createTestCustomer();
        account.setCustomer(customer);

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String updateRequest = asJsonString(TestDataBuilder.createAccountUpdateRequest());

        mockMvc.perform(put("/api/accounts/12345678901")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(12345678901L));
    }

    @Test
    void updateAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(99999999999L)).thenReturn(Optional.empty());

        String updateRequest = asJsonString(TestDataBuilder.createAccountUpdateRequest());

        mockMvc.perform(put("/api/accounts/99999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isNotFound());
    }
}
