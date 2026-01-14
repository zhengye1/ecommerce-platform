package com.ecommerce.user.infrastructure.persistence;

import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import com.ecommerce.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements the domain UserRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email.toLowerCase().trim());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email.toLowerCase().trim());
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.delete(user);
    }

    @Override
    public long countByStatus(UserStatus status) {
        return jpaUserRepository.countByStatus(status);
    }
}
