package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;
    private Account account;

    @BeforeEach
    void setUp() {
        card = new Card();
        account = new Account();
        account.setAcctId(123456789L);
        account.setAcctCurrBal(new BigDecimal("1000.00"));
    }

    @Test
    void testCardCreation() {
        assertNotNull(card);
        assertNull(card.getCardNum());
        assertNull(card.getCardAcctId());
        assertNull(card.getCardEmbossedName());
        assertNull(card.getCardActiveStatus());
        assertNull(card.getCardExpiraionDate());
        assertNull(card.getAccount());
    }

    @Test
    void testSetAndGetCardNum() {
        String cardNumber = "1234567890123456";
        card.setCardNum(cardNumber);
        assertEquals(cardNumber, card.getCardNum());
    }

    @Test
    void testSetAndGetCardNumWithNull() {
        card.setCardNum(null);
        assertNull(card.getCardNum());
    }

    @Test
    void testSetAndGetCardNumWithDifferentFormats() {
        String cardNumber16 = "1234567890123456";
        String cardNumber15 = "123456789012345";
        
        card.setCardNum(cardNumber16);
        assertEquals(cardNumber16, card.getCardNum());
        
        card.setCardNum(cardNumber15);
        assertEquals(cardNumber15, card.getCardNum());
    }

    @Test
    void testSetAndGetCardAcctId() {
        Long accountId = 123456789L;
        card.setCardAcctId(accountId);
        assertEquals(accountId, card.getCardAcctId());
    }

    @Test
    void testSetAndGetCardAcctIdWithNull() {
        card.setCardAcctId(null);
        assertNull(card.getCardAcctId());
    }

    @Test
    void testSetAndGetCardEmbossedName() {
        String embossedName = "JOHN DOE";
        card.setCardEmbossedName(embossedName);
        assertEquals(embossedName, card.getCardEmbossedName());
    }

    @Test
    void testSetAndGetCardEmbossedNameWithSpecialCharacters() {
        String embossedName = "JOHN O'CONNOR-SMITH";
        card.setCardEmbossedName(embossedName);
        assertEquals(embossedName, card.getCardEmbossedName());
    }

    @Test
    void testSetAndGetCardActiveStatus() {
        String status = "Y";
        card.setCardActiveStatus(status);
        assertEquals(status, card.getCardActiveStatus());
    }

    @Test
    void testSetAndGetCardActiveStatusInactive() {
        String status = "N";
        card.setCardActiveStatus(status);
        assertEquals(status, card.getCardActiveStatus());
    }

    @Test
    void testSetAndGetCardExpiraionDate() {
        String expirationDate = "12/2025";
        card.setCardExpiraionDate(expirationDate);
        assertEquals(expirationDate, card.getCardExpiraionDate());
    }

    @Test
    void testSetAndGetCardExpiraionDateDifferentFormats() {
        String expirationDate1 = "12/25";
        String expirationDate2 = "1225";
        
        card.setCardExpiraionDate(expirationDate1);
        assertEquals(expirationDate1, card.getCardExpiraionDate());
        
        card.setCardExpiraionDate(expirationDate2);
        assertEquals(expirationDate2, card.getCardExpiraionDate());
    }

    @Test
    void testSetAndGetAccount() {
        card.setAccount(account);
        assertEquals(account, card.getAccount());
        assertEquals(123456789L, card.getAccount().getAcctId());
    }

    @Test
    void testSetAndGetAccountWithNull() {
        card.setAccount(null);
        assertNull(card.getAccount());
    }

    @Test
    void testCardValidation() {
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        
        assertEquals("1234567890123456", card.getCardNum());
        assertEquals(123456789L, card.getCardAcctId());
        assertEquals("JOHN DOE", card.getCardEmbossedName());
        assertEquals("Y", card.getCardActiveStatus());
        assertEquals("12/2025", card.getCardExpiraionDate());
    }

    @Test
    void testCompleteCardSetup() {
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        card.setAccount(account);

        assertEquals("1234567890123456", card.getCardNum());
        assertEquals(123456789L, card.getCardAcctId());
        assertEquals("JOHN DOE", card.getCardEmbossedName());
        assertEquals("Y", card.getCardActiveStatus());
        assertEquals("12/2025", card.getCardExpiraionDate());
        assertEquals(account, card.getAccount());
        assertEquals(123456789L, card.getAccount().getAcctId());
    }

    @Test
    void testCardEquality() {
        Card card1 = new Card();
        Card card2 = new Card();
        
        card1.setCardNum("1234567890123456");
        card2.setCardNum("1234567890123456");
        
        assertEquals(card1.getCardNum(), card2.getCardNum());
    }

    @Test
    void testCardToString() {
        card.setCardNum("1234567890123456");
        card.setCardActiveStatus("Y");
        String cardString = card.toString();
        assertNotNull(cardString);
        assertTrue(cardString.contains("Card") || cardString.contains("1234567890123456"));
    }

    @Test
    void testCardWithAccountRelationship() {
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setAccount(account);
        
        assertEquals(card.getCardAcctId(), card.getAccount().getAcctId());
    }
}
