package com.ecommerce.inventory.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.BusinessException;

import java.util.UUID;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(UUID productId, int requested, int available) {
        super(ErrorCodes.INVENTORY_INSUFFICIENT,
                "Insufficient stock for product %s: requested %d, available %d",
                productId, requested, available);
    }
}
