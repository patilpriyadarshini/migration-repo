package com.modernized.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardCreation() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");

        assertEquals("1234567890123456", card.getCardNum());
        assertEquals(123456789L, card.getCardAcctId());
        assertEquals("JOHN DOE", card.getCardEmbossedName());
        assertEquals("Y", card.getCardActiveStatus());
    }

    @Test
    void testCardExpirationDate() {
        Card card = new Card();
        card.setCardExpiraionDate("12/2025");

        assertEquals("12/2025", card.getCardExpiraionDate());
    }

    @Test
    void testCardEquality() {
        Card card1 = new Card();
        card1.setCardNum("1234567890123456");
        
        Card card2 = new Card();
        card2.setCardNum("1234567890123456");

        assertEquals(card1.getCardNum(), card2.getCardNum());
    }
}
