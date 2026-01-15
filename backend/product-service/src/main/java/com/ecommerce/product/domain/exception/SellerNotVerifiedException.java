package com.ecommerce.product.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.BusinessException;

import java.util.UUID;

/**
 * Exception thrown when a seller is not verified and cannot list products.
 */
public class SellerNotVerifiedException extends BusinessException {

    public SellerNotVerifiedException(UUID sellerId) {
        super(ErrorCodes.SELLER_NOT_VERIFIED, "Seller is not verified and cannot list products: %s", sellerId);
    }
}
