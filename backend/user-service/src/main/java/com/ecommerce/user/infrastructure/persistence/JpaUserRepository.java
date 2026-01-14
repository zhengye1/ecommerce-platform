package com.ecommerce.user.infrastructure.persistence;

import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for User entity.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByStatus(UserStatus status);
}
