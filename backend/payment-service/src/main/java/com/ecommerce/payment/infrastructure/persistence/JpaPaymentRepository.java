package com.ecommerce.payment.infrastructure.persistence;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);

    Page<Payment> findByUserId(UUID userId, Pageable pageable);

    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    List<Payment> findByOrderIdIn(List<UUID> orderIds);

    boolean existsByOrderId(UUID orderId);
}
