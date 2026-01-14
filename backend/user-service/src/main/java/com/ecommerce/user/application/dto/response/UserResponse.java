package com.ecommerce.user.application.dto.response;

import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for user data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private Role role;
    private UserStatus status;
    private boolean emailVerified;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
