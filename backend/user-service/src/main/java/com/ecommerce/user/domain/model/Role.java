package com.ecommerce.user.domain.model;

/**
 * User roles in the system.
 */
public enum Role {
    /**
     * Regular customer.
     */
    CUSTOMER,

    /**
     * Seller/Merchant.
     */
    SELLER,

    /**
     * Platform administrator.
     */
    ADMIN,

    /**
     * Super administrator with full access.
     */
    SUPER_ADMIN
}
