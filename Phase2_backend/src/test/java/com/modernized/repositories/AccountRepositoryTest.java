package com.modernized.repositories;

import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(12345L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        entityManager.persistAndFlush(testCustomer);

        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testAccount.setAcctOpenDate("2023-01-01");
        testAccount.setAcctExpiraionDate("2025-12-31");
        testAccount.setAcctCurrCycCredit(new BigDecimal("0.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("0.00"));
        testAccount.setCustomer(testCustomer);
        entityManager.persistAndFlush(testAccount);
    }

    @Test
    void testFindById() {
        Optional<Account> found = accountRepository.findById(testAccount.getAcctId());
        
        assertTrue(found.isPresent());
        assertEquals(testAccount.getAcctId(), found.get().getAcctId());
        assertEquals(testAccount.getAcctActiveStatus(), found.get().getAcctActiveStatus());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Account> found = accountRepository.findById(999999999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testSaveAccount() {
        Account newAccount = new Account();
        newAccount.setAcctId(987654321L);
        newAccount.setAcctActiveStatus("Y");
        newAccount.setAcctCurrBal(new BigDecimal("2000.00"));
        newAccount.setAcctCreditLimit(new BigDecimal("10000.00"));
        newAccount.setAcctCashCreditLimit(new BigDecimal("2000.00"));
        newAccount.setAcctOpenDate("2024-01-01");
        newAccount.setAcctExpiraionDate("2026-12-31");
        newAccount.setAcctCurrCycCredit(new BigDecimal("0.00"));
        newAccount.setAcctCurrCycDebit(new BigDecimal("0.00"));
        newAccount.setCustomer(testCustomer);

        Account saved = accountRepository.save(newAccount);
        
        assertNotNull(saved);
        assertEquals(newAccount.getAcctId(), saved.getAcctId());
        assertEquals(newAccount.getAcctCurrBal(), saved.getAcctCurrBal());
    }

    @Test
    void testFindAll() {
        Page<Account> accounts = accountRepository.findAll(PageRequest.of(0, 10));
        
        assertNotNull(accounts);
        assertTrue(accounts.getTotalElements() >= 1);
        assertTrue(accounts.getContent().contains(testAccount));
    }

    @Test
    void testUpdateAccount() {
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        Account updated = accountRepository.save(testAccount);
        
        assertEquals(new BigDecimal("1500.00"), updated.getAcctCurrBal());
    }

    @Test
    void testDeleteAccount() {
        Long accountId = testAccount.getAcctId();
        accountRepository.deleteById(accountId);
        
        Optional<Account> found = accountRepository.findById(accountId);
        assertFalse(found.isPresent());
    }

    @Test
    void testAccountCustomerRelationship() {
        Optional<Account> found = accountRepository.findById(testAccount.getAcctId());
        
        assertTrue(found.isPresent());
        assertNotNull(found.get().getCustomer());
        assertEquals(testCustomer.getCustId(), found.get().getCustomer().getCustId());
    }
}
