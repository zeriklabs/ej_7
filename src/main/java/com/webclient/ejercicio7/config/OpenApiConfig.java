package com.webclient.ejercicio7.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API RESTful de Entrenadores - Nivel 3 de Richardson")
                        .version("1.0.0")
                        .description("API RESTful desarrollada con Arquitectura por Capas, implementando el Nivel 3 de Madurez de Richardson (HATEOAS con enlaces self, update, delete) y respuestas enriquecidas con ResponseEntity.")
                        .contact(new Contact()
                                .name("Desarrollo de Aplicaciones Web Client")
                                .email("contacto@universidad.edu"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
