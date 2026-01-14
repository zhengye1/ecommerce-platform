package com.ecommerce.notification.application.dto.request;

import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    @NotBlank(message = "Recipient is required")
    private String recipient;

    private String subject;

    private String templateName;

    private Map<String, Object> templateVariables;

    private String referenceId;

    private String referenceType;
}
