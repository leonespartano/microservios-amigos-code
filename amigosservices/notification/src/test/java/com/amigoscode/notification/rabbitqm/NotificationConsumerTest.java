package com.amigoscode.notification.rabbitqm;

import com.amigoscode.clients.notification.NotificationRequest;
import com.amigoscode.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

class NotificationConsumerTest {

    private NotificationConsumer underTest;

    @Mock
    private NotificationService notificationService;

    @Captor
    ArgumentCaptor<NotificationRequest> notificationRequestArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new NotificationConsumer(notificationService);
    }

    @Test
    void itShouldCosumeTheMessage() {
        //Given
        NotificationRequest notificationRequest = new NotificationRequest(
                1,
                "test@gmail.com",
                "Hello world"
        );
        //When
        underTest.consumer(notificationRequest);
        //Then
        then(notificationService).should().receiveNotification(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequestValue = notificationRequestArgumentCaptor.getValue();
        assertThat(notificationRequestValue).isEqualTo(notificationRequest);
    }
}