package com.ecommerce.notification.infrastructure.sender;

import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SMS notification sender implementation.
 * TODO: Implement actual SMS sending using AWS SNS, Twilio, or similar.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationSender implements NotificationSender {

    // TODO: Inject SMS client (AWS SNS, Twilio, etc.)

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public void send(String recipient, String subject, String content) {
        log.info("Sending SMS notification to: {}", recipient);

        // TODO: Implement SMS sending logic
        // Example with AWS SNS:
        // PublishRequest request = PublishRequest.builder()
        //     .phoneNumber(recipient)
        //     .message(content)
        //     .build();
        // snsClient.publish(request);

        // Example with Twilio:
        // Message.creator(
        //     new PhoneNumber(recipient),
        //     new PhoneNumber(fromNumber),
        //     content
        // ).create();

        log.debug("SMS notification sent successfully to: {}", recipient);
    }
}
