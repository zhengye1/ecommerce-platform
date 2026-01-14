package com.ecommerce.user.domain.repository;

import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;

import java.util.Optional;
import java.util.UUID;

/**
 * User repository port (interface).
 * Implementation is in infrastructure layer.
 */
public interface UserRepository {

    /**
     * Save a user.
     */
    User save(User user);

    /**
     * Find user by ID.
     */
    Optional<User> findById(UUID id);

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Delete a user.
     */
    void delete(User user);

    /**
     * Count users by status.
     */
    long countByStatus(UserStatus status);
}
