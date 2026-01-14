package com.ecommerce.inventory.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import com.ecommerce.inventory.domain.event.StockUpdatedEvent;
import com.ecommerce.inventory.domain.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Inventory item aggregate root.
 */
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem extends AggregateRoot {

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "quantity_available", nullable = false)
    @Builder.Default
    private Integer quantityAvailable = 0;

    @Column(name = "quantity_reserved", nullable = false)
    @Builder.Default
    private Integer quantityReserved = 0;

    @Column(name = "reorder_level")
    @Builder.Default
    private Integer reorderLevel = 10;

    @Column(name = "reorder_quantity")
    @Builder.Default
    private Integer reorderQuantity = 50;

    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation;

    /**
     * Create a new inventory item.
     */
    public static InventoryItem create(UUID productId, String sku, int initialQuantity) {
        return InventoryItem.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .sku(sku)
                .quantityAvailable(initialQuantity)
                .quantityReserved(0)
                .build();
    }

    /**
     * Get total quantity (available + reserved).
     */
    public int getTotalQuantity() {
        return quantityAvailable + quantityReserved;
    }

    /**
     * Check if item is in stock.
     */
    public boolean isInStock() {
        return quantityAvailable > 0;
    }

    /**
     * Check if item needs reorder.
     */
    public boolean needsReorder() {
        return getTotalQuantity() <= reorderLevel;
    }

    /**
     * Reserve stock for an order.
     */
    public void reserve(int quantity) {
        if (quantity > quantityAvailable) {
            throw new InsufficientStockException(productId, quantity, quantityAvailable);
        }
        this.quantityAvailable -= quantity;
        this.quantityReserved += quantity;
    }

    /**
     * Release reserved stock (order cancelled).
     */
    public void releaseReservation(int quantity) {
        int toRelease = Math.min(quantity, quantityReserved);
        this.quantityReserved -= toRelease;
        this.quantityAvailable += toRelease;
    }

    /**
     * Confirm reservation (order shipped).
     */
    public void confirmReservation(int quantity) {
        int toConfirm = Math.min(quantity, quantityReserved);
        this.quantityReserved -= toConfirm;
        registerEvent(new StockUpdatedEvent(this.productId, this.quantityAvailable, this.quantityReserved));
    }

    /**
     * Add stock (restocking).
     */
    public void addStock(int quantity) {
        this.quantityAvailable += quantity;
        registerEvent(new StockUpdatedEvent(this.productId, this.quantityAvailable, this.quantityReserved));
    }

    /**
     * Adjust stock (inventory correction).
     */
    public void adjustStock(int newQuantity) {
        this.quantityAvailable = newQuantity;
        registerEvent(new StockUpdatedEvent(this.productId, this.quantityAvailable, this.quantityReserved));
    }
}
