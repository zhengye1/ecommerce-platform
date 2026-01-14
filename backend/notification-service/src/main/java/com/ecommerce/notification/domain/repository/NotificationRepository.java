package com.ecommerce.notification.domain.repository;

import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.domain.model.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    Page<Notification> findByUserId(UUID userId, Pageable pageable);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findPendingRetries();
}
