package com.ecommerce.order.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a new order is created.
 */
@Getter
public class OrderCreatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "ORDER_CREATED";

    private final UUID orderId;
    private final String orderNumber;
    private final UUID userId;
    private final BigDecimal totalAmount;

    public OrderCreatedEvent(UUID orderId, String orderNumber, UUID userId, BigDecimal totalAmount) {
        super(EVENT_TYPE, orderId.toString());
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
}
