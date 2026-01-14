package com.ecommerce.payment.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a payment is refunded.
 */
@Getter
public class PaymentRefundedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "PaymentRefunded";

    private final UUID paymentId;
    private final UUID orderId;
    private final BigDecimal refundAmount;

    public PaymentRefundedEvent(UUID paymentId, UUID orderId, BigDecimal refundAmount) {
        super(EVENT_TYPE, paymentId.toString());
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.refundAmount = refundAmount;
    }
}
