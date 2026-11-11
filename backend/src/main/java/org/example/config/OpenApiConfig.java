package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadata shown at the top of the Swagger UI (/swagger-ui.html). */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI houseMateOpenApi() {
        return new OpenAPI().info(new Info()
                .title("HouseMate API")
                .version("1.0")
                .description("Backend of the HouseMate app: users, houses and shared expenses."));
    }
}
