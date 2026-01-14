package com.ecommerce.notification.application.usecase.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.mapper.NotificationMapper;
import com.ecommerce.notification.application.usecase.GetNotificationUseCase;
import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetNotificationUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetNotificationUseCaseImpl implements GetNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse getById(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        return notificationMapper.toResponse(notification);
    }

    @Override
    public Page<NotificationResponse> getByUserId(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(notificationMapper::toResponse);
    }
}
