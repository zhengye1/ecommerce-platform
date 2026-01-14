package com.ecommerce.product.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.util.UUID;

/**
 * Event published when product stock changes.
 */
@Getter
public class ProductStockChangedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_STOCK_CHANGED";

    private final UUID productId;
    private final String sku;
    private final int previousQuantity;
    private final int newQuantity;
    private final boolean lowStock;

    public ProductStockChangedEvent(UUID productId, String sku, int previousQuantity,
                                     int newQuantity, boolean lowStock) {
        super(EVENT_TYPE, productId.toString());
        this.productId = productId;
        this.sku = sku;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.lowStock = lowStock;
    }
}
