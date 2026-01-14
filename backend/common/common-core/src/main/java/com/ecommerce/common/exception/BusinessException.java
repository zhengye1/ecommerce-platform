package com.ecommerce.common.exception;

import lombok.Getter;

/**
 * Base exception for all business exceptions in the platform.
 * All domain-specific exceptions should extend this class.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    private final String errorCode;
    private final transient Object[] args;

    protected BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = new Object[0];
    }

    protected BusinessException(String errorCode, String message, Object... args) {
        super(String.format(message, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    protected BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = new Object[0];
    }
}