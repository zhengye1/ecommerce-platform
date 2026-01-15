package com.ecommerce.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Centralized error codes for consistent error handling across services.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {

    // ============ Common Errors (1000-1999) ============
    public static final String INTERNAL_ERROR = "ERR_1000";
    public static final String VALIDATION_ERROR = "ERR_1001";
    public static final String NOT_FOUND = "ERR_1002";
    public static final String DUPLICATE = "ERR_1003";
    public static final String UNAUTHORIZED = "ERR_1004";
    public static final String FORBIDDEN = "ERR_1005";
    public static final String BAD_REQUEST = "ERR_1006";
    public static final String CONFLICT = "ERR_1007";

    // ============ User Errors (2000-2999) ============
    public static final String USER_NOT_FOUND = "ERR_2000";
    public static final String USER_EMAIL_EXISTS = "ERR_2001";
    public static final String USER_INVALID_CREDENTIALS = "ERR_2002";
    public static final String USER_ACCOUNT_DISABLED = "ERR_2003";
    public static final String USER_ACCOUNT_LOCKED = "ERR_2004";

    // ============ Product Errors (3000-3999) ============
    public static final String PRODUCT_NOT_FOUND = "ERR_3000";
    public static final String PRODUCT_SKU_EXISTS = "ERR_3001";
    public static final String PRODUCT_OUT_OF_STOCK = "ERR_3002";
    public static final String PRODUCT_INACTIVE = "ERR_3003";
    public static final String CATEGORY_NOT_FOUND = "ERR_3100";
    public static final String SELLER_NOT_FOUND = "ERR_3200";
    public static final String SELLER_NOT_VERIFIED = "ERR_3201";

    // ============ Order Errors (4000-4999) ============
    public static final String ORDER_NOT_FOUND = "ERR_4000";
    public static final String ORDER_INVALID_STATUS = "ERR_4001";
    public static final String ORDER_ALREADY_CANCELLED = "ERR_4002";
    public static final String ORDER_CANNOT_MODIFY = "ERR_4003";
    public static final String CART_EMPTY = "ERR_4100";
    public static final String CART_ITEM_NOT_FOUND = "ERR_4101";

    // ============ Payment Errors (5000-5999) ============
    public static final String PAYMENT_NOT_FOUND = "ERR_5000";
    public static final String PAYMENT_FAILED = "ERR_5001";
    public static final String PAYMENT_ALREADY_PROCESSED = "ERR_5002";
    public static final String PAYMENT_REFUND_FAILED = "ERR_5003";
    public static final String PAYMENT_INVALID_AMOUNT = "ERR_5004";

    // ============ Inventory Errors (6000-6999) ============
    public static final String INVENTORY_NOT_FOUND = "ERR_6000";
    public static final String INVENTORY_INSUFFICIENT = "ERR_6001";
    public static final String INVENTORY_RESERVATION_FAILED = "ERR_6002";
    public static final String INVENTORY_ALREADY_RESERVED = "ERR_6003";
}
