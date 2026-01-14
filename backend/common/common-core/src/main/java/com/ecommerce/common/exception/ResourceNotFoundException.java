package com.ecommerce.common.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BusinessException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ERROR_CODE, "%s not found with %s: %s", resourceName, fieldName, fieldValue);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        this(resourceName, "id", id);
    }

    public ResourceNotFoundException(String message) {
        super(ERROR_CODE, message);
    }

    protected ResourceNotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}