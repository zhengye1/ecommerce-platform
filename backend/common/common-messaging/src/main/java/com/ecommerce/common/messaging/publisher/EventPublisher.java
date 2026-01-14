package com.ecommerce.common.messaging.publisher;

import com.ecommerce.common.messaging.event.DomainEvent;

/**
 * Interface for publishing domain events.
 * Implementations can use SNS, SQS, or other message brokers.
 */
public interface EventPublisher {

    /**
     * Publish an event to the specified topic.
     *
     * @param topicName the name of the topic to publish to
     * @param event     the domain event to publish
     */
    void publish(String topicName, DomainEvent event);

    /**
     * Publish an event with a specific message group ID (for FIFO ordering).
     *
     * @param topicName       the name of the topic
     * @param event           the domain event to publish
     * @param messageGroupId  the message group ID for FIFO ordering
     */
    void publish(String topicName, DomainEvent event, String messageGroupId);
}
