package com.amigoscode.customer;

import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerRepository customerRepository;

    private FraudClient fraudClient;

    @Captor
    private ArgumentCaptor<Customer> argumentCaptorCustomer;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.initMocks(this);;
        underTest = new CustomerService(customerRepository, fraudClient);
    }

    @Test
    void itShouldRegisterCustomer() {
        // Given
        Customer customerTest = Customer.builder()
                .id(null)
                .firstName("Jose")
                .lastName("Lulo")
                .email("example@prueba.test")
                .build();

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            customerTest.getFirstName(),
                customerTest.getLastName(),
                customerTest.getEmail()
        );

        // When
        underTest.registerCustomer(request);

        // Then
        then(customerRepository).should().save(argumentCaptorCustomer.capture());
        Customer customerArgumentCaptor = argumentCaptorCustomer.getValue();

        assertThat(customerArgumentCaptor).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(customerTest);

    }
}