package com.amigoscode.customer;

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
    private NotificationClient notificationClient;

    @Captor
    private ArgumentCaptor<Customer> argumentCaptorCustomer;

    @Captor
    private ArgumentCaptor<NotificationRequest> notificationRequestArgumentCaptor;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.initMocks(this);;
        underTest = new CustomerService(customerRepository, fraudClient, notificationClient);
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


        given(fraudClient.isFraudster(customerTest.getId()))
                .willReturn(new FraudCheckResponse(false));

        given(notificationClient.receiveNotification(notificationRequestArgumentCaptor.capture()))
                .willReturn(new NotificationResponse(true));

        // When
        underTest.registerCustomer(request);

        // Then
        then(customerRepository).should().saveAndFlush(argumentCaptorCustomer.capture());
        Customer customerArgumentCaptor = argumentCaptorCustomer.getValue();

        assertThat(customerArgumentCaptor).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(customerTest);

        assertThat(notificationRequestArgumentCaptor.getValue().toCustomerId()).isEqualTo(customerArgumentCaptor.getId());
        
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

    @Test
    void itShouldRegisterCustomerWithOutSentNotification() {
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
                .willReturn(new FraudCheckResponse(false));

        given(notificationClient.receiveNotification(notificationRequestArgumentCaptor.capture()))
                .willReturn(new NotificationResponse(false));

        // When
        // Then

        assertThatThrownBy(() -> underTest.registerCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Notification is not sent");


    }
}