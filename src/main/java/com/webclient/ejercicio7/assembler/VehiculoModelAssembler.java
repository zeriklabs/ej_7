package com.webclient.ejercicio7.assembler;

import com.webclient.ejercicio7.controller.VehiculoController;
import com.webclient.ejercicio7.model.Vehiculo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VehiculoModelAssembler implements RepresentationModelAssembler<Vehiculo, EntityModel<Vehiculo>> {

    @Override
    public EntityModel<Vehiculo> toModel(Vehiculo vehiculo) {
        return EntityModel.of(
                vehiculo,
                linkTo(methodOn(VehiculoController.class).obtenerPorId(vehiculo.getId())).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).actualizar(vehiculo.getId(), null)).withRel("update"),
                linkTo(methodOn(VehiculoController.class).eliminar(vehiculo.getId())).withRel("delete"),
                linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("vehiculos")
        );
    }
}
