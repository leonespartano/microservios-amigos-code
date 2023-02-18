package com.amigoscode.notification;

import com.amigoscode.clients.notification.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

class NotificationServiceTest {

    private NotificationService underTest;

    @Mock
    private NotificationRepository noticationRepository;

    @Captor
    ArgumentCaptor<Notification> notificationArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new NotificationService(noticationRepository);
    }

    @Test
    void itShouldSaveNotification() {
        //Given
        NotificationRequest requestTest = new NotificationRequest(1, "test@gmail.co", "Hello world");
        Notification notificationTest = Notification.builder()
                .toCustomerId(requestTest.toCustomerId())
                .toCustomerEmail(requestTest.toCustomerEmail())
                .message(requestTest.message())
                .build();
        //When
        boolean result = underTest.receiveNotification(requestTest);
        //Then
        then(noticationRepository).should().save(notificationArgumentCaptor.capture());
        Notification notificationValue = notificationArgumentCaptor.getValue();

        assertThat(notificationValue).usingRecursiveComparison()
                        .ignoringFields("sender", "sentAt")
                        .isEqualTo(notificationTest);
        assertThat(result).isTrue();
    }
}