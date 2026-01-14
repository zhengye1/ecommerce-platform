package com.ecommerce.order.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import com.ecommerce.order.domain.model.OrderStatus;
import lombok.Getter;

import java.util.UUID;

/**
 * Event published when order status changes.
 */
@Getter
public class OrderStatusChangedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "ORDER_STATUS_CHANGED";

    private final UUID orderId;
    private final String orderNumber;
    private final OrderStatus previousStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(UUID orderId, String orderNumber,
                                    OrderStatus previousStatus, OrderStatus newStatus) {
        super(EVENT_TYPE, orderId.toString());
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }
}
