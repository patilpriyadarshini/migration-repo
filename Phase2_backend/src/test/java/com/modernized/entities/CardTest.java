package com.modernized.entities;

import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    void constructor_ShouldCreateCardWithAllFields() {
        Card card = TestDataBuilder.createTestCard();
        
        assertThat(card.getCardNum()).isEqualTo("4000123456789012");
        assertThat(card.getCardAcctId()).isEqualTo(12345678901L);
        assertThat(card.getCardCvvCd()).isEqualTo(123);
        assertThat(card.getCardEmbossedName()).isEqualTo("JOHN DOE");
        assertThat(card.getCardExpirationDate()).isEqualTo("12/2025");
        assertThat(card.getCardActiveStatus()).isEqualTo("Y");
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameCardNumber() {
        Card card1 = TestDataBuilder.createTestCard();
        Card card2 = TestDataBuilder.createTestCard();
        
        assertThat(card1).isEqualTo(card2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentCardNumber() {
        Card card1 = TestDataBuilder.createTestCard();
        Card card2 = TestDataBuilder.createTestCard();
        card2.setCardNum("9999999999999999");
        
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void toString_ShouldContainKeyFields() {
        Card card = TestDataBuilder.createTestCard();
        
        String result = card.toString();
        
        assertThat(result).contains("4000123456789012");
        assertThat(result).contains("12345678901");
        assertThat(result).contains("Y");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Card card = new Card();
        
        card.setCardNum("1111222233334444");
        card.setCardAcctId(99999L);
        card.setCardActiveStatus("N");
        
        assertThat(card.getCardNum()).isEqualTo("1111222233334444");
        assertThat(card.getCardAcctId()).isEqualTo(99999L);
        assertThat(card.getCardActiveStatus()).isEqualTo("N");
    }
}
