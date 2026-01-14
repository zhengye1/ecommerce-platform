package com.ecommerce.order.domain.model;

/**
 * Order status enumeration.
 */
public enum OrderStatus {
    PENDING,        // Order created, awaiting payment
    CONFIRMED,      // Payment successful
    PROCESSING,     // Order being prepared
    SHIPPED,        // Order shipped
    DELIVERED,      // Order delivered
    CANCELLED,      // Order cancelled
    REFUNDED        // Order refunded
}
