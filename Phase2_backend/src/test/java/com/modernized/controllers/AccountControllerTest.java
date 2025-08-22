package com.modernized.controllers;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.services.AccountValidationService;
import com.modernized.utils.ResponseMapper;
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

    private Account testAccount;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(1001L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);

        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testAccount.setAcctOpenDate("2020-01-15");
        testAccount.setAcctExpirationDate("2027-01-15");
        testAccount.setAcctReissueDate("2023-01-15");
        testAccount.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("1000.00"));
        testAccount.setAcctAddrZip("10001");
        testAccount.setAcctGroupId("GRP001");
        testAccount.setCustomer(testCustomer);
    }

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(testAccount));

        mockMvc.perform(get("/api/accounts/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(12345678901L))
                .andExpect(jsonPath("$.acctActiveStatus").value("Y"))
                .andExpect(jsonPath("$.acctCurrBal").value(1500.00))
                .andExpect(jsonPath("$.customerFirstName").value("John"))
                .andExpect(jsonPath("$.customerLastName").value("Doe"));
    }

    @Test
    void getAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/99999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount_WhenValidRequest() throws Exception {
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setAcctActiveStatus("Y");
        updateRequest.setAcctCurrBal(new BigDecimal("2000.00"));
        updateRequest.setAcctCreditLimit(new BigDecimal("6000.00"));
        updateRequest.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        updateRequest.setAcctOpenDate("2020-01-15");
        updateRequest.setAcctExpirationDate("2027-01-15");
        updateRequest.setAcctReissueDate("2023-01-15");
        updateRequest.setAcctCurrCycCredit(new BigDecimal("3000.00"));
        updateRequest.setAcctCurrCycDebit(new BigDecimal("1000.00"));
        updateRequest.setAcctAddrZip("10001");
        updateRequest.setAcctGroupId("GRP001");
        updateRequest.setCustomerFirstName("Jane");
        updateRequest.setCustomerLastName("Smith");

        Account updatedAccount = new Account();
        updatedAccount.setAcctId(12345678901L);
        updatedAccount.setAcctCurrBal(new BigDecimal("2000.00"));
        updatedAccount.setCustomer(testCustomer);

        when(accountRepository.findById(12345678901L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(put("/api/accounts/12345678901")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(12345678901L));
    }

    @Test
    void updateAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setAcctActiveStatus("Y");
        updateRequest.setAcctCurrBal(new BigDecimal("2000.00"));
        updateRequest.setAcctCreditLimit(new BigDecimal("6000.00"));
        updateRequest.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        updateRequest.setAcctOpenDate("2020-01-15");
        updateRequest.setAcctExpirationDate("2027-01-15");

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/accounts/99999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}
