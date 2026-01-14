package com.ecommerce.payment.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a payment is completed successfully.
 */
@Getter
public class PaymentCompletedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PaymentCompleted";

    private final UUID paymentId;
    private final UUID orderId;
    private final BigDecimal amount;

    public PaymentCompletedEvent(UUID paymentId, UUID orderId, BigDecimal amount) {
        super(EVENT_TYPE, paymentId.toString());
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
