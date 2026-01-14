package com.ecommerce.notification.application.usecase;

import com.ecommerce.notification.application.dto.request.SendNotificationRequest;
import com.ecommerce.notification.application.dto.response.NotificationResponse;

/**
 * Use case for sending notifications.
 */
public interface SendNotificationUseCase {

    /**
     * Send a notification.
     *
     * @param request the notification request
     * @return the notification response
     */
    NotificationResponse execute(SendNotificationRequest request);
}
