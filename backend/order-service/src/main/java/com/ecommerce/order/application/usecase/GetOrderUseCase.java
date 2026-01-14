package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Use case for retrieving orders.
 */
public interface GetOrderUseCase {

    /**
     * Get order by ID.
     */
    OrderResponse getById(UUID orderId);

    /**
     * Get order by order number.
     */
    OrderResponse getByOrderNumber(String orderNumber);

    /**
     * List orders for a user.
     */
    Page<OrderResponse> listByUser(UUID userId, Pageable pageable);

    /**
     * List all orders (admin only).
     */
    Page<OrderResponse> listAll(Pageable pageable);
}
