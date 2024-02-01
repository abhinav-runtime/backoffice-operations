package com.backoffice.operations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

