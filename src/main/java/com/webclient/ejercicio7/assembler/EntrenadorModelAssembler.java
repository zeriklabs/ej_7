package com.webclient.ejercicio7.assembler;

import com.webclient.ejercicio7.controller.EntrenadorController;
import com.webclient.ejercicio7.model.Entrenador;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EntrenadorModelAssembler implements RepresentationModelAssembler<Entrenador, EntityModel<Entrenador>> {

    @Override
    public EntityModel<Entrenador> toModel(Entrenador entrenador) {
        return EntityModel.of(
                entrenador,
                linkTo(methodOn(EntrenadorController.class).obtenerPorId(entrenador.getId())).withSelfRel(),
                linkTo(methodOn(EntrenadorController.class).actualizar(entrenador.getId(), null)).withRel("update"),
                linkTo(methodOn(EntrenadorController.class).eliminar(entrenador.getId())).withRel("delete"),
                linkTo(methodOn(EntrenadorController.class).listarTodos()).withRel("entrenadores")
        );
    }
}
