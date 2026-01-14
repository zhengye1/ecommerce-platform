package com.ecommerce.product.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.DuplicateResourceException;

/**
 * Exception thrown when attempting to create a product with a duplicate SKU.
 */
public class DuplicateSkuException extends DuplicateResourceException {

    public DuplicateSkuException(String sku) {
        super(ErrorCodes.PRODUCT_SKU_EXISTS, "Product already exists with SKU: %s", sku);
    }
}
