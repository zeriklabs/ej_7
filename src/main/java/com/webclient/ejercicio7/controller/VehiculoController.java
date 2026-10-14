package com.webclient.ejercicio7.controller;

import com.webclient.ejercicio7.assembler.VehiculoModelAssembler;
import com.webclient.ejercicio7.exception.ErrorResponse;
import com.webclient.ejercicio7.model.Vehiculo;
import com.webclient.ejercicio7.service.VehiculoService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
@Tag(name = "Vehículos", description = "Operaciones CRUD para la gestión de vehículos con documentación OpenAPI 3 y nivel 3 de HATEOAS")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final VehiculoModelAssembler assembler;

    public VehiculoController(VehiculoService vehiculoService, VehiculoModelAssembler assembler) {
        this.vehiculoService = vehiculoService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Listar todos los vehículos",
            description = "Recupera la lista de todos los vehículos almacenados, incorporando hipervínculos HATEOAS de navegación y control."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de vehículos recuperada exitosamente",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Vehiculo>>> listarTodos() {
        List<EntityModel<Vehiculo>> vehiculosModel = vehiculoService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Vehiculo>> collectionModel = CollectionModel.of(
                vehiculosModel,
                linkTo(methodOn(VehiculoController.class).listarTodos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Consultar un vehículo por su ID",
            description = "Obtiene los detalles del vehículo mediante su identificador numérico único. Valida que el parámetro sea mayor a 0 y de tipo numérico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehículo localizado y retornado con enlaces HATEOAS",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehiculo.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido: el ID ingresado no es numérico o es menor/igual a cero",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehículo no encontrado para el identificador suministrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<Vehiculo>> obtenerPorId(
            @Parameter(
                    description = "Identificador único del vehículo (número entero positivo > 0)",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(vehiculo));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Crear y registrar un nuevo vehículo",
            description = "Registra un nuevo vehículo en la base de datos. El identificador 'id' es omitido en la creación pues es autogenerado. Se validan todos los atributos obligatorios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vehículo registrado correctamente con cabecera Location y enlaces HATEOAS",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehiculo.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido o datos de entrada no válidos (por ejemplo: precio negativo, año fuera de rango, campos vacíos)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<Vehiculo>> guardar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los atributos del vehículo a crear (sin incluir ID)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Vehiculo.class)
                    )
            )
            @Valid @RequestBody Vehiculo vehiculo) {

        Vehiculo nuevoVehiculo = vehiculoService.guardar(vehiculo);
        EntityModel<Vehiculo> model = assembler.toModel(nuevoVehiculo);

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Actualizar los datos de un vehículo existente",
            description = "Actualiza las características de un vehículo a través de su ID. Valida que el ID del parámetro y todos los atributos sean conformes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehículo actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehiculo.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido: ID incorrecto o datos del cuerpo no válidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehículo no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<Vehiculo>> actualizar(
            @Parameter(
                    description = "Identificador único del vehículo a modificar (entero > 0)",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Atributos actualizados del vehículo",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Vehiculo.class)
                    )
            )
            @Valid @RequestBody Vehiculo vehiculo) {

        Vehiculo actualizado = vehiculoService.actualizar(id, vehiculo);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un vehículo por ID",
            description = "Elimina permanentemente el registro del vehículo identificado por el parámetro ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Vehículo eliminado con éxito (sin contenido en la respuesta)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido: el ID ingresado es menor o igual a cero o no numérico",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehículo no encontrado para el identificador suministrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(
                    description = "Identificador del vehículo a eliminar",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
