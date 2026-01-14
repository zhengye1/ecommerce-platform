package com.ecommerce.user.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import com.ecommerce.user.domain.event.UserCreatedEvent;
import com.ecommerce.user.domain.event.UserUpdatedEvent;
import jakarta.persistence.*;
import lombok.*;

/**
 * User aggregate root.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AggregateRoot {

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING;

    @Column(name = "email_verified")
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * Factory method to create a new user.
     */
    public static User create(String email, String passwordHash, String firstName,
                               String lastName, Role role) {
        User user = User.builder()
                .email(email.toLowerCase().trim())
                .passwordHash(passwordHash)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .status(UserStatus.PENDING)
                .build();

        user.registerEvent(UserCreatedEvent.of(user));
        return user;
    }

    /**
     * Get full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Activate the user account.
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.emailVerified = true;
        registerEvent(UserUpdatedEvent.of(this, "activated"));
    }

    /**
     * Deactivate the user account.
     */
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        registerEvent(UserUpdatedEvent.of(this, "deactivated"));
    }

    /**
     * Suspend the user account.
     */
    public void suspend() {
        this.status = UserStatus.SUSPENDED;
        registerEvent(UserUpdatedEvent.of(this, "suspended"));
    }

    /**
     * Check if user is active.
     */
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * Update profile information.
     */
    public void updateProfile(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        registerEvent(UserUpdatedEvent.of(this, "profile_updated"));
    }

    /**
     * Change password.
     */
    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        registerEvent(UserUpdatedEvent.of(this, "password_changed"));
    }
}
