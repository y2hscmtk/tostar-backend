package com.winner_cat.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermitAllPathsConfig {

    @Bean
    public String[] permitAllPaths() {
        return new String[]{
                "/login", "/join", "/oauth2/**", "/ci",
                "/api/article/today-error", "/api/scream/**",
                "/api/article/all", "/api/article/tag/**",
                "/api/article/today-error", "/api/article/recommend/**"
        };
    }
}