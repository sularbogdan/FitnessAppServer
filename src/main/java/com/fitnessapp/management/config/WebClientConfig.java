package com.fitnessapp.management.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient spoonacularWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.spoonacular.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
