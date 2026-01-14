package com.ecommerce.order.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(UUID orderId) {
        super(ErrorCodes.ORDER_NOT_FOUND, "Order not found with id: %s", orderId);
    }

    public OrderNotFoundException(String orderNumber) {
        super(ErrorCodes.ORDER_NOT_FOUND, "Order not found with number: %s", orderNumber);
    }
}
