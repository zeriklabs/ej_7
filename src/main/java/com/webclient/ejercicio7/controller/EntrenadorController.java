package com.webclient.ejercicio7.controller;

import com.webclient.ejercicio7.assembler.EntrenadorModelAssembler;
import com.webclient.ejercicio7.model.Entrenador;
import com.webclient.ejercicio7.service.EntrenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/entrenadores")
@CrossOrigin(origins = "*")
@Tag(name = "Entrenadores", description = "API RESTful para la gestión de entrenadores con Nivel 3 de Madurez de Richardson (HATEOAS)")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;
    private final EntrenadorModelAssembler assembler;

    public EntrenadorController(EntrenadorService entrenadorService, EntrenadorModelAssembler assembler) {
        this.entrenadorService = entrenadorService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los entrenadores",
            description = "Obtiene la lista completa de entrenadores con enlaces HATEOAS individuales y enlace self a la colección."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de entrenadores obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Entrenador>>> listarTodos() {
        List<EntityModel<Entrenador>> entrenadoresModel = entrenadorService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Entrenador>> collectionModel = CollectionModel.of(
                entrenadoresModel,
                linkTo(methodOn(EntrenadorController.class).listarTodos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un entrenador por ID",
            description = "Recupera un entrenador específico por su identificador único. Incluye enlaces HATEOAS: self, update, delete y colección."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado con el ID proporcionado", content = @Content)
    })
    public ResponseEntity<EntityModel<Entrenador>> obtenerPorId(
            @Parameter(description = "ID del entrenador a consultar", example = "1")
            @PathVariable Long id) {

        Entrenador entrenador = entrenadorService.obtenerPorId(id);
        EntityModel<Entrenador> model = assembler.toModel(entrenador);

        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo entrenador",
            description = "Registra un nuevo entrenador en la base de datos y retorna el recurso creado con código HTTP 201 Created y cabecera Location."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrenador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo electrónico duplicado", content = @Content)
    })
    public ResponseEntity<EntityModel<Entrenador>> guardar(
            @Valid @RequestBody Entrenador entrenador) {

        Entrenador nuevoEntrenador = entrenadorService.guardar(entrenador);
        EntityModel<Entrenador> model = assembler.toModel(nuevoEntrenador);

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un entrenador existente",
            description = "Actualiza todos los campos de un entrenador especificado por su ID y devuelve el recurso actualizado con sus enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo electrónico ya utilizado por otro entrenador", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado con el ID especificado", content = @Content)
    })
    public ResponseEntity<EntityModel<Entrenador>> actualizar(
            @Parameter(description = "ID del entrenador a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Entrenador entrenador) {

        Entrenador entrenadorActualizado = entrenadorService.actualizar(id, entrenador);
        EntityModel<Entrenador> model = assembler.toModel(entrenadorActualizado);

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un entrenador por ID",
            description = "Elimina de forma permanente el entrenador correspondiente al ID especificado y responde con código HTTP 204 No Content."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrenador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado con el ID especificado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del entrenador a eliminar", example = "1")
            @PathVariable Long id) {

        entrenadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
