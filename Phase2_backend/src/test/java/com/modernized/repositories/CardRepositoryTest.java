package com.modernized.repositories;

import com.modernized.entities.Account;
import com.modernized.entities.Card;
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

    private Customer testCustomer;
    private Account testAccount;
    private Card testCard;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustId(1001L);
        testCustomer.setCustFirstName("John");
        testCustomer.setCustLastName("Doe");
        testCustomer.setCustSsn(123456789L);
        testCustomer.setCustDobYyyyMmDd("1985-03-15");
        testCustomer.setCustFicoCreditScore(750);
        testCustomer.setCustPhoneNum1("555-0101");
        testCustomer.setCustAddrLine1("123 Main St");
        testCustomer.setCustAddrLine3("New York");
        testCustomer.setCustAddrStateCd("NY");
        testCustomer.setCustAddrZip("10001");
        testCustomer.setCustAddrCountryCd("USA");
        entityManager.persistAndFlush(testCustomer);

        testAccount = new Account();
        testAccount.setAcctId(12345678901L);
        testAccount.setAcctActiveStatus("Y");
        testAccount.setAcctCurrBal(new BigDecimal("1500.00"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testAccount.setAcctOpenDate("2020-01-15");
        testAccount.setAcctExpirationDate("2027-01-15");
        testAccount.setAcctReissueDate("2023-01-15");
        testAccount.setAcctCurrCycCredit(new BigDecimal("2500.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("1000.00"));
        testAccount.setAcctAddrZip("10001");
        testAccount.setAcctGroupId("GRP001");
        testAccount.setCustomer(testCustomer);
        entityManager.persistAndFlush(testAccount);

        testCard = new Card();
        testCard.setCardNum("4111111111111111");
        testCard.setCardAcctId(12345678901L);
        testCard.setCardEmbossedName("John Doe");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpirationDate("12/2025");
        testCard.setAccount(testAccount);
    }

    @Test
    void findById_ShouldReturnCard_WhenCardExists() {
        entityManager.persistAndFlush(testCard);
        
        Optional<Card> found = cardRepository.findById("4111111111111111");
        
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getCardEmbossedName());
        assertEquals("Y", found.get().getCardActiveStatus());
        assertEquals("12/2025", found.get().getCardExpirationDate());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenCardDoesNotExist() {
        Optional<Card> found = cardRepository.findById("9999999999999999");
        
        assertFalse(found.isPresent());
    }

    @Test
    void findByAcctId_ShouldReturnCards_WhenAccountHasCards() {
        entityManager.persistAndFlush(testCard);
        
        Page<Card> found = cardRepository.findByAcctId(12345678901L, PageRequest.of(0, 10));
        
        assertEquals(1, found.getTotalElements());
        assertEquals("4111111111111111", found.getContent().get(0).getCardNum());
    }

    @Test
    void findByAcctId_ShouldReturnEmpty_WhenAccountHasNoCards() {
        Page<Card> found = cardRepository.findByAcctId(99999999999L, PageRequest.of(0, 10));
        
        assertEquals(0, found.getTotalElements());
    }

    @Test
    void findByCardNum_ShouldReturnCards_WhenCardNumberMatches() {
        entityManager.persistAndFlush(testCard);
        
        Page<Card> found = cardRepository.findByCardNum("4111111111111111", PageRequest.of(0, 10));
        
        assertEquals(1, found.getTotalElements());
        assertEquals("John Doe", found.getContent().get(0).getCardEmbossedName());
    }

    @Test
    void findByAcctIdAndCardNum_ShouldReturnCards_WhenBothMatch() {
        entityManager.persistAndFlush(testCard);
        
        Page<Card> found = cardRepository.findByAcctIdAndCardNum(12345678901L, "4111111111111111", PageRequest.of(0, 10));
        
        assertEquals(1, found.getTotalElements());
        assertEquals("John Doe", found.getContent().get(0).getCardEmbossedName());
    }

    @Test
    void save_ShouldPersistCard_WhenValidCard() {
        Card saved = cardRepository.save(testCard);
        
        assertNotNull(saved);
        assertEquals("4111111111111111", saved.getCardNum());
        assertEquals("John Doe", saved.getCardEmbossedName());
        
        Optional<Card> found = cardRepository.findById("4111111111111111");
        assertTrue(found.isPresent());
    }
}
