package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for Order entity.
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByUserIdAndStatus(UUID userId, OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :before")
    List<Order> findByStatusAndCreatedBefore(
            @Param("status") OrderStatus status,
            @Param("before") Instant before);

    boolean existsByOrderNumber(String orderNumber);
}
