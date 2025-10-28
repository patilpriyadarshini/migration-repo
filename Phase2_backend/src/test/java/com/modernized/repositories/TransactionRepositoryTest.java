package com.modernized.repositories;

import com.modernized.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void findByTranIdContaining_ShouldReturnTransactions_WhenIdMatches() {
        Transaction transaction = createTestTransaction();
        entityManager.persistAndFlush(transaction);

        Page<Transaction> result = transactionRepository.findByTranIdContaining("T123", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("T123456", result.getContent().get(0).getTranId());
    }

    @Test
    void save_ShouldPersistTransaction() {
        Transaction transaction = createTestTransaction();

        Transaction saved = transactionRepository.save(transaction);

        assertNotNull(saved.getTranId());
        assertEquals(new BigDecimal("100.00"), saved.getTranAmt());
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTranId("T123456");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranAmt(new BigDecimal("100.00"));
        transaction.setTranDesc("Test Transaction");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(1);
        transaction.setTranSource("POS");
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:00:00");
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Merchant");
        transaction.setTranMerchantCity("Test City");
        transaction.setTranMerchantZip("12345");
        return transaction;
    }
}
