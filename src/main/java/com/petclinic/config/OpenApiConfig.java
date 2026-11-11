package com.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for API documentation.
 */
@Configuration
public final class OpenApiConfig {

    @Bean
    public OpenAPI petClinicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pet Clinic API")
                        .description("REST API for Pet Clinic management system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pet Clinic Support")
                                .email("support@petclinic.com")));
    }
}
