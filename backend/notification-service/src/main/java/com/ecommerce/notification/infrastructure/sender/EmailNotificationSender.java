package com.ecommerce.notification.infrastructure.sender;

import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Email notification sender implementation.
 * TODO: Implement actual email sending using AWS SES, SendGrid, or similar.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

    // TODO: Inject email client (AWS SES, JavaMailSender, SendGrid, etc.)

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(String recipient, String subject, String content) {
        log.info("Sending email notification to: {}", recipient);

        // TODO: Implement email sending logic
        // Example with AWS SES:
        // SendEmailRequest request = SendEmailRequest.builder()
        //     .destination(d -> d.toAddresses(recipient))
        //     .message(m -> m
        //         .subject(s -> s.data(subject))
        //         .body(b -> b.html(c -> c.data(content))))
        //     .source(fromAddress)
        //     .build();
        // sesClient.sendEmail(request);

        log.debug("Email notification sent successfully to: {}", recipient);
    }
}
