package com.ecommerce.notification.domain.service;

import com.ecommerce.notification.domain.model.NotificationChannel;

/**
 * Strategy interface for notification senders.
 * Implement this for each channel (Email, SMS, Push, etc.)
 *
 * TODO: Implement concrete senders:
 * - EmailNotificationSender (using JavaMailSender or AWS SES)
 * - SmsNotificationSender (using Twilio or AWS SNS)
 * - PushNotificationSender (using Firebase or AWS SNS)
 */
public interface NotificationSender {

    /**
     * Get the channel this sender supports.
     */
    NotificationChannel getChannel();

    /**
     * Send a notification.
     *
     * @param recipient the recipient (email, phone number, device token)
     * @param subject the notification subject
     * @param content the notification content
     * @throws NotificationException if sending fails
     */
    void send(String recipient, String subject, String content);

    /**
     * Exception thrown when notification sending fails.
     */
    class NotificationException extends RuntimeException {
        public NotificationException(String message) {
            super(message);
        }

        public NotificationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
