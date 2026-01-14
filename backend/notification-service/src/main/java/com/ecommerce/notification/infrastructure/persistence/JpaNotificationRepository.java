package com.ecommerce.notification.infrastructure.persistence;

import com.ecommerce.notification.domain.model.NotificationStatus;
import com.ecommerce.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    Page<NotificationJpaEntity> findByUserId(UUID userId, Pageable pageable);

    Page<NotificationJpaEntity> findByStatus(NotificationStatus status, Pageable pageable);

    List<NotificationJpaEntity> findByStatusAndRetryCountLessThan(NotificationStatus status, int maxRetries);

    List<NotificationJpaEntity> findByReferenceIdAndReferenceType(String referenceId, String referenceType);

    long countByUserIdAndCreatedAtAfter(UUID userId, Instant after);
}
