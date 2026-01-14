package com.ecommerce.user.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Event published when a new user is created.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class UserCreatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "user.created";

    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    public static UserCreatedEvent of(User user) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();

        event.initializeEvent(EVENT_TYPE, user.getId(), "User");
        return event;
    }
}
