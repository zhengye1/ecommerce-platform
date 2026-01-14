package com.ecommerce.common.security.filter;

import com.ecommerce.common.security.UserPrincipal;
import com.ecommerce.common.security.jwt.JwtTokenProvider;
import com.ecommerce.common.security.jwt.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * JWT authentication filter that validates tokens and sets security context.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // Check if token is blacklisted (logged out)
                if (tokenBlacklistService.isBlacklisted(token)) {
                    log.debug("Token is blacklisted");
                } else {
                    setAuthentication(token, request);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return jwtTokenProvider.extractTokenFromHeader(bearerToken);
    }

    private void setAuthentication(String token, HttpServletRequest request) {
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        String email = jwtTokenProvider.getEmailFromToken(token);
        String role = jwtTokenProvider.getRoleFromToken(token);

        UserPrincipal userPrincipal = UserPrincipal.fromToken(userId, email, role);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Set authentication for user: {}", email);
    }
}
