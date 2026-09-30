package com.flood.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DatabaseConfig.java
 * 
 * Spring Boot database and JPA configuration.
 * Enables JPA repositories and transaction management for MySQL / JPA operations.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.flood.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}
