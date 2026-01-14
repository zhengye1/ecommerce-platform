package com.ecommerce.payment.application.usecase;

import com.ecommerce.payment.application.dto.request.RefundRequest;
import com.ecommerce.payment.application.dto.response.PaymentResponse;

import java.util.UUID;

/**
 * Use case for refunding a payment.
 */
public interface RefundPaymentUseCase {

    /**
     * Process a refund for a payment.
     *
     * @param paymentId the payment ID
     * @param request the refund request
     * @return the updated payment response
     */
    PaymentResponse execute(UUID paymentId, RefundRequest request);
}
