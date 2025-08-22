package com.modernized.repositories;

import com.modernized.entities.Card;
import com.modernized.entities.Account;
import com.modernized.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    private Card testCard;
    private Account testAccount;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(12345L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        entityManager.persistAndFlush(testCustomer);

        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testAccount.setAcctOpenDate("2023-01-01");
        testAccount.setAcctExpiraionDate("2025-12-31");
        testAccount.setAcctCurrCycCredit(new BigDecimal("0.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("0.00"));
        testAccount.setCustomer(testCustomer);
        entityManager.persistAndFlush(testAccount);

        testCard = new Card();
        testCard.setCardNum("1234567890123456");
        testCard.setCardAcctId(testAccount.getAcctId());
        testCard.setCardEmbossedName("JOHN DOE");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpiraionDate("12/2025");
        testCard.setAccount(testAccount);
        entityManager.persistAndFlush(testCard);
    }

    @Test
    void testFindById() {
        Optional<Card> found = cardRepository.findById(testCard.getCardNum());
        
        assertTrue(found.isPresent());
        assertEquals(testCard.getCardNum(), found.get().getCardNum());
        assertEquals(testCard.getCardActiveStatus(), found.get().getCardActiveStatus());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Card> found = cardRepository.findById("9999999999999999");
        assertFalse(found.isPresent());
    }

    @Test
    void testSaveCard() {
        Card newCard = new Card();
        newCard.setCardNum("9876543210987654");
        newCard.setCardAcctId(testAccount.getAcctId());
        newCard.setCardEmbossedName("JANE DOE");
        newCard.setCardActiveStatus("Y");
        newCard.setCardExpiraionDate("06/2026");
        newCard.setAccount(testAccount);

        Card saved = cardRepository.save(newCard);
        
        assertNotNull(saved);
        assertEquals(newCard.getCardNum(), saved.getCardNum());
        assertEquals(newCard.getCardEmbossedName(), saved.getCardEmbossedName());
    }

    @Test
    void testFindAll() {
        Page<Card> cards = cardRepository.findAll(PageRequest.of(0, 10));
        
        assertNotNull(cards);
        assertTrue(cards.getTotalElements() >= 1);
        assertTrue(cards.getContent().contains(testCard));
    }

    @Test
    void testUpdateCard() {
        testCard.setCardActiveStatus("N");
        Card updated = cardRepository.save(testCard);
        
        assertEquals("N", updated.getCardActiveStatus());
    }

    @Test
    void testDeleteCard() {
        String cardNum = testCard.getCardNum();
        cardRepository.deleteById(cardNum);
        
        Optional<Card> found = cardRepository.findById(cardNum);
        assertFalse(found.isPresent());
    }

    @Test
    void testCardAccountRelationship() {
        Optional<Card> found = cardRepository.findById(testCard.getCardNum());
        
        assertTrue(found.isPresent());
        assertNotNull(found.get().getAccount());
        assertEquals(testAccount.getAcctId(), found.get().getAccount().getAcctId());
    }

    @Test
    void testCardValidation() {
        assertEquals("1234567890123456", testCard.getCardNum());
        assertEquals("JOHN DOE", testCard.getCardEmbossedName());
        assertEquals("Y", testCard.getCardActiveStatus());
        assertEquals("12/2025", testCard.getCardExpiraionDate());
        assertEquals(testAccount.getAcctId(), testCard.getCardAcctId());
    }
}
