package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Validator validator;
    private Account account;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        account = new Account();
        account.setAcctId(12345678901L);
        account.setAcctActiveStatus("Y");
        account.setAcctCurrBal(new BigDecimal("1500.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        account.setAcctOpenDate("2020-01-15");
        account.setAcctExpirationDate("2027-01-15");
        account.setAcctReissueDate("2023-01-15");
        account.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        account.setAcctCurrCycDebit(new BigDecimal("1000.00"));
        account.setAcctAddrZip("10001");
        account.setAcctGroupId("GRP001");
    }

    @Test
    void validAccount_ShouldPassValidation() {
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidAccount_ShouldFailValidation_WhenAcctIdIsNull() {
        account.setAcctId(null);
        
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("acctId")));
    }

    @Test
    void invalidAccount_ShouldFailValidation_WhenActiveStatusIsInvalid() {
        account.setAcctActiveStatus("INVALID");
        
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("acctActiveStatus")));
    }

    @Test
    void invalidAccount_ShouldFailValidation_WhenDateFormatIsInvalid() {
        account.setAcctOpenDate("invalid-date");
        
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("acctOpenDate")));
    }

    @Test
    void invalidAccount_ShouldFailValidation_WhenCreditLimitIsZero() {
        account.setAcctCreditLimit(BigDecimal.ZERO);
        
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("acctCreditLimit")));
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameAcctId() {
        Account account1 = new Account();
        account1.setAcctId(12345678901L);
        
        Account account2 = new Account();
        account2.setAcctId(12345678901L);
        
        assertEquals(account1, account2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentAcctId() {
        Account account1 = new Account();
        account1.setAcctId(12345678901L);
        
        Account account2 = new Account();
        account2.setAcctId(12345678902L);
        
        assertNotEquals(account1, account2);
    }

    @Test
    void hashCode_ShouldBeSame_WhenSameAcctId() {
        Account account1 = new Account();
        account1.setAcctId(12345678901L);
        
        Account account2 = new Account();
        account2.setAcctId(12345678901L);
        
        assertEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void toString_ShouldContainAcctId() {
        String result = account.toString();
        
        assertTrue(result.contains("12345678901"));
        assertTrue(result.contains("Account{"));
    }
}
