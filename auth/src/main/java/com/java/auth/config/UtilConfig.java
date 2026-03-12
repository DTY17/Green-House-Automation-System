package com.java.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.java.auth.component")
public class UtilConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
