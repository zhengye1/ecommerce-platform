package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Order aggregate.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByUserIdAndStatus(UUID userId, OrderStatus status, Pageable pageable);

    List<Order> findByStatusAndCreatedBefore(OrderStatus status, java.time.Instant before);

    boolean existsByOrderNumber(String orderNumber);

    long countByUserId(UUID userId);

    void deleteById(UUID id);
}
