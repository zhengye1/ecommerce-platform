package com.ecommerce.notification.application.usecase;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.notification.application.dto.request.SendNotificationRequest;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.mapper.NotificationMapper;
import com.ecommerce.notification.application.usecase.impl.SendNotificationUseCaseImpl;
import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.domain.model.NotificationChannel;
import com.ecommerce.notification.domain.model.NotificationStatus;
import com.ecommerce.notification.domain.model.NotificationType;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import com.ecommerce.notification.domain.service.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("SendNotificationUseCase Tests")
class SendNotificationUseCaseTest extends BaseUnitTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationSender emailSender;

    private SendNotificationUseCaseImpl sendNotificationUseCase;

    private SendNotificationRequest request;
    private Notification savedNotification;
    private NotificationResponse expectedResponse;

    @BeforeEach
    void setUp() {
        // Manually construct with List of senders
        sendNotificationUseCase = new SendNotificationUseCaseImpl(
                notificationRepository,
                notificationMapper,
                List.of(emailSender)
        );

        request = SendNotificationRequest.builder()
                .userId(UUID.randomUUID())
                .type(NotificationType.ORDER_CONFIRMATION)
                .channel(NotificationChannel.EMAIL)
                .recipient("test@example.com")
                .subject("Order Confirmation")
                .templateName("order-confirmation")
                .templateVariables(Map.of("orderId", "ORD-123"))
                .build();

        savedNotification = Notification.builder()
                .userId(request.getUserId())
                .type(NotificationType.ORDER_CONFIRMATION)
                .channel(NotificationChannel.EMAIL)
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .content("Your order has been confirmed.")
                .status(NotificationStatus.PENDING)
                .build();
        savedNotification.setId(UUID.randomUUID());

        expectedResponse = new NotificationResponse();
        expectedResponse.setId(savedNotification.getId());
        expectedResponse.setStatus(NotificationStatus.PENDING);

        // Mock sender supports EMAIL
        when(emailSender.getChannel()).thenReturn(NotificationChannel.EMAIL);
    }

    @Test
    @DisplayName("should create notification with PENDING status")
    void shouldCreateNotificationWithPendingStatus() {
        // Given
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(notificationMapper.toResponse(any(Notification.class))).thenReturn(expectedResponse);

        // When
        NotificationResponse result = sendNotificationUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(NotificationStatus.PENDING);

        verify(notificationRepository, atLeastOnce()).save(any(Notification.class));
    }

    @Test
    @DisplayName("should save notification with correct recipient")
    void shouldSaveNotificationWithCorrectRecipient() {
        // Given
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> {
            Notification notification = inv.getArgument(0);
            assertThat(notification.getRecipient()).isEqualTo("test@example.com");
            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
            return savedNotification;
        });
        when(notificationMapper.toResponse(any(Notification.class))).thenReturn(expectedResponse);

        // When
        sendNotificationUseCase.execute(request);

        // Then
        verify(notificationRepository, atLeastOnce()).save(any(Notification.class));
    }
}
