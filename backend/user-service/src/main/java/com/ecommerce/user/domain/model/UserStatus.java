package com.ecommerce.user.domain.model;

/**
 * User account status.
 */
public enum UserStatus {
    /**
     * Account created but email not verified.
     */
    PENDING,

    /**
     * Account is active and can be used.
     */
    ACTIVE,

    /**
     * Account has been deactivated by user.
     */
    INACTIVE,

    /**
     * Account has been suspended by admin.
     */
    SUSPENDED
}
