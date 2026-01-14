package com.ecommerce.payment.domain.model;

/**
 * Payment status enumeration.
 */
public enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED
}
