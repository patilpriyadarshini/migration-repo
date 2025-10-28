package com.modernized.repositories;

import com.modernized.entities.Card;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    void findById_ShouldReturnCard_WhenCardExists() {
        Card card = TestDataBuilder.createTestCard();
        cardRepository.save(card);
        
        Optional<Card> result = cardRepository.findById(card.getCardNum());
        
        assertThat(result).isPresent();
        assertThat(result.get().getCardNum()).isEqualTo(card.getCardNum());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenCardDoesNotExist() {
        Optional<Card> result = cardRepository.findById("9999999999999999");
        
        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldPersistCard() {
        Card card = TestDataBuilder.createTestCard();
        
        Card savedCard = cardRepository.save(card);
        
        assertThat(savedCard.getCardNum()).isEqualTo(card.getCardNum());
        assertThat(cardRepository.findById(card.getCardNum())).isPresent();
    }
}
