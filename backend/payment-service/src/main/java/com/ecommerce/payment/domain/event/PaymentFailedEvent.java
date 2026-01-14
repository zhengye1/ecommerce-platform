package com.ecommerce.payment.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.util.UUID;

/**
 * Event published when a payment fails.
 */
@Getter
public class PaymentFailedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PaymentFailed";

    private final UUID paymentId;
    private final UUID orderId;
    private final String reason;

    public PaymentFailedEvent(UUID paymentId, UUID orderId, String reason) {
        super(EVENT_TYPE, paymentId.toString());
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.reason = reason;
    }
}
