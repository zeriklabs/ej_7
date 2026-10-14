package com.webclient.ejercicio7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient.ejercicio7.model.Vehiculo;
import com.webclient.ejercicio7.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    private Vehiculo vehiculoTest;

    @BeforeEach
    void setUp() {
        vehiculoRepository.deleteAll();
        vehiculoTest = vehiculoRepository.save(
                new Vehiculo("Toyota", "Corolla", 2022, "Blanco", new BigDecimal("24500.00"))
        );
    }

    @Test
    @DisplayName("GET /api/vehiculos - Debe retornar 200 OK y colección con enlaces HATEOAS")
    void testListarTodos() throws Exception {
        mockMvc.perform(get("/api/vehiculos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.vehiculoList", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/vehiculos")))
                .andExpect(jsonPath("$._embedded.vehiculoList[0]._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._embedded.vehiculoList[0]._links.update.href", notNullValue()))
                .andExpect(jsonPath("$._embedded.vehiculoList[0]._links.delete.href", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/vehiculos/{id} - Debe retornar 200 OK y enlaces HATEOAS individuales")
    void testObtenerPorId() throws Exception {
        mockMvc.perform(get("/api/vehiculos/" + vehiculoTest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vehiculoTest.getId().intValue())))
                .andExpect(jsonPath("$.marca", is("Toyota")))
                .andExpect(jsonPath("$.modelo", is("Corolla")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/vehiculos/" + vehiculoTest.getId())))
                .andExpect(jsonPath("$._links.update.href", containsString("/api/vehiculos/" + vehiculoTest.getId())))
                .andExpect(jsonPath("$._links.delete.href", containsString("/api/vehiculos/" + vehiculoTest.getId())))
                .andExpect(jsonPath("$._links.vehiculos.href", containsString("/api/vehiculos")));
    }

    @Test
    @DisplayName("PARÁMETRO INVÁLIDO: GET /api/vehiculos/0 - Debe retornar 400 Bad Request por ID <= 0")
    void testObtenerPorIdInvalidoMenorOIgualACero() throws Exception {
        mockMvc.perform(get("/api/vehiculos/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Parámetro Inválido")))
                .andExpect(jsonPath("$.mensaje", containsString("debe ser un número entero positivo mayor a 0")));
    }

    @Test
    @DisplayName("PARÁMETRO INVÁLIDO: GET /api/vehiculos/invalido - Debe retornar 400 Bad Request por parámetro no numérico")
    void testObtenerPorIdInvalidoTipoDato() throws Exception {
        mockMvc.perform(get("/api/vehiculos/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Parámetro Inválido")))
                .andExpect(jsonPath("$.mensaje", containsString("no es válido para el parámetro 'id'")));
    }

    @Test
    @DisplayName("GET /api/vehiculos/99999 - Debe retornar 404 Not Found si no existe")
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/vehiculos/99999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensaje", containsString("Vehículo no encontrado")));
    }

    @Test
    @DisplayName("POST /api/vehiculos - Debe retornar 201 Created con cabecera Location")
    void testGuardarVehiculoExitoso() throws Exception {
        Vehiculo nuevo = new Vehiculo("Honda", "Civic", 2023, "Negro", new BigDecimal("27000.00"));

        mockMvc.perform(post("/api/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.marca", is("Honda")))
                .andExpect(jsonPath("$.modelo", is("Civic")))
                .andExpect(jsonPath("$._links.self.href", notNullValue()));
    }

    @Test
    @DisplayName("PARÁMETRO INVÁLIDO: POST /api/vehiculos - Debe retornar 400 Bad Request si los datos/parámetros son inválidos")
    void testGuardarVehiculoParametrosInvalidos() throws Exception {
        // Vehículo con parámetros inválidos: marca vacía, modelo vacío, año anterior a 1886, precio negativo
        Vehiculo vehiculoInvalido = new Vehiculo("", "", 1800, "", new BigDecimal("-50.00"));

        mockMvc.perform(post("/api/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validaciones.marca", notNullValue()))
                .andExpect(jsonPath("$.validaciones.modelo", notNullValue()))
                .andExpect(jsonPath("$.validaciones.anio", notNullValue()))
                .andExpect(jsonPath("$.validaciones.color", notNullValue()))
                .andExpect(jsonPath("$.validaciones.precio", notNullValue()));
    }

    @Test
    @DisplayName("PUT /api/vehiculos/{id} - Debe retornar 200 OK con los datos actualizados")
    void testActualizarVehiculoExitoso() throws Exception {
        Vehiculo modificado = new Vehiculo("Toyota", "Corolla Cross", 2024, "Plata", new BigDecimal("29800.00"));

        mockMvc.perform(put("/api/vehiculos/" + vehiculoTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo", is("Corolla Cross")))
                .andExpect(jsonPath("$.anio", is(2024)))
                .andExpect(jsonPath("$.color", is("Plata")));
    }

    @Test
    @DisplayName("PARÁMETRO INVÁLIDO: PUT /api/vehiculos/{id} - Debe retornar 400 Bad Request si el ID es inválido")
    void testActualizarVehiculoIdInvalido() throws Exception {
        Vehiculo modificado = new Vehiculo("Toyota", "Corolla Cross", 2024, "Plata", new BigDecimal("29800.00"));

        mockMvc.perform(put("/api/vehiculos/-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Parámetro Inválido")));
    }

    @Test
    @DisplayName("DELETE /api/vehiculos/{id} - Debe retornar 204 No Content")
    void testEliminarVehiculo() throws Exception {
        mockMvc.perform(delete("/api/vehiculos/" + vehiculoTest.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vehiculos/" + vehiculoTest.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PARÁMETRO INVÁLIDO: DELETE /api/vehiculos/{id} - Debe retornar 400 Bad Request si el ID es <= 0")
    void testEliminarVehiculoIdInvalido() throws Exception {
        mockMvc.perform(delete("/api/vehiculos/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Parámetro Inválido")));
    }
}
