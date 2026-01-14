package com.ecommerce.payment.application.usecase.impl;

import com.ecommerce.payment.application.dto.request.RefundRequest;
import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.mapper.PaymentMapper;
import com.ecommerce.payment.application.usecase.RefundPaymentUseCase;
import com.ecommerce.payment.domain.exception.PaymentNotFoundException;
import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.payment.domain.service.PaymentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of RefundPaymentUseCase.
 *
 * TODO: Implement the following business logic:
 * - Validate refund amount doesn't exceed original payment
 * - Validate refund policy (time window, partial refund rules)
 * - Call payment provider to process refund
 * - Handle partial refunds
 * - Notify order-service about refund for order status update
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RefundPaymentUseCaseImpl implements RefundPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final List<PaymentProvider> paymentProviders;

    @Override
    public PaymentResponse execute(UUID paymentId, RefundRequest request) {
        log.info("Processing refund for payment: {} amount: {}", paymentId, request.getAmount());

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // Validate payment can be refunded
        if (!payment.canBeRefunded()) {
            throw new IllegalStateException("Payment cannot be refunded in status: " + payment.getStatus());
        }

        // TODO: Validate refund policy
        // - Check if within refund time window
        // - Check partial refund rules

        // TODO: Process refund with payment provider
        // PaymentProvider provider = findProvider(payment);
        // String refundTransactionId = provider.processRefund(
        //         payment.getProviderTransactionId(),
        //         request.getAmount()
        // );

        // Process refund (updates status and registers event)
        payment.refund(request.getAmount());

        // TODO: Notify order-service about refund
        // orderServiceClient.notifyRefund(payment.getOrderId(), request.getAmount());

        Payment saved = paymentRepository.save(payment);
        log.info("Refund processed for payment: {}", paymentId);

        return paymentMapper.toResponse(saved);
    }
}
