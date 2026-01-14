package com.ecommerce.inventory.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StockUpdatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "StockUpdated";

    private final UUID productId;
    private final int quantityAvailable;
    private final int quantityReserved;

    public StockUpdatedEvent(UUID productId, int quantityAvailable, int quantityReserved) {
        super(EVENT_TYPE, productId.toString());
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
        this.quantityReserved = quantityReserved;
    }
}
