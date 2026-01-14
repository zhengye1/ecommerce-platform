package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing OrderRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaRepository;

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Page<Order> findByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<Order> findByStatusAndCreatedBefore(OrderStatus status, Instant before) {
        return jpaRepository.findByStatusAndCreatedBefore(status, before);
    }

    @Override
    public Page<Order> findByUserIdAndStatus(UUID userId, OrderStatus status, Pageable pageable) {
        return jpaRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return jpaRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public long countByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
