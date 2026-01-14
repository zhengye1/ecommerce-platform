package com.ecommerce.common.test.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Reusable Redis Testcontainer configuration.
 */
public abstract class RedisTestContainer {

    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
                .withExposedPorts(6379)
                .withReuse(true);

        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    protected static GenericContainer<?> getRedisContainer() {
        return REDIS_CONTAINER;
    }
}
