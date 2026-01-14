package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.request.CreateOrderRequest;
import com.ecommerce.order.application.dto.response.OrderResponse;

import java.util.UUID;

/**
 * Use case for creating a new order.
 * This initiates the order saga workflow.
 */
public interface CreateOrderUseCase {

    /**
     * Create a new order for the given user.
     *
     * @param userId the ID of the user placing the order
     * @param request the order creation request
     * @return the created order response
     */
    OrderResponse execute(UUID userId, CreateOrderRequest request);
}
