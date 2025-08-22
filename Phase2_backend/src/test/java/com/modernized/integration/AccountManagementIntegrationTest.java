package com.modernized.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.AccountUpdateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void accountManagementFlow_ShouldWork_EndToEnd() throws Exception {
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        customer.setCustDobYyyyMmDd("1990-01-01");
        customer.setCustFicoCreditScore(750);
        customerRepository.save(customer);

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
        account.setCustomer(customer);
        accountRepository.save(account);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(1L))
                .andExpect(jsonPath("$.customerFirstName").value("John"));

        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setAcctActiveStatus("Y");
        updateRequest.setAcctCurrBal(new BigDecimal("1500.00"));
        updateRequest.setAcctCreditLimit(new BigDecimal("6000.00"));
        updateRequest.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        updateRequest.setAcctOpenDate("2023-01-01");
        updateRequest.setAcctExpiraionDate("2025-12-31");
        updateRequest.setAcctCurrCycCredit(new BigDecimal("0.00"));
        updateRequest.setAcctCurrCycDebit(new BigDecimal("0.00"));
        updateRequest.setCustomerFirstName("Jane");
        updateRequest.setCustomerLastName("Doe");

        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctCurrBal").value(1500.00))
                .andExpect(jsonPath("$.customerFirstName").value("Jane"));
    }
}
