package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.response.OrderResponse;

import java.util.UUID;

/**
 * Use case for cancelling an order.
 * This triggers compensation actions in the saga.
 */
public interface CancelOrderUseCase {

    /**
     * Cancel an order.
     *
     * @param orderId the order ID
     * @param reason the cancellation reason
     * @return the cancelled order response
     */
    OrderResponse execute(UUID orderId, String reason);
}
