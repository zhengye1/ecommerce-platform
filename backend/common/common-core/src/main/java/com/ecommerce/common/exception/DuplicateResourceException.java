package com.ecommerce.common.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends BusinessException {

    private static final String ERROR_CODE = "DUPLICATE_RESOURCE";

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(ERROR_CODE, "%s already exists with %s: %s", resourceName, fieldName, fieldValue);
    }

    public DuplicateResourceException(String message) {
        super(ERROR_CODE, message);
    }

    protected DuplicateResourceException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}