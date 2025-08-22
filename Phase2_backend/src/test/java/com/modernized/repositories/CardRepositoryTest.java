package com.modernized.repositories;

import com.modernized.entities.Card;
import com.modernized.entities.Account;
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
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findById_ShouldReturnCard_WhenCardExists() {
        Card card = createTestCard();
        cardRepository.save(card);

        Optional<Card> found = cardRepository.findById("1234567890123456");

        assertTrue(found.isPresent());
        assertEquals("1234567890123456", found.get().getCardNum());
    }

    @Test
    void findByAcctId_ShouldReturnCardsForAccount() {
        Card card = createTestCard();
        testEntityManager.persistAndFlush(card.getAccount());
        cardRepository.save(card);

        Page<Card> found = cardRepository.findByAcctId(1L, PageRequest.of(0, 10));

        assertEquals(1, found.getTotalElements());
        assertEquals("1234567890123456", found.getContent().get(0).getCardNum());
    }

    @Test
    void save_ShouldPersistCard() {
        Card card = createTestCard();

        Card saved = cardRepository.save(card);

        assertNotNull(saved);
        assertEquals("1234567890123456", saved.getCardNum());
        assertEquals("John Doe", saved.getCardEmbossedName());
    }

    private Card createTestCard() {
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
        return card;
    }
}
