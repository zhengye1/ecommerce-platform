package com.ecommerce.order.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.order.domain.model.OrderStatus;

/**
 * Exception thrown when an order status transition is invalid.
 */
public class InvalidOrderStatusException extends BusinessException {

    public InvalidOrderStatusException(OrderStatus currentStatus, String operation) {
        super(ErrorCodes.ORDER_INVALID_STATUS,
                "Cannot %s order with status: %s", operation, currentStatus);
    }
}
