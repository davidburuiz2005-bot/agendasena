package com.sena.agendasena.controller;

import com.sena.agendasena.dto.OcupacionReporteDTO;
import com.sena.agendasena.service.ReservaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReservaService reservaService;

    public ReporteController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/ocupacion")
    public ResponseEntity<List<OcupacionReporteDTO>> obtenerReporteOcupacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.generarReporteOcupacion(fecha));
    }
    
}