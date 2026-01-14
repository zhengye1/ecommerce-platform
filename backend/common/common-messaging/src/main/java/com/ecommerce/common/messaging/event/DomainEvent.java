package com.ecommerce.common.messaging.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events.
 * Contains common metadata for event tracking and processing.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class DomainEvent {

    /**
     * Unique event identifier.
     */
    private UUID eventId;

    /**
     * Type of the event (e.g., "OrderCreated", "PaymentCompleted").
     */
    private String eventType;

    /**
     * Aggregate ID that generated this event.
     */
    private UUID aggregateId;

    /**
     * Aggregate type (e.g., "Order", "Payment").
     */
    private String aggregateType;

    /**
     * Timestamp when the event occurred.
     */
    private Instant occurredAt;

    /**
     * Version of the event schema for backwards compatibility.
     */
    private int version;

    /**
     * Correlation ID for tracing across services.
     */
    private String correlationId;

    /**
     * User ID who triggered the action (if applicable).
     */
    private UUID triggeredBy;

    /**
     * Constructor for subclasses to initialize event with type and aggregate ID.
     */
    protected DomainEvent(String eventType, String aggregateId) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.aggregateId = UUID.fromString(aggregateId);
        this.occurredAt = Instant.now();
        this.version = 1;
    }

    /**
     * Initialize common event fields.
     */
    protected void initializeEvent(String eventType, UUID aggregateId, String aggregateType) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.occurredAt = Instant.now();
        this.version = 1;
    }
}
