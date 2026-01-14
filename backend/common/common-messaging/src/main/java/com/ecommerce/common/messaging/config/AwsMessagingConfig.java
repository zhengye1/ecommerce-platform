package com.ecommerce.common.messaging.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

/**
 * AWS messaging configuration for SNS and SQS clients.
 */
@Configuration
public class AwsMessagingConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Value("${aws.endpoint.override:#{null}}")
    private String endpointOverride;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Bean
    public SnsClient snsClient(AwsCredentialsProvider credentialsProvider) {
        var builder = SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider);

        // For LocalStack or local development
        if (endpointOverride != null && !endpointOverride.isBlank()) {
            builder.endpointOverride(URI.create(endpointOverride));
        }

        return builder.build();
    }

    @Bean
    public SqsClient sqsClient(AwsCredentialsProvider credentialsProvider) {
        var builder = SqsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider);

        // For LocalStack or local development
        if (endpointOverride != null && !endpointOverride.isBlank()) {
            builder.endpointOverride(URI.create(endpointOverride));
        }

        return builder.build();
    }
}
