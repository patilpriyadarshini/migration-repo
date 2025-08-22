package com.modernized.utils;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.CardResponse;
import com.modernized.dto.TransactionResponse;
import com.modernized.dto.UserResponse;
import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Customer;
import com.modernized.entities.Transaction;
import com.modernized.entities.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMapperUtilTest {

    @Test
    void mapToAccountResponse_ShouldMapAllFields() {
        Account account = createTestAccount();
        
        AccountResponse response = ResponseMapperUtil.mapToAccountResponse(account);
        
        assertEquals(1L, response.getAcctId());
        assertEquals("Y", response.getAcctActiveStatus());
        assertEquals(new BigDecimal("1000.00"), response.getAcctCurrBal());
        assertEquals("John", response.getCustomerFirstName());
        assertEquals("Doe", response.getCustomerLastName());
    }

    @Test
    void mapToCardResponse_ShouldMapAllFields() {
        Card card = createTestCard();
        
        CardResponse response = ResponseMapperUtil.mapToCardResponse(card);
        
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals(1L, response.getAcctId());
        assertEquals("John Doe", response.getCardName());
        assertEquals("Y", response.getCardStatus());
        assertEquals(12, response.getExpiryMonth());
        assertEquals(2025, response.getExpiryYear());
    }

    @Test
    void mapToTransactionResponse_ShouldMapAllFields() {
        Transaction transaction = createTestTransaction();
        
        TransactionResponse response = ResponseMapperUtil.mapToTransactionResponse(transaction);
        
        assertEquals("T123", response.getTranId());
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals("01", response.getTranTypeCd());
        assertEquals("100", response.getTranCatCd());
        assertEquals(new BigDecimal("50.00"), response.getTranAmt());
    }

    @Test
    void mapToUserResponse_ShouldMapAllFields() {
        User user = createTestUser();
        
        UserResponse response = ResponseMapperUtil.mapToUserResponse(user);
        
        assertEquals("testuser", response.getUserId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("A", response.getUserType());
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

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(1L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        return card;
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(100);
        transaction.setTranSource("POS");
        transaction.setTranDesc("Purchase");
        transaction.setTranAmt(new BigDecimal("50.00"));
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:01:00");
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Store");
        transaction.setTranMerchantCity("Test City");
        transaction.setTranMerchantZip("12345");
        return transaction;
    }

    private User createTestUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("A");
        return user;
    }
}
