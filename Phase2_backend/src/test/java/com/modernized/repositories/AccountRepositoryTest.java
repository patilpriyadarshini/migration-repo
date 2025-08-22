package com.modernized.repositories;

import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findById_ShouldReturnAccount_WhenAccountExists() {
        Account account = createTestAccount();
        entityManager.persistAndFlush(account);

        Account found = accountRepository.findById(123456789L).orElse(null);

        assertNotNull(found);
        assertEquals(123456789L, found.getAcctId());
        assertEquals(new BigDecimal("1000.00"), found.getAcctCurrBal());
    }

    @Test
    void save_ShouldPersistAccount() {
        Account account = createTestAccount();

        Account saved = accountRepository.save(account);

        assertNotNull(saved.getAcctId());
        assertEquals(new BigDecimal("1000.00"), saved.getAcctCurrBal());
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
        account.setAcctCreditLimit(new BigDecimal("5000.00"));
        account.setAcctActiveStatus("Y");
        account.setAcctOpenDate("2023-01-01");
        account.setAcctExpiraionDate("2025-12-31");
        account.setAcctCurrCycCredit(new BigDecimal("500.00"));
        account.setAcctCurrCycDebit(new BigDecimal("200.00"));
        
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        account.setCustomer(customer);
        
        return account;
    }
}
