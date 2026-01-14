package com.ecommerce.common.test.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

/**
 * Reusable LocalStack Testcontainer configuration for AWS services.
 */
public abstract class LocalStackTestContainer {

    private static final LocalStackContainer LOCALSTACK_CONTAINER;

    static {
        LOCALSTACK_CONTAINER = new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:3.0"))
                .withServices(SQS, SNS, S3, DYNAMODB)
                .withReuse(true);

        LOCALSTACK_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.region", () -> LOCALSTACK_CONTAINER.getRegion());
        registry.add("aws.endpoint.override", () ->
                LOCALSTACK_CONTAINER.getEndpointOverride(SQS).toString());
        registry.add("aws.accessKeyId", () -> LOCALSTACK_CONTAINER.getAccessKey());
        registry.add("aws.secretAccessKey", () -> LOCALSTACK_CONTAINER.getSecretKey());
    }

    protected static LocalStackContainer getLocalStackContainer() {
        return LOCALSTACK_CONTAINER;
    }
}
