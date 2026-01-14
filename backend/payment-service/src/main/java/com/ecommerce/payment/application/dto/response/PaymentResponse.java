package com.ecommerce.payment.application.dto.response;

import com.ecommerce.payment.domain.model.PaymentMethod;
import com.ecommerce.payment.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for payment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private UUID id;
    private UUID orderId;
    private UUID userId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String providerName;
    private String providerTransactionId;
    private String failureReason;
    private BigDecimal refundAmount;
    private Instant refundedAt;
    private Instant completedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
