package com.ecommerce.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT token provider for creating and validating tokens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Generate an access token for the given user.
     */
    public String generateAccessToken(UUID userId, String email, String role) {
        return generateToken(userId, email, role, jwtProperties.getAccessTokenExpiration());
    }

    /**
     * Generate a refresh token for the given user.
     */
    public String generateRefreshToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .issuer(jwtProperties.getIssuer())
                .id(UUID.randomUUID().toString())
                .signWith(secretKey)
                .compact();
    }

    private String generateToken(UUID userId, String email, String role, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(userId.toString())
                .claims(Map.of(
                        "email", email,
                        "role", role
                ))
                .issuedAt(now)
                .expiration(expiryDate)
                .issuer(jwtProperties.getIssuer())
                .id(UUID.randomUUID().toString())
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extract user ID from token.
     */
    public UUID getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return UUID.fromString(claims.getSubject());
    }

    /**
     * Extract email from token.
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("email", String.class);
    }

    /**
     * Extract role from token.
     */
    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Extract JWT ID (jti) from token.
     */
    public String getJtiFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getId();
    }

    /**
     * Validate token and return true if valid.
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get token expiration date.
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract token from Authorization header.
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return authHeader.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }
}
