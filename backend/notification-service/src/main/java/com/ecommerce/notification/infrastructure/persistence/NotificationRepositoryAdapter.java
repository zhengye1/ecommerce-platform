package com.ecommerce.notification.infrastructure.persistence;

import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.domain.model.NotificationStatus;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import com.ecommerce.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private static final int MAX_RETRIES = 3;

    private final JpaNotificationRepository jpaRepository;
    private final NotificationEntityMapper mapper;

    @Override
    public Notification save(Notification notification) {
        NotificationJpaEntity entity = mapper.toEntity(notification);
        NotificationJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Notification> findByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable).map(mapper::toDomain);
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
        return jpaRepository.findByStatus(status, Pageable.unpaged())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findPendingRetries() {
        return jpaRepository.findByStatusAndRetryCountLessThan(NotificationStatus.FAILED, MAX_RETRIES)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
