package com.ecommerce.common.test.security;

import com.ecommerce.common.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

/**
 * Factory for creating SecurityContext with mock user.
 */
public class WithMockUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal = UserPrincipal.builder()
                .id(UUID.fromString(annotation.userId()))
                .email(annotation.email())
                .role(annotation.role())
                .enabled(true)
                .accountNonLocked(true)
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

        context.setAuthentication(authentication);
        return context;
    }
}
