package com.ecommerce.user.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.DuplicateResourceException;

/**
 * Exception thrown when attempting to register with an existing email.
 */
public class DuplicateEmailException extends DuplicateResourceException {

    public DuplicateEmailException(String email) {
        super(ErrorCodes.USER_EMAIL_EXISTS, "Email already registered: " + email);
    }
}
