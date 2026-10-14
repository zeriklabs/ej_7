package com.webclient.ejercicio7.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehiculo")
@Schema(description = "Entidad representativa de un Vehículo en el sistema")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            description = "Identificador único autoincremental del vehículo",
            example = "1"
    )
    private Long id;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    @Schema(description = "Marca del fabricante del vehículo", example = "Toyota")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    @Schema(description = "Modelo comercial del vehículo", example = "Corolla")
    private String modelo;

    @NotNull(message = "El año de fabricación es obligatorio")
    @Min(value = 1886, message = "El año no puede ser anterior a 1886")
    @Max(value = 2100, message = "El año no puede ser superior a 2100")
    @Column(name = "anio", nullable = false)
    @JsonProperty("anio")
    @JsonAlias({"año", "anio"})
    @Schema(description = "Año de fabricación del vehículo", example = "2023")
    private Integer anio;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 30, message = "El color no puede tener más de 30 caracteres")
    @Column(nullable = false, length = 30)
    @Schema(description = "Color exterior del vehículo", example = "Rojo")
    private String color;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 enteros y 2 decimales")
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Precio de venta comercial en moneda local", example = "25000.50")
    private BigDecimal precio;

    public Vehiculo() {
    }

    public Vehiculo(Long id, String marca, String modelo, Integer anio, String color, BigDecimal precio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.precio = precio;
    }

    public Vehiculo(String marca, String modelo, Integer anio, String color, BigDecimal precio) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio=" + anio +
                ", color='" + color + '\'' +
                ", precio=" + precio +
                '}';
    }
}
