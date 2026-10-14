# API RESTful de Vehículos y Entrenadores - Nivel 3 de Madurez de Richardson

API RESTful desarrollada en **Spring Boot 3** con **Arquitectura por Capas**, implementando el **Nivel 3 del Modelo de Madurez de Richardson (HATEOAS)**, validaciones estrictas y manejo del caso de **Parámetro Inválido (HTTP 400 Bad Request)**, documentada con **OpenAPI 3 / Swagger**.

---

## 🏛️ 1. Arquitectura por Capas

El proyecto está estructurado con bajo acoplamiento y alta cohesión siguiendo el patrón de arquitectura por capas:

```
src/main/java/com/webclient/ejercicio7/
├── assembler/
│   ├── EntrenadorModelAssembler.java    # Capa HATEOAS: Ensamblado de hipermedios de Entrenador
│   └── VehiculoModelAssembler.java      # Capa HATEOAS: Ensamblado de hipermedios de Vehiculo
├── config/
│   └── DataLoader.java                  # Carga de datos iniciales en BD H2
├── configuration/
│   └── SwaggerConfig.java               # Configuración OpenAPI 3 con @OpenAPIDefinition e @Info
├── controller/
│   ├── EntrenadorController.java        # Controlador REST Entrenadores con Swagger
│   └── VehiculoController.java          # Controlador REST Vehículos (Tabla 6) con Swagger
├── exception/
│   ├── BadRequestException.java         # Excepciones de solicitud/parámetro inválido (400)
│   ├── ErrorResponse.java               # Modelo estándar de respuesta de error JSON
│   ├── GlobalExceptionHandler.java      # Manejador global con @RestControllerAdvice
│   └── ResourceNotFoundException.java   # Excepciones de recurso no encontrado (404)
├── model/
│   ├── Entrenador.java                  # Capa Dominio / Entidad JPA 'entrenador'
│   └── Vehiculo.java                    # Capa Dominio / Entidad JPA 'vehiculo' (Tabla 6)
├── repository/
│   ├── EntrenadorRepository.java        # Spring Data JPA para Entrenador
│   └── VehiculoRepository.java          # Spring Data JPA para Vehiculo
├── service/
│   ├── VehiculoService.java             # Interfaz de lógica de negocio de Vehiculo
│   └── VehiculoServiceImpl.java         # Implementación y validación de parámetros de Vehiculo
└── Ejercicio7Application.java           # Clase principal de arranque
```

---

## 🚘 2. Tabla 6: `Vehiculo` (Especificación del Ejercicio)

| Campo | Tipo | Restricciones / Validación | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | Primary Key, Auto Increment, `@Schema(accessMode = READ_ONLY)` | Identificador del vehículo |
| `marca` | `VARCHAR(50)` | NOT NULL, Max 50 caracteres | Marca del fabricante |
| `modelo` | `VARCHAR(50)` | NOT NULL, Max 50 caracteres | Modelo comercial |
| `año` / `anio` | `INT` | NOT NULL, Min 1886, Max 2100 | Año de fabricación |
| `color` | `VARCHAR(30)` | NOT NULL, Max 30 caracteres | Color exterior |
| `precio` | `DECIMAL(10,2)`| NOT NULL, DecimalMin(0.01) | Precio de venta comercial |

> En el POST el `id` no se solicita al usuario gracias a `@Schema(accessMode = Schema.AccessMode.READ_ONLY)`.

---

## ⚠️ 3. Caso Especial: Parámetro Inválido (HTTP 400 Bad Request)

Se implementaron múltiples mecanismos de validación y control para el caso de **parámetro inválido**:

1. **Parámetro de ruta inválido (`id <= 0`):**
   - Si se consulta `GET /api/vehiculos/0` o con valores negativos como `/api/vehiculos/-5`, el servicio lanza `BadRequestException` y retorna código `400 Bad Request`.
2. **Parámetro con tipo de dato incompatible:**
   - Si se envía un valor no numérico como `GET /api/vehiculos/abc`, `MethodArgumentTypeMismatchException` lo intercepta y devuelve `400 Bad Request` indicando el tipo esperado.
3. **Parámetros del cuerpo JSON inválidos (`@Valid @RequestBody`):**
   - Si se envía un vehículo con campos vacíos, precio menor a cero o año fuera de rango, `MethodArgumentNotValidException` genera una respuesta estructurada con el detalle de cada campo inválido.

**Ejemplo de respuesta de error por parámetro inválido:**
```json
{
  "timestamp": "2026-10-14T13:36:12",
  "status": 400,
  "error": "Parámetro Inválido",
  "mensaje": "Parámetro inválido: el 'id' (0) debe ser un número entero positivo mayor a 0",
  "path": "/api/vehiculos/0",
  "validaciones": null
}
```

---

## 📖 4. Configuración OpenAPI / Swagger UI

Siguiendo la guía de configuración y las anotaciones requeridas:

- **Clase de Configuración**: [SwaggerConfig.java](file:///c:/Users/javie/OneDrive/Documentos/Universidad/Desarrollo_apps_WC/ejercicio7/src/main/java/com/webclient/ejercicio7/configuration/SwaggerConfig.java) en el paquete `configuration` usando:
  - `@OpenAPIDefinition`
  - `@Info` (`title`, `version`, `description`, `termsOfService`, `@Contact`, `@License`)
  - `servers = { @Server(...) }`
- **Anotaciones en Controlador y Modelos**:
  - `@Tag(name = "Vehículos", description = "...")`
  - `@Operation(summary = "...", description = "...")`
  - `@Parameter(description = "...", example = "1", required = true)`
  - `@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "...", required = true, content = @Content(...))`
  - `@ApiResponse(responseCode = "200", ...)`
  - `@ApiResponse(responseCode = "400", description = "Parámetro inválido o datos de entrada no válidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))`
  - `@ApiResponse(responseCode = "404", description = "Vehículo no encontrado", ...)`

### URL de Acceso a Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```
o
```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 5. Endpoints de Vehículos

| Método | Endpoint | Descripción | Códigos HTTP |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/vehiculos` | Listar todos los vehículos con HATEOAS | `200 OK` |
| `GET` | `/api/vehiculos/{id}` | Consultar vehículo por ID | `200 OK`, `400 BAD REQUEST`, `404 NOT FOUND` |
| `POST` | `/api/vehiculos` | Crear nuevo vehículo | `201 CREATED`, `400 BAD REQUEST` |
| `PUT` | `/api/vehiculos/{id}` | Actualizar vehículo existente | `200 OK`, `400 BAD REQUEST`, `404 NOT FOUND` |
| `DELETE` | `/api/vehiculos/{id}` | Eliminar vehículo | `204 NO CONTENT`, `400 BAD REQUEST`, `404 NOT FOUND` |

---

## 🧪 6. Ejecución de Pruebas

Para ejecutar las 19 pruebas automatizadas (incluyendo las pruebas de parámetros inválidos y HATEOAS):

```bash
./mvnw test
```
