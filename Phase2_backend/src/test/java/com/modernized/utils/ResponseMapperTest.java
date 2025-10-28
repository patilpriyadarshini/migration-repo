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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMapperTest {

    private Account testAccount;
    private Customer testCustomer;
    private Card testCard;
    private Transaction testTransaction;
    private User testUser;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        testCustomer.setCustDobYyyyMmDd("1985-03-15");
        testCustomer.setCustFicoCreditScore(750);
        testCustomer.setCustPhoneNum1("555-0101");
        testCustomer.setCustPhoneNum2("555-0102");
        testCustomer.setCustAddrLine1("123 Main St");
        testCustomer.setCustAddrLine2("Apt 4B");
        testCustomer.setCustAddrLine3("New York");
        testCustomer.setCustAddrStateCd("NY");
        testCustomer.setCustAddrZip("10001");
        testCustomer.setCustAddrCountryCd("USA");
        testCustomer.setCustGovtIssuedId("DL123456");
        testCustomer.setCustEftAccountId("EFT789");
        testCustomer.setCustPriCardHolderInd("Y");

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

        testCard = new Card();
        testCard.setCardNum("4111111111111111");
        testCard.setCardAcctId(12345678901L);
        testCard.setCardEmbossedName("John Doe");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpirationDate("12/2025");

        testTransaction = new Transaction();
        testTransaction.setTranId("T123456789");
        testTransaction.setTranCardNum("4111111111111111");
        testTransaction.setTranTypeCd("01");
        testTransaction.setTranCatCd(1);
        testTransaction.setTranSource("POS");
        testTransaction.setTranDesc("Purchase");
        testTransaction.setTranAmt(new BigDecimal("100.00"));
        testTransaction.setTranOrigTs("2023-01-15 10:30:00");
        testTransaction.setTranProcTs("2023-01-15 10:30:00");
        testTransaction.setTranMerchantId(12345L);
        testTransaction.setTranMerchantName("Test Store");
        testTransaction.setTranMerchantCity("New York");
        testTransaction.setTranMerchantZip("10001");

        testUser = new User();
        testUser.setSecUsrId("testuser");
        testUser.setSecUsrFname("John");
        testUser.setSecUsrLname("Doe");
        testUser.setSecUsrType("R");
    }

    @Test
    void mapToAccountResponse_ShouldMapAllFields_WhenValidAccount() {
        AccountResponse result = ResponseMapper.mapToAccountResponse(testAccount);
        
        assertNotNull(result);
        assertEquals(12345678901L, result.getAcctId());
        assertEquals("Y", result.getAcctActiveStatus());
        assertEquals(new BigDecimal("1500.00"), result.getAcctCurrBal());
        assertEquals(new BigDecimal("5000.00"), result.getAcctCreditLimit());
        assertEquals("2027-01-15", result.getAcctExpirationDate());
        assertEquals("John", result.getCustomerFirstName());
        assertEquals("Doe", result.getCustomerLastName());
        assertEquals("123456789", result.getCustomerSsn());
    }

    @Test
    void mapToAccountResponse_ShouldReturnNull_WhenAccountIsNull() {
        AccountResponse result = ResponseMapper.mapToAccountResponse(null);
        
        assertNull(result);
    }

    @Test
    void mapToAccountResponse_ShouldHandleNullCustomer_WhenCustomerIsNull() {
        testAccount.setCustomer(null);
        
        AccountResponse result = ResponseMapper.mapToAccountResponse(testAccount);
        
        assertNotNull(result);
        assertEquals(12345678901L, result.getAcctId());
        assertNull(result.getCustomerFirstName());
        assertNull(result.getCustomerLastName());
    }

    @Test
    void mapToCardResponse_ShouldMapAllFields_WhenValidCard() {
        CardResponse result = ResponseMapper.mapToCardResponse(testCard);
        
        assertNotNull(result);
        assertEquals("4111111111111111", result.getCardNum());
        assertEquals(12345678901L, result.getAcctId());
        assertEquals("John Doe", result.getCardName());
        assertEquals("Y", result.getCardStatus());
        assertEquals(12, result.getExpiryMonth());
        assertEquals(2025, result.getExpiryYear());
    }

    @Test
    void mapToCardResponse_ShouldReturnNull_WhenCardIsNull() {
        CardResponse result = ResponseMapper.mapToCardResponse(null);
        
        assertNull(result);
    }

    @Test
    void mapToTransactionResponse_ShouldMapAllFields_WhenValidTransaction() {
        TransactionResponse result = ResponseMapper.mapToTransactionResponse(testTransaction);
        
        assertNotNull(result);
        assertEquals("T123456789", result.getTranId());
        assertEquals("4111111111111111", result.getCardNum());
        assertEquals("01", result.getTranTypeCd());
        assertEquals("1", result.getTranCatCd());
        assertEquals("POS", result.getTranSource());
        assertEquals("Purchase", result.getTranDesc());
        assertEquals(new BigDecimal("100.00"), result.getTranAmt());
        assertEquals("12345", result.getMerchantId());
        assertEquals("Test Store", result.getMerchantName());
    }

    @Test
    void mapToTransactionResponse_ShouldReturnNull_WhenTransactionIsNull() {
        TransactionResponse result = ResponseMapper.mapToTransactionResponse(null);
        
        assertNull(result);
    }

    @Test
    void mapToUserResponse_ShouldMapAllFields_WhenValidUser() {
        UserResponse result = ResponseMapper.mapToUserResponse(testUser);
        
        assertNotNull(result);
        assertEquals("testuser", result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("R", result.getUserType());
    }

    @Test
    void mapToUserResponse_ShouldReturnNull_WhenUserIsNull() {
        UserResponse result = ResponseMapper.mapToUserResponse(null);
        
        assertNull(result);
    }
}
