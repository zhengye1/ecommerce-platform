package com.ecommerce.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for aggregate roots that can publish domain events.
 * Extends BaseEntity with domain event support.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class AggregateRoot extends BaseEntity {

    @Getter(onMethod_ = @DomainEvents)
    private final transient List<Object> domainEvents = new ArrayList<>();

    /**
     * Register a domain event to be published when the aggregate is saved.
     */
    protected void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    /**
     * Clear all domain events after they have been published.
     */
    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * Get unmodifiable list of registered events.
     */
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
