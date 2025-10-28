package com.modernized.utils;

import com.modernized.entities.*;
import com.modernized.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestDataBuilder {

    public static Account createTestAccount() {
        return new Account(
            12345678901L,
            "Y",
            new BigDecimal("1500.00"),
            new BigDecimal("5000.00"),
            new BigDecimal("1000.00"),
            "2023-01-15",
            "2025-12-31",
            "2024-01-15",
            new BigDecimal("2000.00"),
            new BigDecimal("500.00"),
            "12345",
            "GRP001"
        );
    }

    public static Customer createTestCustomer() {
        return new Customer(
            1001L,
            "John",
            "M",
            "Doe",
            "123 Main St",
            "Apt 4B",
            "New York",
            "NY",
            "USA",
            "10001",
            "555-1234",
            "555-5678",
            123456789L,
            "DL123456789",
            "1985-06-15",
            "EFT123456",
            "Y",
            750
        );
    }

    public static Card createTestCard() {
        return new Card(
            "4000123456789012",
            12345678901L,
            123,
            "JOHN DOE",
            "12/2025",
            "Y"
        );
    }

    public static Transaction createTestTransaction() {
        return new Transaction(
            "T123456789",
            "01",
            1,
            "ONLINE",
            "Test Purchase",
            new BigDecimal("100.00"),
            9876543L,
            "Test Merchant",
            "Test City",
            "12345",
            "4000123456789012",
            "2024-01-15 10:30:00",
            "2024-01-15 10:30:00"
        );
    }

    public static User createTestUser() {
        User user = new User();
        user.setSecUsrId("TESTUSER");
        user.setSecUsrFname("Test");
        user.setSecUsrLname("User");
        user.setSecUsrPwd("password");
        user.setSecUsrType("U");
        return user;
    }

    public static LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUserId("TESTUSER");
        request.setPassword("password");
        return request;
    }

    public static AccountUpdateRequest createAccountUpdateRequest() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAcctActiveStatus("Y");
        request.setAcctCurrBal(new BigDecimal("2000.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        request.setAcctOpenDate("2023-01-15");
        request.setAcctExpirationDate("2025-12-31");
        request.setAcctReissueDate("2024-01-15");
        request.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        request.setAcctCurrCycDebit(new BigDecimal("600.00"));
        request.setAcctAddrZip("12345");
        request.setAcctGroupId("GRP001");
        request.setCustomerFirstName("Jane");
        request.setCustomerLastName("Smith");
        request.setCustomerSsn("987-65-4321");
        request.setCustomerDateOfBirth("1990-03-20");
        request.setCustomerFicoScore(800);
        request.setCustomerPhone1("555-9999");
        request.setCustomerPhone2("555-8888");
        request.setCustomerAddress1("456 Oak Ave");
        request.setCustomerAddress2("Suite 10");
        request.setCustomerCity("Boston");
        request.setCustomerState("MA");
        request.setCustomerZipCode("02101");
        request.setCustomerCountry("USA");
        request.setCustomerGovtIssuedId("DL987654321");
        request.setCustomerEftAccountId("EFT987654");
        request.setCustomerPriCardHolderInd("Y");
        return request;
    }

    public static TransactionCreateRequest createTransactionCreateRequest() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setCardNum("4000123456789012");
        request.setTranTypeCd("01");
        request.setTranCatCd("1");
        request.setTranSource("ONLINE");
        request.setTranDesc("Test Transaction");
        request.setTranAmt(new BigDecimal("150.00"));
        request.setOrigDate("2024-01-15");
        request.setProcDate("2024-01-15");
        request.setMerchantId("9876543");
        request.setMerchantName("Test Store");
        request.setMerchantCity("Test City");
        request.setMerchantZip("12345");
        request.setConfirmation("Y");
        return request;
    }

    public static BillPaymentRequest createBillPaymentRequest() {
        BillPaymentRequest request = new BillPaymentRequest();
        request.setAccountId(12345678901L);
        request.setConfirmation("Y");
        request.setPaymentAmount(new BigDecimal("1500.00"));
        request.setPaymentDate("2024-01-15");
        return request;
    }
}
