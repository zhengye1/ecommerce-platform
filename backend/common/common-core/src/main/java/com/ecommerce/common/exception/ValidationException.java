package com.ecommerce.common.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when validation fails.
 */
@Getter
public class ValidationException extends BusinessException {

    private static final String ERROR_CODE = "VALIDATION_ERROR";

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(ERROR_CODE, message);
        this.errors = Collections.emptyMap();
    }

    public ValidationException(Map<String, String> errors) {
        super(ERROR_CODE, "Validation failed");
        this.errors = errors;
    }

    public ValidationException(String field, String message) {
        super(ERROR_CODE, message);
        this.errors = Map.of(field, message);
    }
}