package com.modernized.repositories;

import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Customer testCustomer;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(1001L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        testCustomer.setCustDobYyyyMmDd("1985-03-15");
        testCustomer.setCustFicoCreditScore(750);
        testCustomer.setCustPhoneNum1("555-0101");
        testCustomer.setCustAddrLine1("123 Main St");
        testCustomer.setCustAddrLine3("New York");
        testCustomer.setCustAddrStateCd("NY");
        testCustomer.setCustAddrZip("10001");
        testCustomer.setCustAddrCountryCd("USA");
        entityManager.persistAndFlush(testCustomer);

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
    void findById_ShouldReturnAccount_WhenAccountExists() {
        entityManager.persistAndFlush(testAccount);
        
        Optional<Account> found = accountRepository.findById(12345678901L);
        
        assertTrue(found.isPresent());
        assertEquals("Y", found.get().getAcctActiveStatus());
        assertEquals(new BigDecimal("1500.00"), found.get().getAcctCurrBal());
        assertEquals("John", found.get().getCustomer().getCustFirstName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenAccountDoesNotExist() {
        Optional<Account> found = accountRepository.findById(99999999999L);
        
        assertFalse(found.isPresent());
    }

    @Test
    void save_ShouldPersistAccount_WhenValidAccount() {
        Account saved = accountRepository.save(testAccount);
        
        assertNotNull(saved);
        assertEquals(12345678901L, saved.getAcctId());
        assertEquals("Y", saved.getAcctActiveStatus());
        
        Optional<Account> found = accountRepository.findById(12345678901L);
        assertTrue(found.isPresent());
    }

    @Test
    void save_ShouldUpdateAccount_WhenAccountExists() {
        entityManager.persistAndFlush(testAccount);
        
        testAccount.setAcctActiveStatus("N");
        testAccount.setAcctCurrBal(new BigDecimal("2000.00"));
        
        Account updated = accountRepository.save(testAccount);
        
        assertEquals("N", updated.getAcctActiveStatus());
        assertEquals(new BigDecimal("2000.00"), updated.getAcctCurrBal());
    }
}
