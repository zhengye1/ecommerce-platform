package com.ecommerce.payment.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when a payment is not found.
 */
public class PaymentNotFoundException extends ResourceNotFoundException {

    public PaymentNotFoundException(UUID paymentId) {
        super(ErrorCodes.PAYMENT_NOT_FOUND, "Payment not found with id: %s", paymentId);
    }

    public PaymentNotFoundException(String orderId) {
        super(ErrorCodes.PAYMENT_NOT_FOUND, "Payment not found for order: %s", orderId);
    }
}
