package com.sena.agendasena.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaRequestDTO {
    @NotNull(message = "El id del ambiente es obligatorio.")
    private Long ambienteId;

    @NotBlank(message = "El nombre del instructor no puede estar vacío.")
    private String nombreInstructor;

    @NotNull(message = "La fecha y hora de inicio es obligatoria.")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria.")
    private LocalDateTime fechaHoraFin;

    @NotNull(message = "El número de aprendices es obligatorio.")
    @Min(value = 1, message = "El número de aprendices debe ser al menos 1.")
    private Integer numeroAprendices;
}