package com.webclient.ejercicio7.config;

import com.webclient.ejercicio7.model.Entrenador;
import com.webclient.ejercicio7.model.Vehiculo;
import com.webclient.ejercicio7.repository.EntrenadorRepository;
import com.webclient.ejercicio7.repository.VehiculoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(EntrenadorRepository entrenadorRepository, VehiculoRepository vehiculoRepository) {
        return args -> {
            if (entrenadorRepository.count() == 0) {
                entrenadorRepository.saveAll(List.of(
                        new Entrenador("Carlos Morales", "Fútbol", 8, "carlos.morales@gym.com"),
                        new Entrenador("Ana Gómez", "Natación", 5, "ana.gomez@gym.com"),
                        new Entrenador("Roberto Fernández", "Crossfit y Acondicionamiento", 10, "roberto.fernandez@gym.com"),
                        new Entrenador("Lucía Valenzuela", "Yoga y Pilates", 6, "lucia.valenzuela@gym.com")
                ));
            }

            if (vehiculoRepository.count() == 0) {
                vehiculoRepository.saveAll(List.of(
                        new Vehiculo("Toyota", "Corolla", 2022, "Blanco", new BigDecimal("24500.00")),
                        new Vehiculo("Mazda", "CX-5", 2023, "Rojo", new BigDecimal("32800.50")),
                        new Vehiculo("Honda", "Civic", 2021, "Gris", new BigDecimal("26900.00")),
                        new Vehiculo("Ford", "Mustang", 2020, "Azul", new BigDecimal("45000.00")),
                        new Vehiculo("Nissan", "Sentra", 2024, "Negro", new BigDecimal("22400.00"))
                ));
            }
        };
    }
}
