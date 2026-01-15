package com.ecommerce.product.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when a seller is not found.
 */
public class SellerNotFoundException extends ResourceNotFoundException {

    public SellerNotFoundException(UUID sellerId) {
        super(ErrorCodes.SELLER_NOT_FOUND, "Seller not found with id: %s", sellerId);
    }
}
