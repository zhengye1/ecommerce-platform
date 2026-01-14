package com.ecommerce.user.domain.event;

import com.ecommerce.common.messaging.event.DomainEvent;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Event published when a user is updated.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class UserUpdatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "user.updated";

    private UUID userId;
    private String email;
    private UserStatus status;
    private String changeType;

    public static UserUpdatedEvent of(User user, String changeType) {
        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .changeType(changeType)
                .build();

        event.initializeEvent(EVENT_TYPE, user.getId(), "User");
        return event;
    }
}
