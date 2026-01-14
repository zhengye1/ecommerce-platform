package com.ecommerce.notification.application.dto.response;

import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.model.NotificationStatus;
import com.ecommerce.notification.domain.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private UUID id;
    private UUID userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String recipient;
    private String subject;
    private String content;
    private NotificationStatus status;
    private String failureReason;
    private Integer retryCount;
    private Instant sentAt;
    private Instant createdAt;
}
