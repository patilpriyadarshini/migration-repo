package com.modernized.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.TransactionCreateRequest;
import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Customer;
import com.modernized.repositories.AccountRepository;
import com.modernized.repositories.CardRepository;
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
class TransactionProcessingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transactionProcessingFlow_ShouldWork_EndToEnd() throws Exception {
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

        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(1L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        card.setAccount(account);
        cardRepository.save(card);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setCardNum("1234567890123456");
        createRequest.setTranTypeCd("01");
        createRequest.setTranCatCd("100");
        createRequest.setTranSource("POS");
        createRequest.setTranDesc("Test Purchase");
        createRequest.setTranAmt(new BigDecimal("50.00"));
        createRequest.setOrigDate("2023-01-01");
        createRequest.setProcDate("2023-01-01");
        createRequest.setMerchantId("12345");
        createRequest.setMerchantName("Test Store");
        createRequest.setMerchantCity("Test City");
        createRequest.setMerchantZip("12345");
        createRequest.setConfirmation("Y");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tranAmt").value(50.00))
                .andExpect(jsonPath("$.tranDesc").value("Test Purchase"));
    }
}
