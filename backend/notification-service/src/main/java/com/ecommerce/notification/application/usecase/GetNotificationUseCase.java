package com.ecommerce.notification.application.usecase;

import com.ecommerce.notification.application.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Use case for retrieving notifications.
 */
public interface GetNotificationUseCase {

    NotificationResponse getById(UUID id);

    Page<NotificationResponse> getByUserId(UUID userId, Pageable pageable);
}
