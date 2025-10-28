package com.modernized.entities;

import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void constructor_ShouldCreateCustomerWithAllFields() {
        Customer customer = TestDataBuilder.createTestCustomer();
        
        assertThat(customer.getCustId()).isEqualTo(1001L);
        assertThat(customer.getCustFirstName()).isEqualTo("John");
        assertThat(customer.getCustLastName()).isEqualTo("Doe");
        assertThat(customer.getCustSsn()).isEqualTo(123456789L);
        assertThat(customer.getCustFicoCreditScore()).isEqualTo(750);
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        Customer customer1 = TestDataBuilder.createTestCustomer();
        Customer customer2 = TestDataBuilder.createTestCustomer();
        
        assertThat(customer1).isEqualTo(customer2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        Customer customer1 = TestDataBuilder.createTestCustomer();
        Customer customer2 = TestDataBuilder.createTestCustomer();
        customer2.setCustId(9999L);
        
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void toString_ShouldContainKeyFields() {
        Customer customer = TestDataBuilder.createTestCustomer();
        
        String result = customer.toString();
        
        assertThat(result).contains("1001");
        assertThat(result).contains("John");
        assertThat(result).contains("Doe");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Customer customer = new Customer();
        
        customer.setCustId(2002L);
        customer.setCustFirstName("Jane");
        customer.setCustLastName("Smith");
        customer.setCustFicoCreditScore(800);
        
        assertThat(customer.getCustId()).isEqualTo(2002L);
        assertThat(customer.getCustFirstName()).isEqualTo("Jane");
        assertThat(customer.getCustLastName()).isEqualTo("Smith");
        assertThat(customer.getCustFicoCreditScore()).isEqualTo(800);
    }
}
