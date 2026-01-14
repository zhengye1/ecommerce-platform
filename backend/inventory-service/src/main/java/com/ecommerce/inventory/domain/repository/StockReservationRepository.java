package com.ecommerce.inventory.domain.repository;

import com.ecommerce.inventory.domain.model.StockReservation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockReservationRepository {

    StockReservation save(StockReservation reservation);

    Optional<StockReservation> findById(UUID id);

    List<StockReservation> findByOrderId(UUID orderId);

    List<StockReservation> findActiveByOrderId(UUID orderId);

    List<StockReservation> findExpiredReservations(Instant before);

    void deleteByOrderId(UUID orderId);
}
