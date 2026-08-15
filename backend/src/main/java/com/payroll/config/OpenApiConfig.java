package com.payroll.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================================
 * OpenAPI / Swagger Documentation Configuration
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Configures global Swagger OpenAPI documentation UI (`/swagger-ui.html`).
 * Registers JWT Bearer token authentication scheme into Swagger UI interactive endpoint header.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Abstraction: Exposes REST API contract interfaces abstracting internal database structures.
 * - Encapsulation: Groups OpenAPI security schema definitions within a single configuration component.
 * 
 * Design Patterns Used:
 * --------------------
 * - Builder Pattern: Uses OpenAPI object builders to construct documentation models.
 * - Factory Bean Pattern: {@code @Bean} method produces a singleton OpenAPI bean for Spring container context.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    /**
     * Customizes OpenAPI bean with Enterprise Payroll metadata and JWT Bearer security requirements.
     *
     * @return Fully configured {@link OpenAPI} object for Swagger UI rendering.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Payroll Management System API")
                        .version("1.0.0")
                        .description("RESTful API Backend for Payroll, Attendance, Employees, Leaves, Departments, and User Role Security.")
                        .contact(new Contact()
                                .name("Enterprise Engineering Team")
                                .email("support@payroll.com"))
                        .license(new License().name("Apache 2.0").url("https://spring.io")))
                // Attach global Security Requirement for JWT Bearer Tokens
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token to authorize REST API endpoints.")));
    }
}
