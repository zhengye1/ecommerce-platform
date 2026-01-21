package com.ecommerce.user.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.BusinessException;

/**
 * Exception thrown when authentication fails due to invalid credentials.
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super(ErrorCodes.USER_INVALID_CREDENTIALS, "Invalid email or password");
    }
    public InvalidCredentialsException(String message){
        super(ErrorCodes.USER_INVALID_CREDENTIALS, message);
    }
}
