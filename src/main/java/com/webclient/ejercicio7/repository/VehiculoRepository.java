package com.webclient.ejercicio7.repository;

import com.webclient.ejercicio7.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByMarcaIgnoreCase(String marca);
    List<Vehiculo> findByAnio(Integer anio);
}
