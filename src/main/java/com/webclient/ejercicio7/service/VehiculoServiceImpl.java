package com.webclient.ejercicio7.service;

import com.webclient.ejercicio7.exception.BadRequestException;
import com.webclient.ejercicio7.exception.ResourceNotFoundException;
import com.webclient.ejercicio7.model.Vehiculo;
import com.webclient.ejercicio7.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo obtenerPorId(Long id) {
        validarId(id);
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con el ID: " + id));
    }

    @Override
    @Transactional
    public Vehiculo guardar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new BadRequestException("Parámetro inválido: el objeto vehículo no puede ser nulo");
        }
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional
    public Vehiculo actualizar(Long id, Vehiculo vehiculo) {
        validarId(id);
        if (vehiculo == null) {
            throw new BadRequestException("Parámetro inválido: el objeto vehículo para actualizar no puede ser nulo");
        }

        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con el ID: " + id));

        existente.setMarca(vehiculo.getMarca());
        existente.setModelo(vehiculo.getModelo());
        existente.setAnio(vehiculo.getAnio());
        existente.setColor(vehiculo.getColor());
        existente.setPrecio(vehiculo.getPrecio());

        return vehiculoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        validarId(id);
        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con el ID: " + id));
        vehiculoRepository.delete(existente);
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Parámetro inválido: el 'id' (" + id + ") debe ser un número entero positivo mayor a 0");
        }
    }
}
