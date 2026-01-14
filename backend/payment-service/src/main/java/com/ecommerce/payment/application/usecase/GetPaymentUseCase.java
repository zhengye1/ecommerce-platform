package com.ecommerce.payment.application.usecase;

import com.ecommerce.payment.application.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Use case for retrieving payments.
 */
public interface GetPaymentUseCase {

    PaymentResponse getById(UUID paymentId);

    PaymentResponse getByOrderId(UUID orderId);

    Page<PaymentResponse> listByUser(UUID userId, Pageable pageable);
}
