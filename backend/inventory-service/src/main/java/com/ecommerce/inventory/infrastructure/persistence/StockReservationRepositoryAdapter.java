package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.StockReservation;
import com.ecommerce.inventory.domain.repository.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StockReservationRepositoryAdapter implements StockReservationRepository {

    private final JpaStockReservationRepository jpaRepository;

    @Override
    public StockReservation save(StockReservation reservation) {
        return jpaRepository.save(reservation);
    }

    @Override
    public Optional<StockReservation> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<StockReservation> findByOrderId(UUID orderId) {
        return jpaRepository.findByOrderId(orderId);
    }

    @Override
    public List<StockReservation> findActiveByOrderId(UUID orderId) {
        return jpaRepository.findActiveByOrderId(orderId);
    }

    @Override
    public List<StockReservation> findExpiredReservations(Instant before) {
        return jpaRepository.findExpiredReservations(before);
    }

    @Override
    public void deleteByOrderId(UUID orderId) {
        jpaRepository.deleteByOrderId(orderId);
    }
}
