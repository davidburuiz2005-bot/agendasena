package com.sena.agendasena.controller;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
 
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.sena.agendasena.dto.AmbienteRequestDTO;
import com.sena.agendasena.model.Ambiente;
import com.sena.agendasena.model.Reserva;
import com.sena.agendasena.service.AmbienteService;
import com.sena.agendasena.service.ReservaService;
 
import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {
 
    private final AmbienteService ambienteService;
    private final ReservaService reservaService;
 
    public AmbienteController(AmbienteService ambienteService, ReservaService reservaService) {
        this.ambienteService = ambienteService;
        this.reservaService = reservaService;
    }
 
    @PostMapping
    public ResponseEntity<Ambiente> registrar(@Valid @RequestBody AmbienteRequestDTO dto) {
        Ambiente ambiente = new Ambiente();
        ambiente.setNombre(dto.getNombre());
        ambiente.setTipo(dto.getTipo());
        ambiente.setCapacidad(dto.getCapacidad());
        ambiente.setActivo(dto.getActivo());
        return ResponseEntity.ok(ambienteService.registrar(ambiente));
    }
 
    @GetMapping
    public ResponseEntity<List<Ambiente>> listar() {
        return ResponseEntity.ok(ambienteService.listarTodos());
    }
 
    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<Reserva>> verReservasPorFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.listarPorAmbienteYFecha(id, fecha));
    }
 
    @GetMapping("/disponibles")
    public ResponseEntity<List<Ambiente>> verDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(reservaService.listarDisponibles(inicio, fin));
    }
}