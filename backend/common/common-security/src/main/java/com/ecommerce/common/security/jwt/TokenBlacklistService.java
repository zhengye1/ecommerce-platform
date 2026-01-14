package com.ecommerce.common.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 * Service for managing token blacklist in Redis.
 * Used for logout and token revocation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Add a token to the blacklist.
     * Token will be automatically removed when it expires.
     */
    public void blacklistToken(String token) {
        try {
            String jti = jwtTokenProvider.getJtiFromToken(token);
            Date expiration = jwtTokenProvider.getExpirationFromToken(token);

            long ttlMillis = expiration.getTime() - System.currentTimeMillis();
            if (ttlMillis > 0) {
                String key = BLACKLIST_PREFIX + jti;
                redisTemplate.opsForValue().set(key, "revoked", Duration.ofMillis(ttlMillis));
                log.debug("Token blacklisted: {}", jti);
            }
        } catch (Exception e) {
            log.error("Failed to blacklist token", e);
        }
    }

    /**
     * Check if a token is blacklisted.
     */
    public boolean isBlacklisted(String token) {
        try {
            String jti = jwtTokenProvider.getJtiFromToken(token);
            String key = BLACKLIST_PREFIX + jti;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Failed to check token blacklist", e);
            return false;
        }
    }
}
