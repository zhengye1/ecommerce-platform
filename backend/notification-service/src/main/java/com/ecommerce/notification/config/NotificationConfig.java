package com.ecommerce.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class NotificationConfig {

    // TODO: Configure template engine for notification rendering (Thymeleaf, FreeMarker)

    // TODO: Configure AWS SES client for email sending

    // TODO: Configure AWS SNS client for SMS and push notifications

    // TODO: Configure retry policy for failed notifications
}
