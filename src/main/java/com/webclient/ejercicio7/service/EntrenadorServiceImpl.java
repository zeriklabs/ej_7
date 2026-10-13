package com.webclient.ejercicio7.service;

import com.webclient.ejercicio7.exception.BadRequestException;
import com.webclient.ejercicio7.exception.ResourceNotFoundException;
import com.webclient.ejercicio7.model.Entrenador;
import com.webclient.ejercicio7.repository.EntrenadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntrenadorServiceImpl implements EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    public EntrenadorServiceImpl(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entrenador> listarTodos() {
        return entrenadorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Entrenador obtenerPorId(Long id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con ID: " + id));
    }

    @Override
    public Entrenador guardar(Entrenador entrenador) {
        if (entrenadorRepository.existsByCorreo(entrenador.getCorreo())) {
            throw new BadRequestException("Ya existe un entrenador registrado con el correo: " + entrenador.getCorreo());
        }
        entrenador.setId(null);
        return entrenadorRepository.save(entrenador);
    }

    @Override
    public Entrenador actualizar(Long id, Entrenador entrenadorActualizado) {
        Entrenador entrenadorExistente = obtenerPorId(id);

        if (entrenadorRepository.existsByCorreoAndIdNot(entrenadorActualizado.getCorreo(), id)) {
            throw new BadRequestException("El correo ya está en uso por otro entrenador: " + entrenadorActualizado.getCorreo());
        }

        entrenadorExistente.setNombre(entrenadorActualizado.getNombre());
        entrenadorExistente.setEspecialidad(entrenadorActualizado.getEspecialidad());
        entrenadorExistente.setExperiencia(entrenadorActualizado.getExperiencia());
        entrenadorExistente.setCorreo(entrenadorActualizado.getCorreo());

        return entrenadorRepository.save(entrenadorExistente);
    }

    @Override
    public void eliminar(Long id) {
        Entrenador entrenador = obtenerPorId(id);
        entrenadorRepository.delete(entrenador);
    }
}
