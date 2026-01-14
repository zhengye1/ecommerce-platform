package com.ecommerce.common.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT configuration properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /**
     * Secret key for signing JWT tokens.
     * Should be at least 256 bits (32 characters) for HS256.
     */
    private String secret;

    /**
     * Access token expiration time in milliseconds.
     * Default: 15 minutes (900000ms)
     */
    private long accessTokenExpiration = 900000;

    /**
     * Refresh token expiration time in milliseconds.
     * Default: 7 days (604800000ms)
     */
    private long refreshTokenExpiration = 604800000;

    /**
     * Token issuer.
     */
    private String issuer = "ecommerce-platform";

    /**
     * Header name for JWT token.
     */
    private String headerName = "Authorization";

    /**
     * Token prefix (e.g., "Bearer ").
     */
    private String tokenPrefix = "Bearer ";
}
