package com.ecommerce.product.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a product is updated.
 */
@Getter
public class ProductUpdatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PRODUCT_UPDATED";

    private final UUID productId;
    private final String sku;
    private final String name;
    private final BigDecimal price;

    public ProductUpdatedEvent(UUID productId, String sku, String name, BigDecimal price) {
        super(EVENT_TYPE, productId.toString());
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.price = price;
    }
}
