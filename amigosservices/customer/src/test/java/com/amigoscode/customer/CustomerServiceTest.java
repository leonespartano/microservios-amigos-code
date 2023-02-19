package com.amigoscode.customer;

import com.amigoscode.amqp.RabbitMQMessageProducer;
import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClient;
import com.amigoscode.clients.notification.NotificationClient;
import com.amigoscode.clients.notification.NotificationRequest;
import com.amigoscode.clients.notification.NotificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FraudClient fraudClient;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Captor
    private ArgumentCaptor<Customer> argumentCaptorCustomer;

    @Captor
    private ArgumentCaptor<NotificationRequest> notificationRequestArgumentCaptor;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.initMocks(this);;
        underTest = new CustomerService(customerRepository, fraudClient, rabbitMQMessageProducer);
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

        NotificationRequest notificationRequestTest = new NotificationRequest(
                customerTest.getId(),
                customerTest.getEmail(),
                "message send"
        );


        given(fraudClient.isFraudster(customerTest.getId()))
                .willReturn(new FraudCheckResponse(false));


        // When
        underTest.registerCustomer(request);

        // Then
        then(customerRepository).should().saveAndFlush(argumentCaptorCustomer.capture());
        Customer customerArgumentCaptor = argumentCaptorCustomer.getValue();

        assertThat(customerArgumentCaptor).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(customerTest);

        then(rabbitMQMessageProducer).should().publish(
                notificationRequestTest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

//        NotificationRequest notificationRequestValue = notificationRequestArgumentCaptor.getValue();

//        assertThat(notificationRequestArgumentCaptor.getValue().toCustomerEmail())
//                .isEqualTo(customerTest.getEmail());
        
    }

    @Test
    void itShouldNotRegisterCustomerWhenIsFraudster() {
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

        given(fraudClient.isFraudster(customerTest.getId()))
                .willReturn(new FraudCheckResponse(true));

        // When
        // Then
        assertThatThrownBy(() -> underTest.registerCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("fraudster");

    }

}