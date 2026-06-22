package com.sena.agendasena.dto;

import com.sena.agendasena.model.TipoAmbiente;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmbienteRequestDTO {

    @NotBlank(message = "El nombre del ambiente no puede estar vacío.")
    private String nombre;

    @NotNull(message = "El tipo de ambiente es obligatorio (SALA, LABORATORIO o AUDITORIO).")
    private TipoAmbiente tipo;

    @NotNull(message = "La capacidad es obligatoria.")
    @Min(value = 1, message = "La capacidad debe ser un número positivo mayor a 0.")
    private Integer capacidad;

    @NotNull(message = "El campo 'activo' es obligatorio.")
    private Boolean activo;
}