package com.ecommerce.common.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Custom UserDetails implementation for JWT authentication.
 */
@Data
@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private UUID id;
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    private boolean accountNonLocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Create UserPrincipal from JWT token claims.
     */
    public static UserPrincipal fromToken(UUID userId, String email, String role) {
        return UserPrincipal.builder()
                .id(userId)
                .email(email)
                .role(role)
                .enabled(true)
                .accountNonLocked(true)
                .build();
    }
}
