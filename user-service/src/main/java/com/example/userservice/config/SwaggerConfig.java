package com.example.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "User Service API specifications for MSA",
                     description = "User Service API specifications with spring boot 3.2 + spring cloud.",
                     version ="v1.0.0")
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customTestOpenAPI() {
        String[] paths = {"/users/**", "/welcome", "/health-check"};
        return GroupedOpenApi.builder()
                             .group("msa-spring-cloud-v1.0")
                             .pathsToMatch(paths)
                             .build();
    }
}
