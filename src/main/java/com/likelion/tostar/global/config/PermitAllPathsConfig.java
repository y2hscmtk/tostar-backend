package com.likelion.tostar.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermitAllPathsConfig {

    @Bean
    public String[] permitAllPaths() {
        return new String[]{
                "/api/user/login", "/api/user/join", "/ws/**",
        };
    }
}