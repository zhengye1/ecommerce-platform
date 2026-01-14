package com.ecommerce.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Application-wide constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

    // ============ Pagination ============
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final int MAX_PAGE_SIZE = 100;

    // ============ Headers ============
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";

    // ============ Cache Keys ============
    public static final String CACHE_USER_PREFIX = "user:";
    public static final String CACHE_PRODUCT_PREFIX = "product:";
    public static final String CACHE_CART_PREFIX = "cart:";

    // ============ Cache TTL (seconds) ============
    public static final long CACHE_TTL_SHORT = 300;       // 5 minutes
    public static final long CACHE_TTL_MEDIUM = 1800;     // 30 minutes
    public static final long CACHE_TTL_LONG = 3600;       // 1 hour
    public static final long CACHE_TTL_DAY = 86400;       // 24 hours

    // ============ Date Formats ============
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // ============ Regex Patterns ============
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String PHONE_PATTERN = "^\\+?[1-9]\\d{1,14}$";
    public static final String SKU_PATTERN = "^[A-Z0-9-]{3,50}$";
}
