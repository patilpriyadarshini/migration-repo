package com.modernized.repositories;

import com.modernized.entities.Transaction;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void findById_ShouldReturnTransaction_WhenTransactionExists() {
        Transaction transaction = TestDataBuilder.createTestTransaction();
        transactionRepository.save(transaction);
        
        Optional<Transaction> result = transactionRepository.findById(transaction.getTranId());
        
        assertThat(result).isPresent();
        assertThat(result.get().getTranId()).isEqualTo(transaction.getTranId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTransactionDoesNotExist() {
        Optional<Transaction> result = transactionRepository.findById("T999999999");
        
        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldPersistTransaction() {
        Transaction transaction = TestDataBuilder.createTestTransaction();
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        assertThat(savedTransaction.getTranId()).isEqualTo(transaction.getTranId());
        assertThat(transactionRepository.findById(transaction.getTranId())).isPresent();
    }
}
