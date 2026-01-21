package com.ecommerce.user.domain.service;

import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * User domain service interface.
 * Contains business logic that doesn't belong to the User entity.
 */
public interface UserDomainService {

    /**
     * Register a new user.
     */
    User registerUser(String email, String password, String firstName,
                      String lastName, Role role);

    /**
     * Authenticate user and return if credentials are valid.
     */
    Optional<User> authenticate(String email, String password);

    /**
     * Get user by ID.
     */
    Optional<User> getUserById(UUID id);

    /**
     * Get user by email.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Update user profile.
     */
    User updateProfile(UUID userId, String firstName, String lastName, String phone);

    /**
     * Change user password.
     */
    void changePassword(UUID userId, String currentPassword, String newPassword);

    /**
     * Activate user account.
     */
    User activateUser(UUID userId);

    /**
     * Deactivate user account.
     */
    User deactivateUser(UUID userId);

    /**
     * Suspend user account (admin only).
     */
    User suspendUser(UUID userId);

}
