package com.ecommerce.common.messaging.consumer;

import com.ecommerce.common.messaging.event.DomainEvent;

/**
 * Interface for handling domain events.
 * Implement this for each event type you want to consume.
 *
 * @param <T> the type of event to handle
 */
public interface EventHandler<T extends DomainEvent> {

    /**
     * Handle the incoming event.
     *
     * @param event the event to handle
     */
    void handle(T event);

    /**
     * Get the event type this handler processes.
     */
    Class<T> getEventType();

    /**
     * Get the event type name for routing.
     */
    String getEventTypeName();
}
