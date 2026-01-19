package com.ecommerce.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration - enables auditing for createdAt/updatedAt fields.
 * Separated from main Application class to avoid issues with @WebMvcTest.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
