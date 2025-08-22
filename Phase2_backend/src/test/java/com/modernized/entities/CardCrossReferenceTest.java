package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCrossReferenceTest {

    private CardCrossReference cardCrossReference;

    @BeforeEach
    void setUp() {
        cardCrossReference = new CardCrossReference();
    }

    @Test
    void testCardCrossReferenceCreation() {
        assertNotNull(cardCrossReference);
        assertNull(cardCrossReference.getXrefCardNum());
        assertNull(cardCrossReference.getXrefCustId());
        assertNull(cardCrossReference.getXrefAcctId());
        assertNull(cardCrossReference.getCustomer());
        assertNull(cardCrossReference.getAccount());
        assertNull(cardCrossReference.getCard());
    }

    @Test
    void testParameterizedConstructor() {
        CardCrossReference xref = new CardCrossReference("1234567890123456", 12345L, 123456789L);
        
        assertEquals("1234567890123456", xref.getXrefCardNum());
        assertEquals(12345L, xref.getXrefCustId());
        assertEquals(123456789L, xref.getXrefAcctId());
    }

    @Test
    void testSetAndGetXrefCardNum() {
        String cardNum = "1234567890123456";
        cardCrossReference.setXrefCardNum(cardNum);
        assertEquals(cardNum, cardCrossReference.getXrefCardNum());
    }

    @Test
    void testSetAndGetXrefCardNumWithNull() {
        cardCrossReference.setXrefCardNum(null);
        assertNull(cardCrossReference.getXrefCardNum());
    }

    @Test
    void testSetAndGetXrefCustId() {
        Long custId = 12345L;
        cardCrossReference.setXrefCustId(custId);
        assertEquals(custId, cardCrossReference.getXrefCustId());
    }

    @Test
    void testSetAndGetXrefCustIdWithNull() {
        cardCrossReference.setXrefCustId(null);
        assertNull(cardCrossReference.getXrefCustId());
    }

    @Test
    void testSetAndGetXrefAcctId() {
        Long acctId = 123456789L;
        cardCrossReference.setXrefAcctId(acctId);
        assertEquals(acctId, cardCrossReference.getXrefAcctId());
    }

    @Test
    void testSetAndGetXrefAcctIdWithNull() {
        cardCrossReference.setXrefAcctId(null);
        assertNull(cardCrossReference.getXrefAcctId());
    }

    @Test
    void testSetAndGetCustomer() {
        Customer customer = new Customer();
        customer.setCustId(12345L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        
        cardCrossReference.setCustomer(customer);
        assertEquals(customer, cardCrossReference.getCustomer());
        assertEquals("John", cardCrossReference.getCustomer().getCustFirstName());
    }

    @Test
    void testSetAndGetCustomerWithNull() {
        cardCrossReference.setCustomer(null);
        assertNull(cardCrossReference.getCustomer());
    }

    @Test
    void testSetAndGetAccount() {
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        
        cardCrossReference.setAccount(account);
        assertEquals(account, cardCrossReference.getAccount());
        assertEquals("Y", cardCrossReference.getAccount().getAcctActiveStatus());
    }

    @Test
    void testSetAndGetAccountWithNull() {
        cardCrossReference.setAccount(null);
        assertNull(cardCrossReference.getAccount());
    }

    @Test
    void testSetAndGetCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardActiveStatus("Y");
        
        cardCrossReference.setCard(card);
        assertEquals(card, cardCrossReference.getCard());
        assertEquals("Y", cardCrossReference.getCard().getCardActiveStatus());
    }

    @Test
    void testSetAndGetCardWithNull() {
        cardCrossReference.setCard(null);
        assertNull(cardCrossReference.getCard());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(cardCrossReference.equals(cardCrossReference));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(cardCrossReference.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(cardCrossReference.equals("not a card cross reference"));
    }

    @Test
    void testEqualsWithSameXrefCardNum() {
        CardCrossReference xref1 = new CardCrossReference("1234567890123456", 12345L, 123456789L);
        CardCrossReference xref2 = new CardCrossReference("1234567890123456", 54321L, 987654321L);
        
        assertTrue(xref1.equals(xref2));
    }

    @Test
    void testEqualsWithDifferentXrefCardNum() {
        CardCrossReference xref1 = new CardCrossReference("1234567890123456", 12345L, 123456789L);
        CardCrossReference xref2 = new CardCrossReference("6543210987654321", 12345L, 123456789L);
        
        assertFalse(xref1.equals(xref2));
    }

    @Test
    void testHashCodeConsistency() {
        cardCrossReference.setXrefCardNum("1234567890123456");
        int hash1 = cardCrossReference.hashCode();
        int hash2 = cardCrossReference.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCodeWithSameXrefCardNum() {
        CardCrossReference xref1 = new CardCrossReference("1234567890123456", 12345L, 123456789L);
        CardCrossReference xref2 = new CardCrossReference("1234567890123456", 54321L, 987654321L);
        
        assertEquals(xref1.hashCode(), xref2.hashCode());
    }

    @Test
    void testToString() {
        cardCrossReference.setXrefCardNum("1234567890123456");
        cardCrossReference.setXrefCustId(12345L);
        cardCrossReference.setXrefAcctId(123456789L);
        
        String result = cardCrossReference.toString();
        assertNotNull(result);
        assertTrue(result.contains("CardCrossReference"));
        assertTrue(result.contains("1234567890123456"));
        assertTrue(result.contains("12345"));
        assertTrue(result.contains("123456789"));
    }

    @Test
    void testCompleteCardCrossReferenceSetup() {
        Customer customer = new Customer();
        customer.setCustId(12345L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        
        Account account = new Account();
        account.setAcctId(123456789L);
        account.setAcctActiveStatus("Y");
        
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardActiveStatus("Y");
        
        cardCrossReference.setXrefCardNum("1234567890123456");
        cardCrossReference.setXrefCustId(12345L);
        cardCrossReference.setXrefAcctId(123456789L);
        cardCrossReference.setCustomer(customer);
        cardCrossReference.setAccount(account);
        cardCrossReference.setCard(card);

        assertEquals("1234567890123456", cardCrossReference.getXrefCardNum());
        assertEquals(12345L, cardCrossReference.getXrefCustId());
        assertEquals(123456789L, cardCrossReference.getXrefAcctId());
        assertEquals(customer, cardCrossReference.getCustomer());
        assertEquals(account, cardCrossReference.getAccount());
        assertEquals(card, cardCrossReference.getCard());
    }

    @Test
    void testCardNumValidation() {
        String validCardNum = "1234567890123456";
        cardCrossReference.setXrefCardNum(validCardNum);
        assertEquals(validCardNum, cardCrossReference.getXrefCardNum());
        assertTrue(cardCrossReference.getXrefCardNum().length() <= 16);
    }

    @Test
    void testRelationshipConsistency() {
        Customer customer = new Customer();
        customer.setCustId(12345L);
        
        Account account = new Account();
        account.setAcctId(123456789L);
        
        Card card = new Card();
        card.setCardNum("1234567890123456");
        
        cardCrossReference.setXrefCardNum("1234567890123456");
        cardCrossReference.setXrefCustId(12345L);
        cardCrossReference.setXrefAcctId(123456789L);
        cardCrossReference.setCustomer(customer);
        cardCrossReference.setAccount(account);
        cardCrossReference.setCard(card);
        
        assertEquals(cardCrossReference.getXrefCustId(), cardCrossReference.getCustomer().getCustId());
        assertEquals(cardCrossReference.getXrefAcctId(), cardCrossReference.getAccount().getAcctId());
        assertEquals(cardCrossReference.getXrefCardNum(), cardCrossReference.getCard().getCardNum());
    }
}
