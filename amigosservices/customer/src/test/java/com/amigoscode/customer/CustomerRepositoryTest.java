package com.amigoscode.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer() {
        // Given
        int id = 1;
        Customer customerTest = Customer.builder()
                .id(id)
                .firstName("Juan")
                .lastName("Lopez")
                .email("prueba@test.com")
                .build();
        // When
        underTest.save(customerTest);
        // Then
        Optional<Customer> customer = underTest.findById(id);
        assertThat(customer)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c).isEqualTo(customerTest);
                        }
                );
    }
}