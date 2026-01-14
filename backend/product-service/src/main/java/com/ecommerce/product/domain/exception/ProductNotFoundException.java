package com.ecommerce.product.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when a product is not found.
 */
public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(UUID productId) {
        super(ErrorCodes.PRODUCT_NOT_FOUND, "Product not found with id: %s", productId);
    }

    public ProductNotFoundException(String sku) {
        super(ErrorCodes.PRODUCT_NOT_FOUND, "Product not found with SKU: %s", sku);
    }
}
