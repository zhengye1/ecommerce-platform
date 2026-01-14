package com.ecommerce.inventory.application.usecase.impl;

import com.ecommerce.inventory.application.dto.request.ReserveStockRequest;
import com.ecommerce.inventory.application.dto.response.ReservationResponse;
import com.ecommerce.inventory.application.usecase.ReserveStockUseCase;
import com.ecommerce.inventory.domain.exception.InsufficientStockException;
import com.ecommerce.inventory.domain.model.InventoryItem;
import com.ecommerce.inventory.domain.model.StockReservation;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.domain.repository.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ReserveStockUseCase.
 *
 * This is a critical saga step for order processing:
 * 1. Validate all items have sufficient stock
 * 2. Reserve stock for each item atomically
 * 3. Create reservation records with expiry
 *
 * TODO: Implement the following business logic:
 * - Distributed locking for concurrent reservations
 * - Reservation expiry handling (scheduled job)
 * - Partial reservation handling (all-or-nothing vs best-effort)
 * - Integration with warehouse management system
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReserveStockUseCaseImpl implements ReserveStockUseCase {

    private final InventoryRepository inventoryRepository;
    private final StockReservationRepository reservationRepository;

    private static final int RESERVATION_EXPIRY_MINUTES = 15;

    @Override
    public ReservationResponse execute(ReserveStockRequest request) {
        log.info("Reserving stock for order: {}", request.getOrderId());

        // TODO: Implement distributed lock for concurrent reservations
        // distributedLock.acquire("order:" + request.getOrderId());

        List<ReservationResponse.ReservedItem> reservedItems = new ArrayList<>();
        Instant expiresAt = Instant.now().plus(RESERVATION_EXPIRY_MINUTES, ChronoUnit.MINUTES);
        boolean allReserved = true;

        for (ReserveStockRequest.ReservationItem item : request.getItems()) {
            try {
                Optional<InventoryItem> inventoryOpt = inventoryRepository.findByProductId(item.getProductId());

                if (inventoryOpt.isEmpty()) {
                    reservedItems.add(ReservationResponse.ReservedItem.builder()
                            .productId(item.getProductId())
                            .quantityReserved(0)
                            .reserved(false)
                            .failureReason("Product not found in inventory")
                            .build());
                    allReserved = false;
                    continue;
                }

                InventoryItem inventory = inventoryOpt.get();

                // Try to reserve
                inventory.reserve(item.getQuantity());
                inventoryRepository.save(inventory);

                // Create reservation record
                StockReservation reservation = StockReservation.create(
                        request.getOrderId(),
                        item.getProductId(),
                        item.getQuantity(),
                        expiresAt
                );
                reservationRepository.save(reservation);

                reservedItems.add(ReservationResponse.ReservedItem.builder()
                        .productId(item.getProductId())
                        .quantityReserved(item.getQuantity())
                        .reserved(true)
                        .build());

                log.debug("Reserved {} units for product {}", item.getQuantity(), item.getProductId());

            } catch (InsufficientStockException e) {
                reservedItems.add(ReservationResponse.ReservedItem.builder()
                        .productId(item.getProductId())
                        .quantityReserved(0)
                        .reserved(false)
                        .failureReason(e.getMessage())
                        .build());
                allReserved = false;
            }
        }

        // TODO: If not all reserved and using all-or-nothing strategy, rollback
        // if (!allReserved) {
        //     rollbackReservations(request.getOrderId());
        // }

        return ReservationResponse.builder()
                .orderId(request.getOrderId())
                .success(allReserved)
                .message(allReserved ? "All items reserved" : "Some items could not be reserved")
                .items(reservedItems)
                .expiresAt(expiresAt)
                .build();
    }
}
