package com.ecommerce.notification.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.response.PageResponse;
import com.ecommerce.notification.application.dto.request.SendNotificationRequest;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.usecase.GetNotificationUseCase;
import com.ecommerce.notification.application.usecase.SendNotificationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final GetNotificationUseCase getNotificationUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationResponse response = sendNotificationUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Notification sent successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotification(@PathVariable UUID id) {
        NotificationResponse response = getNotificationUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getUserNotifications(
            @PathVariable UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<NotificationResponse> page = getNotificationUseCase.getByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }
}
