package com.sena.agendasena.dto;

import lombok.Data;

@Data
public class OcupacionReporteDTO {
    private Long ambienteId;
    private String nombreAmbiente;
    private double horasReservadas;
    private String porcentajeOcupacion;

    public OcupacionReporteDTO(Long ambienteId, String nombreAmbiente, double horasReservadas, double porcentaje) {
        this.ambienteId = ambienteId;
        this.nombreAmbiente = nombreAmbiente;
        this.horasReservadas = horasReservadas;
        this.porcentajeOcupacion = String.format("%.2f%%", porcentaje);
    }
}