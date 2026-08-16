package com.web_service.sm_rest_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource")
public record DatabaseConfigProperties(
    String url,
    String username,
    String password
) {}
