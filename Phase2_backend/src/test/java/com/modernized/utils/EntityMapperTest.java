package com.modernized.utils;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.CardResponse;
import com.modernized.dto.TransactionResponse;
import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Customer;
import com.modernized.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    private EntityMapper entityMapper;

    @BeforeEach
    void setUp() {
        entityMapper = new EntityMapper();
    }

    @Test
    void mapToAccountResponse_ShouldMapAllFields() {
        Account account = createTestAccount();
        
        AccountResponse response = entityMapper.mapToAccountResponse(account);
        
        assertEquals(123456789L, response.getAcctId());
        assertEquals(new BigDecimal("1000.00"), response.getAcctCurrBal());
        assertEquals("John", response.getCustomerFirstName());
        assertEquals("Doe", response.getCustomerLastName());
    }

    @Test
    void mapToCardResponse_ShouldMapAllFields() {
        Card card = createTestCard();
        
        CardResponse response = entityMapper.mapToCardResponse(card);
        
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals(123456789L, response.getAcctId());
        assertEquals("JOHN DOE", response.getCardName());
        assertEquals(12, response.getExpiryMonth());
        assertEquals(2025, response.getExpiryYear());
    }

    @Test
    void mapToTransactionResponse_ShouldMapAllFields() {
        Transaction transaction = createTestTransaction();
        
        TransactionResponse response = entityMapper.mapToTransactionResponse(transaction);
        
        assertEquals("T123", response.getTranId());
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals(new BigDecimal("100.00"), response.getTranAmt());
        assertEquals("Test Transaction", response.getTranDesc());
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctActiveStatus("Y");
        
        Customer customer = new Customer();
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        account.setCustomer(customer);
        
        return account;
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        return card;
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranAmt(new BigDecimal("100.00"));
        transaction.setTranDesc("Test Transaction");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(1);
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Merchant");
        return transaction;
    }
}
