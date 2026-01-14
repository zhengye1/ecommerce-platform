package com.ecommerce.common.util;

import com.ecommerce.common.constant.AppConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Validation utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(AppConstants.EMAIL_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(AppConstants.PHONE_PATTERN);
    private static final Pattern SKU_PATTERN = Pattern.compile(AppConstants.SKU_PATTERN);

    /**
     * Check if email format is valid.
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Check if phone number format is valid (E.164 format).
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.isNotBlank(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Check if SKU format is valid.
     */
    public static boolean isValidSku(String sku) {
        return StringUtils.isNotBlank(sku) && SKU_PATTERN.matcher(sku).matches();
    }

    /**
     * Check if string is a valid UUID.
     */
    public static boolean isValidUuid(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parse string to UUID or return null.
     */
    public static UUID parseUuidOrNull(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Check if collection is null or empty.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not null and not empty.
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Require non-blank string or throw exception.
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    /**
     * Require positive number or throw exception.
     */
    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        return value;
    }

    /**
     * Require non-negative number or throw exception.
     */
    public static int requireNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
        return value;
    }
}
