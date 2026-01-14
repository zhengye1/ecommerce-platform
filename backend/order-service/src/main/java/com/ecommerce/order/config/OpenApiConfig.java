package com.ecommerce.order.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for Order Service.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order Service API",
                description = "Order management, status tracking, and saga orchestration",
                version = "1.0.0",
                contact = @Contact(
                        name = "Platform Team",
                        email = "platform@ecommerce.com"
                )
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
