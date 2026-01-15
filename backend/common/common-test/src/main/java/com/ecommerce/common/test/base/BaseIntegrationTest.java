package com.ecommerce.common.test.base;

import com.ecommerce.common.test.container.MySqlTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base class for integration tests with MySQL Testcontainer.
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest extends MySqlTestContainer {
    // Common integration test utilities can be added here
}
