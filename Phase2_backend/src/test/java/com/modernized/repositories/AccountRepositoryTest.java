package com.modernized.repositories;

import com.modernized.entities.Account;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findById_ShouldReturnAccount_WhenAccountExists() {
        Account account = TestDataBuilder.createTestAccount();
        accountRepository.save(account);
        
        Optional<Account> result = accountRepository.findById(account.getAcctId());
        
        assertThat(result).isPresent();
        assertThat(result.get().getAcctId()).isEqualTo(account.getAcctId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenAccountDoesNotExist() {
        Optional<Account> result = accountRepository.findById(99999999999L);
        
        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldPersistAccount() {
        Account account = TestDataBuilder.createTestAccount();
        
        Account savedAccount = accountRepository.save(account);
        
        assertThat(savedAccount.getAcctId()).isEqualTo(account.getAcctId());
        assertThat(accountRepository.findById(account.getAcctId())).isPresent();
    }

    @Test
    void delete_ShouldRemoveAccount() {
        Account account = TestDataBuilder.createTestAccount();
        accountRepository.save(account);
        
        accountRepository.delete(account);
        
        assertThat(accountRepository.findById(account.getAcctId())).isEmpty();
    }
}
