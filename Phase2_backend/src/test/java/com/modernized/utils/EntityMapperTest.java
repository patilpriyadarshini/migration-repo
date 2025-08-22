package com.modernized.utils;

import com.modernized.dto.*;
import com.modernized.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    private EntityMapper entityMapper;
    private Account testAccount;
    private Customer testCustomer;
    private Card testCard;
    private Transaction testTransaction;
    private User testUser;

    @BeforeEach
    void setUp() {
        entityMapper = new EntityMapper();

        testCustomer = new Customer();
        testCustomer.setCustId(1L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        testCustomer.setCustDobYyyyMmDd("1990-01-01");
        testCustomer.setCustFicoCreditScore(750);

        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setCustomer(testCustomer);

        testCard = new Card();
        testCard.setCardNum("1234567890123456");
        testCard.setCardAcctId(123456789L);
        testCard.setCardEmbossedName("JOHN DOE");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpiraionDate("12/2025");

        testTransaction = new Transaction();
        testTransaction.setTranId("T123456");
        testTransaction.setTranCardNum("1234567890123456");
        testTransaction.setTranTypeCd("01");
        testTransaction.setTranCatCd(1);
        testTransaction.setTranAmt(new BigDecimal("100.00"));

        testUser = new User();
        testUser.setSecUsrId("user123");
        testUser.setSecUsrFname("John");
        testUser.setSecUsrLname("Doe");
        testUser.setSecUsrType("ADMIN");
    }

    @Test
    void mapToAccountResponse_ValidAccount_ReturnsCorrectResponse() {
        AccountResponse response = entityMapper.mapToAccountResponse(testAccount);

        assertNotNull(response);
        assertEquals(123456789L, response.getAcctId());
        assertEquals("Y", response.getAcctActiveStatus());
        assertEquals(new BigDecimal("1000.00"), response.getAcctCurrBal());
        assertEquals(new BigDecimal("5000.00"), response.getAcctCreditLimit());
        assertEquals("John", response.getCustomerFirstName());
        assertEquals("Doe", response.getCustomerLastName());
        assertEquals("123456789", response.getCustomerSsn());
        assertEquals("1990-01-01", response.getCustomerDateOfBirth());
        assertEquals(750, response.getCustomerFicoScore());
    }

    @Test
    void mapToAccountResponse_NullAccount_ReturnsNull() {
        AccountResponse response = entityMapper.mapToAccountResponse(null);

        assertNull(response);
    }

    @Test
    void mapToAccountResponse_AccountWithoutCustomer_ReturnsResponseWithoutCustomerData() {
        testAccount.setCustomer(null);

        AccountResponse response = entityMapper.mapToAccountResponse(testAccount);

        assertNotNull(response);
        assertEquals(123456789L, response.getAcctId());
        assertNull(response.getCustomerFirstName());
        assertNull(response.getCustomerLastName());
    }

    @Test
    void mapToCardResponse_ValidCard_ReturnsCorrectResponse() {
        CardResponse response = entityMapper.mapToCardResponse(testCard);

        assertNotNull(response);
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals(123456789L, response.getAcctId());
        assertEquals("JOHN DOE", response.getCardName());
        assertEquals("Y", response.getCardStatus());
        assertEquals(12, response.getExpiryMonth());
        assertEquals(2025, response.getExpiryYear());
    }

    @Test
    void mapToCardResponse_NullCard_ReturnsNull() {
        CardResponse response = entityMapper.mapToCardResponse(null);

        assertNull(response);
    }

    @Test
    void mapToCardResponse_CardWithInvalidExpiryDate_HandlesGracefully() {
        testCard.setCardExpiraionDate("invalid");

        CardResponse response = entityMapper.mapToCardResponse(testCard);

        assertNotNull(response);
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals(1, response.getExpiryMonth());
        assertEquals(1970, response.getExpiryYear());
    }

    @Test
    void mapToTransactionResponse_ValidTransaction_ReturnsCorrectResponse() {
        TransactionResponse response = entityMapper.mapToTransactionResponse(testTransaction);

        assertNotNull(response);
        assertEquals("T123456", response.getTranId());
        assertEquals("1234567890123456", response.getCardNum());
        assertEquals("01", response.getTranTypeCd());
        assertEquals("1", response.getTranCatCd());
        assertEquals(new BigDecimal("100.00"), response.getTranAmt());
    }

    @Test
    void mapToTransactionResponse_NullTransaction_ReturnsNull() {
        TransactionResponse response = entityMapper.mapToTransactionResponse(null);

        assertNull(response);
    }

    @Test
    void mapToUserResponse_ValidUser_ReturnsCorrectResponse() {
        UserResponse response = entityMapper.mapToUserResponse(testUser);

        assertNotNull(response);
        assertEquals("user123", response.getUserId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("ADMIN", response.getUserType());
    }

    @Test
    void mapToUserResponse_NullUser_ReturnsNull() {
        UserResponse response = entityMapper.mapToUserResponse(null);

        assertNull(response);
    }

    @Test
    void updateAccountFromRequest_ValidInputs_UpdatesAccount() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAcctActiveStatus("N");
        request.setAcctCurrBal(new BigDecimal("1500.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setCustomerFirstName("Jane");
        request.setCustomerLastName("Smith");
        request.setCustomerSsn("987-65-4321");

        entityMapper.updateAccountFromRequest(testAccount, request);

        assertEquals("N", testAccount.getAcctActiveStatus());
        assertEquals(new BigDecimal("1500.00"), testAccount.getAcctCurrBal());
        assertEquals(new BigDecimal("6000.00"), testAccount.getAcctCreditLimit());
        assertEquals("Jane", testAccount.getCustomer().getCustFirstName());
        assertEquals("Smith", testAccount.getCustomer().getCustLastName());
        assertEquals(987654321L, testAccount.getCustomer().getCustSsn());
    }

    @Test
    void updateAccountFromRequest_NullInputs_DoesNothing() {
        Account originalAccount = new Account();
        originalAccount.setAcctId(testAccount.getAcctId());

        entityMapper.updateAccountFromRequest(null, new AccountUpdateRequest());
        entityMapper.updateAccountFromRequest(testAccount, null);

        assertEquals(testAccount.getAcctId(), originalAccount.getAcctId());
    }

    @Test
    void updateCardFromRequest_ValidInputs_UpdatesCard() {
        CardUpdateRequest request = new CardUpdateRequest();
        request.setCardName("JANE SMITH");
        request.setCardStatus("N");
        request.setExpiryMonth(6);
        request.setExpiryYear(2026);

        entityMapper.updateCardFromRequest(testCard, request);

        assertEquals("JANE SMITH", testCard.getCardEmbossedName());
        assertEquals("N", testCard.getCardActiveStatus());
        assertEquals("6/2026", testCard.getCardExpiraionDate());
    }

    @Test
    void updateCardFromRequest_NullInputs_DoesNothing() {
        String originalName = testCard.getCardEmbossedName();

        entityMapper.updateCardFromRequest(null, new CardUpdateRequest());
        entityMapper.updateCardFromRequest(testCard, null);

        assertEquals(originalName, testCard.getCardEmbossedName());
    }
}
