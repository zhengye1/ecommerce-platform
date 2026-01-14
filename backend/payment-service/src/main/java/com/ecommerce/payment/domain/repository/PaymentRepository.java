package com.ecommerce.payment.domain.repository;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Payment aggregate.
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    Optional<Payment> findByOrderId(UUID orderId);

    Page<Payment> findByUserId(UUID userId, Pageable pageable);

    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    List<Payment> findByOrderIdIn(List<UUID> orderIds);

    boolean existsByOrderId(UUID orderId);

    void deleteById(UUID id);
}
