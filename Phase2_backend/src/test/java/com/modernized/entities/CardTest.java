package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Validator validator;
    private Card card;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        card = new Card();
        card.setCardNum("4111111111111111");
        card.setCardAcctId(12345678901L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpirationDate("12/2025");
    }

    @Test
    void validCard_ShouldPassValidation() {
        Set<ConstraintViolation<Card>> violations = validator.validate(card);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameCardNum() {
        Card card1 = new Card();
        card1.setCardNum("4111111111111111");
        
        Card card2 = new Card();
        card2.setCardNum("4111111111111111");
        
        assertEquals(card1, card2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentCardNum() {
        Card card1 = new Card();
        card1.setCardNum("4111111111111111");
        
        Card card2 = new Card();
        card2.setCardNum("4111111111111112");
        
        assertNotEquals(card1, card2);
    }

    @Test
    void hashCode_ShouldBeSame_WhenSameCardNum() {
        Card card1 = new Card();
        card1.setCardNum("4111111111111111");
        
        Card card2 = new Card();
        card2.setCardNum("4111111111111111");
        
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void toString_ShouldContainCardNum() {
        String result = card.toString();
        
        assertTrue(result.contains("4111111111111111"));
        assertTrue(result.contains("Card{"));
    }

    @Test
    void constructor_ShouldSetAllFields() {
        Card newCard = new Card("4111111111111112", 12345678902L, 123, 
                               "Jane Smith", "06/2026", "Y");
        
        assertEquals("4111111111111112", newCard.getCardNum());
        assertEquals(12345678902L, newCard.getCardAcctId());
        assertEquals(Integer.valueOf(123), newCard.getCardCvvCd());
        assertEquals("Jane Smith", newCard.getCardEmbossedName());
        assertEquals("06/2026", newCard.getCardExpirationDate());
        assertEquals("Y", newCard.getCardActiveStatus());
    }
}
