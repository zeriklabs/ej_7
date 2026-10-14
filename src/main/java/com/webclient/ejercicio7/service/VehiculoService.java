package com.webclient.ejercicio7.service;

import com.webclient.ejercicio7.model.Vehiculo;

import java.util.List;

public interface VehiculoService {
    List<Vehiculo> listarTodos();
    Vehiculo obtenerPorId(Long id);
    Vehiculo guardar(Vehiculo vehiculo);
    Vehiculo actualizar(Long id, Vehiculo vehiculo);
    void eliminar(Long id);
}
