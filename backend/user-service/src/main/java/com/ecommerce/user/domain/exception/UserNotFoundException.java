package com.ecommerce.user.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID userId) {
        super(ErrorCodes.USER_NOT_FOUND, "User not found with id: " + userId);
    }

    public UserNotFoundException(String email) {
        super(ErrorCodes.USER_NOT_FOUND, "User not found with email: " + email);
    }
}
