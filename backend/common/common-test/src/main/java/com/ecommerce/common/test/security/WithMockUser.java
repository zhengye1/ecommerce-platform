package com.ecommerce.common.test.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * Annotation to create a mock authenticated user for testing.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {

    /**
     * User ID (UUID string).
     */
    String userId() default "00000000-0000-0000-0000-000000000001";

    /**
     * User email.
     */
    String email() default "test@example.com";

    /**
     * User role (without ROLE_ prefix).
     */
    String role() default "USER";
}
