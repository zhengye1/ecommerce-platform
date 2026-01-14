package com.ecommerce.notification.infrastructure.sender;

import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Push notification sender implementation.
 * TODO: Implement actual push notification using Firebase FCM, AWS SNS, or similar.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationSender implements NotificationSender {

    // TODO: Inject push notification client (Firebase Admin SDK, AWS SNS, etc.)

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public void send(String recipient, String subject, String content) {
        log.info("Sending push notification to device: {}", recipient);

        // TODO: Implement push notification logic
        // Example with Firebase:
        // Message message = Message.builder()
        //     .setNotification(com.google.firebase.messaging.Notification.builder()
        //         .setTitle(subject)
        //         .setBody(content)
        //         .build())
        //     .setToken(recipient) // device token
        //     .build();
        // FirebaseMessaging.getInstance().send(message);

        log.debug("Push notification sent successfully to device: {}", recipient);
    }
}
