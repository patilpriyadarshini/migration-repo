package com.modernized.repositories;

import com.modernized.entities.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findById_ShouldReturnAccount_WhenAccountExists() {
        Account account = createTestAccount();
        accountRepository.save(account);

        Optional<Account> found = accountRepository.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getAcctId());
    }

    @Test
    void save_ShouldPersistAccount() {
        Account account = createTestAccount();

        Account saved = accountRepository.save(account);

        assertNotNull(saved);
        assertEquals(1L, saved.getAcctId());
        assertEquals("Y", saved.getAcctActiveStatus());
    }

    @Test
    void deleteById_ShouldRemoveAccount() {
        Account account = createTestAccount();
        accountRepository.save(account);

        accountRepository.deleteById(1L);

        Optional<Account> found = accountRepository.findById(1L);
        assertFalse(found.isPresent());
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
        return account;
    }
}
