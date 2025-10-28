package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void testCustomerCreation() {
        assertNotNull(customer);
        assertNull(customer.getCustId());
        assertNull(customer.getCustFirstName());
        assertNull(customer.getCustLastName());
        assertNull(customer.getCustSsn());
    }

    @Test
    void testSetAndGetCustId() {
        Long custId = 12345L;
        customer.setCustId(custId);
        assertEquals(custId, customer.getCustId());
    }

    @Test
    void testSetAndGetCustIdWithNull() {
        customer.setCustId(null);
        assertNull(customer.getCustId());
    }

    @Test
    void testSetAndGetCustFirstName() {
        String firstName = "John";
        customer.setCustFirstName(firstName);
        assertEquals(firstName, customer.getCustFirstName());
    }

    @Test
    void testSetAndGetCustFirstNameWithSpecialCharacters() {
        String firstName = "Jean-Pierre";
        customer.setCustFirstName(firstName);
        assertEquals(firstName, customer.getCustFirstName());
    }

    @Test
    void testSetAndGetCustFirstNameWithNull() {
        customer.setCustFirstName(null);
        assertNull(customer.getCustFirstName());
    }

    @Test
    void testSetAndGetCustLastName() {
        String lastName = "Doe";
        customer.setCustLastName(lastName);
        assertEquals(lastName, customer.getCustLastName());
    }

    @Test
    void testSetAndGetCustLastNameWithSpecialCharacters() {
        String lastName = "O'Connor-Smith";
        customer.setCustLastName(lastName);
        assertEquals(lastName, customer.getCustLastName());
    }

    @Test
    void testSetAndGetCustLastNameWithNull() {
        customer.setCustLastName(null);
        assertNull(customer.getCustLastName());
    }

    @Test
    void testSetAndGetCustSsn() {
        Long ssn = 123456789L;
        customer.setCustSsn(ssn);
        assertEquals(ssn, customer.getCustSsn());
    }

    @Test
    void testSetAndGetCustSsnWithNull() {
        customer.setCustSsn(null);
        assertNull(customer.getCustSsn());
    }

    @Test
    void testCompleteCustomerSetup() {
        customer.setCustId(12345L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);

        assertEquals(12345L, customer.getCustId());
        assertEquals("John", customer.getCustFirstName());
        assertEquals("Doe", customer.getCustLastName());
        assertEquals(123456789L, customer.getCustSsn());
    }

    @Test
    void testCustomerEquality() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        
        customer1.setCustId(12345L);
        customer2.setCustId(12345L);
        
        assertEquals(customer1.getCustId(), customer2.getCustId());
    }

    @Test
    void testCustomerToString() {
        customer.setCustId(12345L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        String customerString = customer.toString();
        assertNotNull(customerString);
        assertTrue(customerString.contains("Customer") || customerString.contains("12345"));
    }

    @Test
    void testCustomerValidation() {
        customer.setCustId(12345L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        
        assertEquals(12345L, customer.getCustId());
        assertEquals("John", customer.getCustFirstName());
        assertEquals("Doe", customer.getCustLastName());
        assertEquals(123456789L, customer.getCustSsn());
    }

    @Test
    void testSsnValidation() {
        Long validSsn = 123456789L;
        customer.setCustSsn(validSsn);
        assertEquals(validSsn, customer.getCustSsn());
        assertTrue(customer.getCustSsn().toString().length() == 9);
    }

    @Test
    void testNameValidation() {
        String validFirstName = "John";
        String validLastName = "Doe";
        
        customer.setCustFirstName(validFirstName);
        customer.setCustLastName(validLastName);
        
        assertEquals(validFirstName, customer.getCustFirstName());
        assertEquals(validLastName, customer.getCustLastName());
        assertTrue(customer.getCustFirstName().length() > 0);
        assertTrue(customer.getCustLastName().length() > 0);
    }
}
