package com.ecommerce.payment.infrastructure.persistence;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentStatus;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpaRepository;

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return jpaRepository.findByOrderId(orderId);
    }

    @Override
    public Page<Payment> findByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Payment> findByStatus(PaymentStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable);
    }

    @Override
    public List<Payment> findByOrderIdIn(List<UUID> orderIds) {
        return jpaRepository.findByOrderIdIn(orderIds);
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return jpaRepository.existsByOrderId(orderId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
