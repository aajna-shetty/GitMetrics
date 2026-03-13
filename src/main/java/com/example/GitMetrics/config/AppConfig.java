package com.example.GitMetrics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {
    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "GitMetrics-App")
                .build();
    }
}
