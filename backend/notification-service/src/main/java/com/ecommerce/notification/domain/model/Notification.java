package com.ecommerce.notification.domain.model;

import com.ecommerce.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Notification entity for tracking sent notifications.
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "template_name", length = 100)
    private String templateName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "retry_count")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    public static Notification create(UUID userId, NotificationType type, NotificationChannel channel,
                                       String recipient, String subject, String content) {
        return Notification.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(type)
                .channel(channel)
                .recipient(recipient)
                .subject(subject)
                .content(content)
                .status(NotificationStatus.PENDING)
                .build();
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markFailed(String reason) {
        this.status = NotificationStatus.FAILED;
        this.failureReason = reason;
        this.retryCount++;
    }

    public boolean canRetry() {
        return retryCount < 3 && status == NotificationStatus.FAILED;
    }
}
