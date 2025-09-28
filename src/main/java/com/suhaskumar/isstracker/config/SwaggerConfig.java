package com.suhaskumar.isstracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI issTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("ISS Real-Time Tracker API")
                        .description("API for tracking the real-time and historical position of the International Space Station.")
                        .version("v1.0"));
    }
}
