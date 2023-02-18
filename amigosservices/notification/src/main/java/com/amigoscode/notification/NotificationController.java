package com.amigoscode.notification;

import com.amigoscode.clients.notification.NotificationRequest;
import com.amigoscode.clients.notification.NotificationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping
    public NotificationResponse receiveNotification(@RequestBody NotificationRequest request){
        log.info("New notification ... {}", request);
        boolean result = notificationService.receiveNotification(request);
        return new NotificationResponse(result);
    }
}
