package com.modernized.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void constructor_ShouldCreateCustomer() {
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustFirstName("John");
        customer.setCustLastName("Doe");
        customer.setCustSsn(123456789L);
        customer.setCustDobYyyyMmDd("1990-01-01");
        customer.setCustFicoCreditScore(750);

        assertEquals(1L, customer.getCustId());
        assertEquals("John", customer.getCustFirstName());
        assertEquals("Doe", customer.getCustLastName());
        assertEquals(123456789L, customer.getCustSsn());
        assertEquals("1990-01-01", customer.getCustDobYyyyMmDd());
        assertEquals(750, customer.getCustFicoCreditScore());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameCustId() {
        Customer customer1 = new Customer();
        customer1.setCustId(1L);
        
        Customer customer2 = new Customer();
        customer2.setCustId(1L);

        assertEquals(customer1, customer2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentCustId() {
        Customer customer1 = new Customer();
        customer1.setCustId(1L);
        
        Customer customer2 = new Customer();
        customer2.setCustId(2L);

        assertNotEquals(customer1, customer2);
    }

    @Test
    void toString_ShouldContainCustId() {
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustFirstName("John");

        String result = customer.toString();

        assertTrue(result.contains("1"));
    }
}
