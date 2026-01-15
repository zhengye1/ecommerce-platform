package com.ecommerce.product.domain.model;

/**
 * Seller account status.
 */
public enum SellerStatus {
    /**
     * Pending verification.
     */
    PENDING,

    /**
     * Verified and active.
     */
    VERIFIED,

    /**
     * Suspended by admin.
     */
    SUSPENDED,

    /**
     * Closed by seller.
     */
    CLOSED
}
