package com.ecommerce.user.domain.service.impl;

import com.ecommerce.user.domain.exception.DuplicateEmailException;
import com.ecommerce.user.domain.exception.InvalidCredentialsException;
import com.ecommerce.user.domain.exception.UserNotFoundException;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.repository.UserRepository;
import com.ecommerce.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserDomainService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(String email, String password, String firstName,
                              String lastName, Role role) {
        log.info("Registering new user with email: {}", email);

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = User.create(email, passwordHash, firstName, lastName, role);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());

        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> authenticate(String email, String password) {
        log.debug("Authenticating user: {}", email);

        return userRepository.findByEmail(email)
                .filter(user -> {
                    if (!user.isActive()) {
                        log.warn("User account is not active: {}", email);
                        return false;
                    }
                    boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
                    if (!matches) {
                        log.warn("Invalid password for user: {}", email);
                    }
                    return matches;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateProfile(UUID userId, String firstName, String lastName, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateProfile(firstName, lastName, phone);
        return userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String newPasswordHash = passwordEncoder.encode(newPassword);
        user.changePassword(newPasswordHash);
        userRepository.save(user);

        log.info("Password changed for user: {}", userId);
    }

    @Override
    public User activateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.activate();
        return userRepository.save(user);
    }

    @Override
    public User deactivateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.deactivate();
        return userRepository.save(user);
    }

    @Override
    public User suspendUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.suspend();
        return userRepository.save(user);
    }

}
