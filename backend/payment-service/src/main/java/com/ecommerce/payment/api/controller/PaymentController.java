package com.ecommerce.payment.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.response.PageResponse;
import com.ecommerce.payment.application.dto.request.ProcessPaymentRequest;
import com.ecommerce.payment.application.dto.request.RefundRequest;
import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.usecase.GetPaymentUseCase;
import com.ecommerce.payment.application.usecase.ProcessPaymentUseCase;
import com.ecommerce.payment.application.usecase.RefundPaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final RefundPaymentUseCase refundPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;

    @PostMapping
    @Operation(summary = "Process a payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody ProcessPaymentRequest request) {
        PaymentResponse response = processPaymentUseCase.execute(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable UUID id) {
        PaymentResponse response = getPaymentUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(@PathVariable UUID orderId) {
        PaymentResponse response = getPaymentUseCase.getByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-payments")
    @Operation(summary = "List current user's payments")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getMyPayments(
            @AuthenticationPrincipal UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PaymentResponse> page = getPaymentUseCase.listByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Process a refund (Admin only)")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @PathVariable UUID id,
            @Valid @RequestBody RefundRequest request) {
        PaymentResponse response = refundPaymentUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
