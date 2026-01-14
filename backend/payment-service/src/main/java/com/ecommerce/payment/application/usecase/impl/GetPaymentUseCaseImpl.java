package com.ecommerce.payment.application.usecase.impl;

import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.mapper.PaymentMapper;
import com.ecommerce.payment.application.usecase.GetPaymentUseCase;
import com.ecommerce.payment.domain.exception.PaymentNotFoundException;
import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetPaymentUseCase.
 *
 * TODO: Implement access control - users can only see their own payments.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPaymentUseCaseImpl implements GetPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse getById(UUID paymentId) {
        // TODO: Add access control
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getByOrderId(UUID orderId) {
        // TODO: Add access control
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId.toString()));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public Page<PaymentResponse> listByUser(UUID userId, Pageable pageable) {
        // TODO: Validate current user matches userId or is admin
        return paymentRepository.findByUserId(userId, pageable)
                .map(paymentMapper::toResponse);
    }
}
