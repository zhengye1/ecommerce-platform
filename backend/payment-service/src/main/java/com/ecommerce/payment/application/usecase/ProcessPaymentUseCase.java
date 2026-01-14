package com.ecommerce.payment.application.usecase;

import com.ecommerce.payment.application.dto.request.ProcessPaymentRequest;
import com.ecommerce.payment.application.dto.response.PaymentResponse;

import java.util.UUID;

/**
 * Use case for processing a payment.
 */
public interface ProcessPaymentUseCase {

    /**
     * Process a payment for an order.
     *
     * @param userId the ID of the user making the payment
     * @param request the payment request
     * @return the payment response
     */
    PaymentResponse execute(UUID userId, ProcessPaymentRequest request);
}
