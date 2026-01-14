package com.ecommerce.common.messaging.publisher;

import com.ecommerce.common.messaging.event.DomainEvent;
import com.ecommerce.common.messaging.event.EventEnvelope;
import com.ecommerce.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * SNS implementation of EventPublisher.
 * Publishes domain events to SNS topics for fan-out to multiple consumers.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SnsEventPublisher implements EventPublisher {

    private static final String DATA_TYPE_STRING = "String";

    private final SnsClient snsClient;

    @Value("${app.messaging.sns.topic-arn-prefix:}")
    private String topicArnPrefix;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Override
    public void publish(String topicName, DomainEvent event) {
        publish(topicName, event, null);
    }

    @Override
    public void publish(String topicName, DomainEvent event, String messageGroupId) {
        try {
            EventEnvelope<DomainEvent> envelope = EventEnvelope.wrap(
                    event,
                    event.getEventType(),
                    serviceName
            );

            String messageBody = JsonUtils.toJson(envelope);
            String topicArn = resolveTopicArn(topicName);

            PublishRequest.Builder requestBuilder = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(messageBody)
                    .messageAttributes(buildMessageAttributes(event));

            // Add message group ID for FIFO topics
            if (messageGroupId != null) {
                requestBuilder.messageGroupId(messageGroupId);
                requestBuilder.messageDeduplicationId(event.getEventId().toString());
            }

            PublishResponse response = snsClient.publish(requestBuilder.build());

            log.info("Published event {} to topic {} with messageId {}",
                    event.getEventType(), topicName, response.messageId());

        } catch (Exception e) {
            log.error("Failed to publish event {} to topic {}",
                    event.getEventType(), topicName, e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    private String resolveTopicArn(String topicName) {
        if (topicName.startsWith("arn:aws:sns:")) {
            return topicName;
        }
        return topicArnPrefix + topicName;
    }

    private Map<String, MessageAttributeValue> buildMessageAttributes(DomainEvent event) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();

        attributes.put("eventType", MessageAttributeValue.builder()
                .dataType(DATA_TYPE_STRING)
                .stringValue(event.getEventType())
                .build());

        attributes.put("aggregateType", MessageAttributeValue.builder()
                .dataType(DATA_TYPE_STRING)
                .stringValue(event.getAggregateType())
                .build());

        attributes.put("source", MessageAttributeValue.builder()
                .dataType(DATA_TYPE_STRING)
                .stringValue(serviceName)
                .build());

        if (event.getCorrelationId() != null) {
            attributes.put("correlationId", MessageAttributeValue.builder()
                    .dataType(DATA_TYPE_STRING)
                    .stringValue(event.getCorrelationId())
                    .build());
        }

        return attributes;
    }
}
