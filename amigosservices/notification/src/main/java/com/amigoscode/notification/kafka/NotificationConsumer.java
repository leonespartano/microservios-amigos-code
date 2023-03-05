package com.amigoscode.notification.kafka;

import com.amigoscode.clients.notification.NotificationRequest;
import com.amigoscode.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "code-services",
            groupId = "groupId"
    )
    void listener(NotificationRequest notificationRequest){
        log.info("Consumed {} from kafka", notificationRequest);
        notificationService.receiveNotification(notificationRequest);
    }

}
