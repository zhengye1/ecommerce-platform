package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaStockReservationRepository extends JpaRepository<StockReservation, UUID> {

    List<StockReservation> findByOrderId(UUID orderId);

    @Query("SELECT r FROM StockReservation r WHERE r.orderId = :orderId AND r.status = 'ACTIVE'")
    List<StockReservation> findActiveByOrderId(@Param("orderId") UUID orderId);

    @Query("SELECT r FROM StockReservation r WHERE r.status = 'ACTIVE' AND r.expiresAt < :before")
    List<StockReservation> findExpiredReservations(@Param("before") Instant before);

    void deleteByOrderId(UUID orderId);
}
