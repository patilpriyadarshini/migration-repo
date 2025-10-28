package com.modernized.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void constructor_ShouldCreateCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(1L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");

        assertEquals("1234567890123456", card.getCardNum());
        assertEquals(1L, card.getCardAcctId());
        assertEquals("John Doe", card.getCardEmbossedName());
        assertEquals("Y", card.getCardActiveStatus());
        assertEquals("12/2025", card.getCardExpiraionDate());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameCardNum() {
        Card card1 = new Card();
        card1.setCardNum("1234567890123456");
        
        Card card2 = new Card();
        card2.setCardNum("1234567890123456");

        assertEquals(card1, card2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentCardNum() {
        Card card1 = new Card();
        card1.setCardNum("1234567890123456");
        
        Card card2 = new Card();
        card2.setCardNum("9876543210987654");

        assertNotEquals(card1, card2);
    }

    @Test
    void toString_ShouldContainCardNum() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardEmbossedName("John Doe");

        String result = card.toString();

        assertTrue(result.contains("1234567890123456"));
    }
}
