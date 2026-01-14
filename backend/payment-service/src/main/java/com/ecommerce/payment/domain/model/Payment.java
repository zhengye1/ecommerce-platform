package com.ecommerce.payment.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import com.ecommerce.payment.domain.event.PaymentCompletedEvent;
import com.ecommerce.payment.domain.event.PaymentFailedEvent;
import com.ecommerce.payment.domain.event.PaymentRefundedEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Payment aggregate root.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends AggregateRoot {

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "provider_transaction_id")
    private String providerTransactionId;

    @Column(name = "provider_name", length = 50)
    private String providerName;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    /**
     * Create a new payment.
     */
    public static Payment create(UUID orderId, UUID userId, BigDecimal amount,
                                  String currency, PaymentMethod paymentMethod) {
        return Payment.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .userId(userId)
                .amount(amount)
                .currency(currency)
                .paymentMethod(paymentMethod)
                .status(PaymentStatus.PENDING)
                .build();
    }

    /**
     * Mark payment as processing.
     */
    public void startProcessing(String providerName) {
        this.status = PaymentStatus.PROCESSING;
        this.providerName = providerName;
    }

    /**
     * Complete the payment.
     */
    public void complete(String providerTransactionId) {
        this.status = PaymentStatus.COMPLETED;
        this.providerTransactionId = providerTransactionId;
        this.completedAt = Instant.now();
        registerEvent(new PaymentCompletedEvent(this.getId(), this.orderId, this.amount));
    }

    /**
     * Mark payment as failed.
     */
    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        registerEvent(new PaymentFailedEvent(this.getId(), this.orderId, reason));
    }

    /**
     * Process a refund.
     */
    public void refund(BigDecimal refundAmount) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Can only refund completed payments");
        }
        if (refundAmount.compareTo(this.amount) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed payment amount");
        }

        this.refundAmount = refundAmount;
        this.refundedAt = Instant.now();
        this.status = refundAmount.compareTo(this.amount) == 0
                ? PaymentStatus.REFUNDED
                : PaymentStatus.PARTIALLY_REFUNDED;

        registerEvent(new PaymentRefundedEvent(this.getId(), this.orderId, refundAmount));
    }

    /**
     * Check if payment can be refunded.
     */
    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED || status == PaymentStatus.PARTIALLY_REFUNDED;
    }
}
