package com.ecommerce.inventory.domain.model;

import com.ecommerce.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Stock reservation entity for tracking order reservations.
 */
@Entity
@Table(name = "stock_reservations")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StockReservation extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.ACTIVE;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "released_at")
    private Instant releasedAt;

    public enum ReservationStatus {
        ACTIVE,
        CONFIRMED,
        RELEASED,
        EXPIRED
    }

    public static StockReservation create(UUID orderId, UUID productId, int quantity, Instant expiresAt) {
        return StockReservation.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .status(ReservationStatus.ACTIVE)
                .expiresAt(expiresAt)
                .build();
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
        this.confirmedAt = Instant.now();
    }

    public void release() {
        this.status = ReservationStatus.RELEASED;
        this.releasedAt = Instant.now();
    }

    public void expire() {
        this.status = ReservationStatus.EXPIRED;
        this.releasedAt = Instant.now();
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt) && isActive();
    }
}
