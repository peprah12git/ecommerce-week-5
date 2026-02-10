package com.smartcommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 configuration for SmartCommerce API documentation.
 * Swagger UI available at: /api/swagger-ui.html
 * OpenAPI JSON spec at:    /api/v3/api-docs
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SmartCommerce API",
                version = "1.0.0",
                description = "RESTful API for the SmartCommerce e-commerce platform. "
                        + "Provides endpoints for managing products, categories, and users "
                        + "with support for pagination, filtering, and sorting.",
                contact = @Contact(
                        name = "SmartCommerce Team",
                        email = "support@smartcommerce.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Local Development Server")
        }
)
public class OpenApiConfig {

}
