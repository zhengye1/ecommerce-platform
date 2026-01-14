package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.domain.model.OrderStatus;

import java.util.UUID;

/**
 * Use case for updating order status.
 * Used by saga handlers and admin operations.
 */
public interface UpdateOrderStatusUseCase {

    /**
     * Confirm order after successful payment.
     */
    OrderResponse confirmOrder(UUID orderId, UUID paymentId);

    /**
     * Mark order as processing.
     */
    OrderResponse startProcessing(UUID orderId);

    /**
     * Mark order as shipped.
     */
    OrderResponse shipOrder(UUID orderId);

    /**
     * Mark order as delivered.
     */
    OrderResponse deliverOrder(UUID orderId);
}
