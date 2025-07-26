package de.boniel.apps.restaurantlocator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name}") String title,
                                 @Value("${spring.application.api-version}") String apiVersion,
                                 @Value("${spring.application.description}") String description) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(apiVersion)
                        .description(description)
                );
    }
}
