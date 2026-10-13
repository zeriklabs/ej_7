package com.webclient.ejercicio7.service;

import com.webclient.ejercicio7.model.Entrenador;

import java.util.List;

public interface EntrenadorService {

    List<Entrenador> listarTodos();

    Entrenador obtenerPorId(Long id);

    Entrenador guardar(Entrenador entrenador);

    Entrenador actualizar(Long id, Entrenador entrenador);

    void eliminar(Long id);
}
