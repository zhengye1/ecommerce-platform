package com.ecommerce.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class for accessing security context.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    /**
     * Get the current authenticated user principal.
     */
    public static Optional<UserPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return Optional.of(userPrincipal);
        }

        return Optional.empty();
    }

    /**
     * Get the current authenticated user's ID.
     */
    public static Optional<UUID> getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getId);
    }

    /**
     * Get the current authenticated user's email.
     */
    public static Optional<String> getCurrentUserEmail() {
        return getCurrentUser().map(UserPrincipal::getEmail);
    }

    /**
     * Get the current authenticated user's role.
     */
    public static Optional<String> getCurrentUserRole() {
        return getCurrentUser().map(UserPrincipal::getRole);
    }

    /**
     * Check if current user has a specific role.
     */
    public static boolean hasRole(String role) {
        return getCurrentUserRole()
                .map(r -> r.equalsIgnoreCase(role))
                .orElse(false);
    }

    /**
     * Check if current user is an admin.
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Check if a user is authenticated.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserPrincipal;
    }
}
