# API RESTful de Entrenadores - Nivel 3 de Madurez de Richardson

API RESTful desarrollada en **Spring Boot 3** con **Arquitectura por Capas**, implementando el **Nivel 3 del Modelo de Madurez de Richardson (HATEOAS)**, respuestas estandarizadas mediante `ResponseEntity`, validaciones de datos y documentación con **OpenAPI / Swagger**.

---

## 🏛️ 1. Arquitectura por Capas

El proyecto está estructurado siguiendo el patrón de arquitectura por capas para garantizar bajo acoplamiento y alta cohesión:

```
src/main/java/com/webclient/ejercicio7/
├── assembler/
│   └── EntrenadorModelAssembler.java    # Capa HATEOAS: Ensamblado de hipermedios (_links)
├── config/
│   ├── DataLoader.java                  # Carga de datos iniciales para pruebas
│   └── OpenApiConfig.java               # Configuración OpenAPI 3.0 / Swagger UI
├── controller/
│   └── EntrenadorController.java        # Capa Presentación / Controladores REST
├── exception/
│   ├── BadRequestException.java         # Excepciones de solicitud inválida (400)
│   ├── ErrorResponse.java               # Modelo estándar de respuesta de error
│   ├── GlobalExceptionHandler.java      # Manejador global con @RestControllerAdvice
│   └── ResourceNotFoundException.java   # Excepciones de recurso no encontrado (404)
├── model/
│   └── Entrenador.java                  # Capa Dominio / Entidad JPA (tabla 'entrenador')
├── repository/
│   └── EntrenadorRepository.java        # Capa Acceso a Datos / Spring Data JPA
├── service/
│   ├── EntrenadorService.java           # Interfaz de lógica de negocio
│   └── EntrenadorServiceImpl.java       # Implementación de lógica de negocio
└── Ejercicio7Application.java           # Clase principal de arranque
```

---

## 📈 2. Niveles de Madurez de Richardson Implementados

| Nivel | Característica | Implementación en este proyecto |
| :---: | :--- | :--- |
| **0** | **El pantano de POX** | No se utiliza un único endpoint tipo RPC. |
| **1** | **Recursos individuales** | URI bien definidas orientadas a recursos: `/api/entrenadores` y `/api/entrenadores/{id}`. |
| **2** | **Verbos y códigos HTTP adecuados** | `GET` (200), `POST` (201 con cabecera `Location`), `PUT` (200), `DELETE` (204), y códigos de error (400, 404). Respuestas estructuradas con `ResponseEntity`. |
| **3** | **Controles de Hipermedios (HATEOAS)** | El recurso devuelto contiene hipervínculos dinámicos a las acciones disponibles: `self`, `update`, `delete` y `entrenadores`. |

---

## 🗄️ 3. Estructura de la Tabla `entrenador`

| Campo | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | Primary Key, Auto Increment | Identificador único del entrenador |
| `nombre` | `VARCHAR(100)` | NOT NULL | Nombre completo del entrenador |
| `especialidad` | `VARCHAR(100)` | NOT NULL | Disciplina o especialidad (ej. Fútbol, Natación, Crossfit) |
| `experiencia` | `INT` | NOT NULL, >= 0 | Años de experiencia en el área |
| `correo` | `VARCHAR(120)` | NOT NULL, UNIQUE, Formato Email | Correo electrónico de contacto |

---

## 🚀 4. Endpoints de la API

| Método | Endpoint | Descripción | Códigos HTTP |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/entrenadores` | Obtener todos los entrenadores con enlaces HATEOAS | `200 OK` |
| `GET` | `/api/entrenadores/{id}` | Obtener un entrenador específico por ID con hipermedios | `200 OK`, `404 NOT FOUND` |
| `POST` | `/api/entrenadores` | Registrar un nuevo entrenador | `201 CREATED`, `400 BAD REQUEST` |
| `PUT` | `/api/entrenadores/{id}` | Actualizar un entrenador existente | `200 OK`, `400 BAD REQUEST`, `404 NOT FOUND` |
| `DELETE` | `/api/entrenadores/{id}` | Eliminar permanentemente un entrenador | `204 NO CONTENT`, `404 NOT FOUND` |

---

## 📬 5. Ejemplos de Peticiones y Respuestas HATEOAS (Nivel 3)

### 🔹 GET `/api/entrenadores/1`
**Respuesta (200 OK):**
```json
{
  "id": 1,
  "nombre": "Carlos Morales",
  "especialidad": "Fútbol",
  "experiencia": 8,
  "correo": "carlos.morales@gym.com",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/entrenadores/1"
    },
    "update": {
      "href": "http://localhost:8080/api/entrenadores/1"
    },
    "delete": {
      "href": "http://localhost:8080/api/entrenadores/1"
    },
    "entrenadores": {
      "href": "http://localhost:8080/api/entrenadores"
    }
  }
}
```

### 🔹 POST `/api/entrenadores`
**Cuerpo de la Petición:**
```json
{
  "nombre": "Laura Mendoza",
  "especialidad": "Spinning",
  "experiencia": 4,
  "correo": "laura.mendoza@gym.com"
}
```
**Respuesta (201 Created):**
- **Header Location**: `http://localhost:8080/api/entrenadores/5`
- **Body**: Recurso creado con sus respectivos enlaces `_links` (`self`, `update`, `delete`, `entrenadores`).

### 🔹 DELETE `/api/entrenadores/1`
**Respuesta:** `204 No Content` (sin cuerpo).

---

## 📖 6. Documentación Interactiva (Swagger / OpenAPI)

Una vez iniciada la aplicación, la documentación interactiva está disponible en:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## ⚙️ 7. Ejecución y Pruebas

### Ejecutar la Aplicación:
```bash
./mvnw spring-boot:run
```

### Ejecutar las Pruebas Automatizadas:
```bash
./mvnw test
```

### Base de Datos H2 Web Console:
- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:entrenadoresdb`
- Usuario: `sa`
- Contraseña: *(vacía)*
