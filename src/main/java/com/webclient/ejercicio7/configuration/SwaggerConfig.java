package com.webclient.ejercicio7.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API RESTful de Gestión de Vehículos",
                version = "1.0.0",
                description = "API RESTful desarrollada en Spring Boot 3 con Arquitectura por Capas, soporte HATEOAS (Nivel 3 del Modelo de Madurez de Richardson), validación estricta de parámetros inválidos y documentación completa con OpenAPI 3 / Swagger.",
                termsOfService = "https://www.kranio.io/terminos",
                contact = @Contact(
                        name = "Equipo de Desarrollo - Vehículos API",
                        email = "soporte.vehiculos@kranio.io",
                        url = "https://www.kranio.io"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local de Desarrollo"),
                @Server(url = "https://ejercicio7.onrender.com", description = "Servidor en la Nube (Render)")
        }
)
public class SwaggerConfig {
}
