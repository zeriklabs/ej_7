package com.webclient.ejercicio7.config;

import com.webclient.ejercicio7.model.Entrenador;
import com.webclient.ejercicio7.repository.EntrenadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(EntrenadorRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new Entrenador("Carlos Morales", "Fútbol", 8, "carlos.morales@gym.com"),
                        new Entrenador("Ana Gómez", "Natación", 5, "ana.gomez@gym.com"),
                        new Entrenador("Roberto Fernández", "Crossfit y Acondicionamiento", 10, "roberto.fernandez@gym.com"),
                        new Entrenador("Lucía Valenzuela", "Yoga y Pilates", 6, "lucia.valenzuela@gym.com")
                ));
            }
        };
    }
}
