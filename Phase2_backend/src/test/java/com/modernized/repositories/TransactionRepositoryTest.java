package com.modernized.repositories;

import com.modernized.entities.Transaction;
import com.modernized.entities.Card;
import com.modernized.entities.Account;
import com.modernized.entities.TransactionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findById_ShouldReturnTransaction_WhenTransactionExists() {
        Transaction transaction = createTestTransaction();
        transactionRepository.save(transaction);

        Optional<Transaction> found = transactionRepository.findById("T123");

        assertTrue(found.isPresent());
        assertEquals("T123", found.get().getTranId());
    }

    @Test
    void findByTranIdContaining_ShouldReturnMatchingTransactions() {
        Transaction transaction = createTestTransaction();
        testEntityManager.persistAndFlush(transaction.getCard().getAccount());
        testEntityManager.persistAndFlush(transaction.getCard());
        
        testEntityManager.persistAndFlush(createTransactionCategory());
        
        transactionRepository.save(transaction);

        Page<Transaction> found = transactionRepository.findByTranIdContaining("T1", PageRequest.of(0, 10));

        assertEquals(1, found.getTotalElements());
        assertEquals("T123", found.getContent().get(0).getTranId());
    }

    @Test
    void save_ShouldPersistTransaction() {
        Transaction transaction = createTestTransaction();

        Transaction saved = transactionRepository.save(transaction);

        assertNotNull(saved);
        assertEquals("T123", saved.getTranId());
        assertEquals(new BigDecimal("50.00"), saved.getTranAmt());
    }

    private Transaction createTestTransaction() {
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
        
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(1L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        card.setAccount(account);
        
        Transaction transaction = new Transaction();
        transaction.setTranId("T123");
        transaction.setTranCardNum("1234567890123456");
        transaction.setTranTypeCd("01");
        transaction.setTranCatCd(100);
        transaction.setTranSource("POS");
        transaction.setTranDesc("Purchase");
        transaction.setTranAmt(new BigDecimal("50.00"));
        transaction.setTranOrigTs("2023-01-01 10:00:00");
        transaction.setTranProcTs("2023-01-01 10:01:00");
        transaction.setTranMerchantId(12345L);
        transaction.setTranMerchantName("Test Store");
        transaction.setTranMerchantCity("Test City");
        transaction.setTranMerchantZip("12345");
        transaction.setCard(card);
        return transaction;
    }

    private TransactionCategory createTransactionCategory() {
        TransactionCategory category = new TransactionCategory();
        category.setTranCatCd(100);
        category.setTranTypeCd("01");
        category.setTranCatTypeDesc("Purchase");
        return category;
    }
}
