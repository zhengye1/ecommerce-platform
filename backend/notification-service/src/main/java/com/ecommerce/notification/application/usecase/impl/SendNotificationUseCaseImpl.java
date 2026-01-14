package com.ecommerce.notification.application.usecase.impl;

import com.ecommerce.notification.application.dto.request.SendNotificationRequest;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.mapper.NotificationMapper;
import com.ecommerce.notification.application.usecase.SendNotificationUseCase;
import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.model.NotificationStatus;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import com.ecommerce.notification.domain.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Implementation of SendNotificationUseCase.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SendNotificationUseCaseImpl implements SendNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final List<NotificationSender> notificationSenders;

    @Override
    public NotificationResponse execute(SendNotificationRequest request) {
        log.info("Sending notification to user {} via {}", request.getUserId(), request.getChannel());

        // TODO: Step 1 - Validate user preferences and opt-out settings
        // Check if user has opted out of this notification type/channel

        // TODO: Step 2 - Load and render template
        // Use templateName and templateVariables to render content
        String renderedContent = renderTemplate(request.getTemplateName(), request.getTemplateVariables());

        // Build notification entity
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .channel(request.getChannel())
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .content(renderedContent)
                .referenceId(request.getReferenceId())
                .referenceType(request.getReferenceType())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .build();

        // Save notification first
        Notification savedNotification = notificationRepository.save(notification);

        // TODO: Step 3 - Send notification via appropriate sender
        // Get the sender based on channel (EMAIL, SMS, PUSH)
        try {
            NotificationSender sender = getSenderForChannel(request.getChannel());

            // TODO: Implement actual sending logic
            sender.send(
                    savedNotification.getRecipient(),
                    savedNotification.getSubject(),
                    savedNotification.getContent()
            );

            // Mark as sent on success
            savedNotification.markSent();
            log.info("Notification {} sent successfully", savedNotification.getId());
        } catch (Exception e) {
            log.error("Failed to send notification {}: {}", savedNotification.getId(), e.getMessage());
            savedNotification.markFailed(e.getMessage());

            // TODO: Step 4 - Schedule retry if applicable
            // Check retry policy and schedule for retry if needed
        }

        // Save updated status
        savedNotification = notificationRepository.save(savedNotification);

        return notificationMapper.toResponse(savedNotification);
    }

    /**
     * Renders the template with given variables.
     * TODO: Implement template rendering using a template engine (Thymeleaf, FreeMarker, etc.)
     */
    private String renderTemplate(String templateName, Map<String, Object> variables) {
        if (templateName == null || templateName.isBlank()) {
            return null;
        }

        // TODO: Load template from storage (file system, database, S3)
        // TODO: Render template with variables using template engine

        // Placeholder implementation
        StringBuilder content = new StringBuilder("Template: ").append(templateName);
        if (variables != null && !variables.isEmpty()) {
            content.append(" with variables: ").append(variables);
        }
        return content.toString();
    }

    /**
     * Gets the appropriate sender for the channel.
     */
    private NotificationSender getSenderForChannel(NotificationChannel channel) {
        return notificationSenders.stream()
                .filter(sender -> sender.getChannel() == channel)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No sender configured for channel: " + channel));
    }
}
