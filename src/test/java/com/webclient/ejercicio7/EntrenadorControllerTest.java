package com.webclient.ejercicio7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient.ejercicio7.model.Entrenador;
import com.webclient.ejercicio7.repository.EntrenadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EntrenadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    private Entrenador entrenadorTest;

    @BeforeEach
    void setUp() {
        entrenadorRepository.deleteAll();
        entrenadorTest = entrenadorRepository.save(
                new Entrenador("Carlos Morales", "Fútbol", 8, "carlos.morales@gym.com")
        );
    }

    @Test
    @DisplayName("Nivel 2 y 3: GET /api/entrenadores debe retornar 200 OK y enlaces HATEOAS")
    void testListarTodos() throws Exception {
        mockMvc.perform(get("/api/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.entrenadorList", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/entrenadores")))
                .andExpect(jsonPath("$._embedded.entrenadorList[0]._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._embedded.entrenadorList[0]._links.update.href", notNullValue()))
                .andExpect(jsonPath("$._embedded.entrenadorList[0]._links.delete.href", notNullValue()));
    }

    @Test
    @DisplayName("Nivel 3: GET /api/entrenadores/{id} debe retornar 200 OK y enlaces self, update, delete")
    void testObtenerPorId() throws Exception {
        mockMvc.perform(get("/api/entrenadores/" + entrenadorTest.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(entrenadorTest.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Carlos Morales")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/entrenadores/" + entrenadorTest.getId())))
                .andExpect(jsonPath("$._links.update.href", containsString("/api/entrenadores/" + entrenadorTest.getId())))
                .andExpect(jsonPath("$._links.delete.href", containsString("/api/entrenadores/" + entrenadorTest.getId())))
                .andExpect(jsonPath("$._links.entrenadores.href", containsString("/api/entrenadores")));
    }

    @Test
    @DisplayName("Nivel 2: GET /api/entrenadores/999 no existente debe responder 404 Not Found")
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/entrenadores/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensaje", containsString("Entrenador no encontrado")));
    }

    @Test
    @DisplayName("Nivel 2 y 3: POST /api/entrenadores debe responder 201 Created con cabecera Location y enlaces")
    void testGuardarEntrenador() throws Exception {
        Entrenador nuevo = new Entrenador("Laura Mendoza", "Spinning", 4, "laura.mendoza@gym.com");

        mockMvc.perform(post("/api/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Laura Mendoza")))
                .andExpect(jsonPath("$._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._links.update.href", notNullValue()))
                .andExpect(jsonPath("$._links.delete.href", notNullValue()));
    }

    @Test
    @DisplayName("Validación: POST con campos vacíos debe responder 400 Bad Request")
    void testGuardarEntrenadorInvalido() throws Exception {
        Entrenador invalido = new Entrenador("", "", -1, "correo-invalido");

        mockMvc.perform(post("/api/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validaciones.nombre", notNullValue()))
                .andExpect(jsonPath("$.validaciones.especialidad", notNullValue()))
                .andExpect(jsonPath("$.validaciones.experiencia", notNullValue()))
                .andExpect(jsonPath("$.validaciones.correo", notNullValue()));
    }

    @Test
    @DisplayName("Nivel 2: PUT /api/entrenadores/{id} debe responder 200 OK y datos modificados")
    void testActualizarEntrenador() throws Exception {
        Entrenador actualizado = new Entrenador("Carlos Morales Editado", "Fútbol Avanzado", 9, "carlos.morales@gym.com");

        mockMvc.perform(put("/api/entrenadores/" + entrenadorTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Morales Editado")))
                .andExpect(jsonPath("$.especialidad", is("Fútbol Avanzado")))
                .andExpect(jsonPath("$.experiencia", is(9)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/entrenadores/" + entrenadorTest.getId())));
    }

    @Test
    @DisplayName("Nivel 2: DELETE /api/entrenadores/{id} debe responder 204 No Content")
    void testEliminarEntrenador() throws Exception {
        mockMvc.perform(delete("/api/entrenadores/" + entrenadorTest.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/entrenadores/" + entrenadorTest.getId()))
                .andExpect(status().isNotFound());
    }
}
