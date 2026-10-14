package com.webclient.ejercicio7.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            if (datasourceUrl != null && datasourceUrl.contains("postgresql")) {
                hibernateProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            } else {
                hibernateProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
            }
        };
    }
}
