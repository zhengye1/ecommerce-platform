package com.ecommerce.common.test.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

/**
 * Meta-annotation for integration tests.
 * Automatically activates the "test" profile.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest
@ActiveProfiles("test")
public @interface IntegrationTest {
}
