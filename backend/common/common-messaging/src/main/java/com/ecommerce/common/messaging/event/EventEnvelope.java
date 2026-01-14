package com.ecommerce.common.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Wrapper for events when publishing to message queues.
 * Contains the event payload plus metadata for routing and processing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope<T> {

    /**
     * Unique message identifier.
     */
    private UUID messageId;

    /**
     * Event type for routing (e.g., "order.created", "payment.completed").
     */
    private String eventType;

    /**
     * Source service that published the event.
     */
    private String source;

    /**
     * Event payload.
     */
    private T payload;

    /**
     * Timestamp when the event was published.
     */
    private Instant timestamp;

    /**
     * Additional headers/metadata.
     */
    private Map<String, String> headers;

    /**
     * Create an envelope for an event.
     */
    public static <T> EventEnvelope<T> wrap(T event, String eventType, String source) {
        return EventEnvelope.<T>builder()
                .messageId(UUID.randomUUID())
                .eventType(eventType)
                .source(source)
                .payload(event)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create an envelope with additional headers.
     */
    public static <T> EventEnvelope<T> wrap(T event, String eventType, String source,
                                             Map<String, String> headers) {
        return EventEnvelope.<T>builder()
                .messageId(UUID.randomUUID())
                .eventType(eventType)
                .source(source)
                .payload(event)
                .timestamp(Instant.now())
                .headers(headers)
                .build();
    }
}
