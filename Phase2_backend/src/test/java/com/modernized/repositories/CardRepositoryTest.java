package com.modernized.repositories;

import com.modernized.entities.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    @Test
    void findByAcctId_ShouldReturnCards_WhenAccountExists() {
        Card card = createTestCard();
        entityManager.persistAndFlush(card);

        Page<Card> result = cardRepository.findByAcctId(123456789L, PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("1234567890123456", result.getContent().get(0).getCardNum());
    }

    @Test
    void findByCardNum_ShouldReturnCards_WhenCardNumberMatches() {
        Card card = createTestCard();
        entityManager.persistAndFlush(card);

        Page<Card> result = cardRepository.findByCardNum("1234567890123456", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        return card;
    }
}
