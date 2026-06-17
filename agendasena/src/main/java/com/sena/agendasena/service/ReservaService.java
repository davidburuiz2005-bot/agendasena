package com.sena.agendasena.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.agendasena.dto.OcupacionReporteDTO;
import com.sena.agendasena.dto.ReservaRequestDTO;
import com.sena.agendasena.exception.BusinessException;
import com.sena.agendasena.model.Ambiente;
import com.sena.agendasena.model.EstadoReserva;
import com.sena.agendasena.model.Reserva;
import com.sena.agendasena.repository.AmbienteRepository;
import com.sena.agendasena.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AmbienteRepository ambienteRepository;

    public ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository) {
        this.reservaRepository = reservaRepository;
        this.ambienteRepository = ambienteRepository;
    }

    public Reserva crearReserva(ReservaRequestDTO dto) {
        Ambiente ambiente = ambienteRepository.findById(dto.getAmbienteId())
                .orElseThrow(() -> new BusinessException("Ambiente no encontrado.", HttpStatus.BAD_REQUEST));

        LocalDateTime inicio = dto.getFechaHoraInicio();
        LocalDateTime fin = dto.getFechaHoraFin();

        // Regla 7: No se reserva en el pasado
        if (inicio.isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se pueden realizar reservas en el pasado.", HttpStatus.BAD_REQUEST);
        }

        // Regla 4: Ambientes inactivos
        if (!ambiente.getActivo()) {
            throw new BusinessException("No se puede reservar un ambiente inactivo.", HttpStatus.BAD_REQUEST);
        }

        // Regla 2: Capacidad
        if (dto.getNumeroAprendices() > ambiente.getCapacidad()) {
            throw new BusinessException("La cantidad de aprendices supera la capacidad del ambiente.", HttpStatus.BAD_REQUEST);
        }

        // Regla 3: Horario institucional (06:00 a 22:00) y duración (1 a 4 horas)
        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();
        if (horaInicio.isBefore(LocalTime.of(6, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
            throw new BusinessException("Las reservas deben estar dentro del horario institucional (06:00 a 22:00).", HttpStatus.BAD_REQUEST);
        }

        long horasDuracion = Duration.between(inicio, fin).toHours();
        if (horasDuracion < 1 || horasDuracion > 4) {
            throw new BusinessException("La reserva debe tener una duración de entre 1 y 4 horas.", HttpStatus.BAD_REQUEST);
        }

        // Regla 5: Límite por instructor (Máximo 3 activas por día)
        LocalDateTime inicioDia = inicio.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicio.toLocalDate().atTime(23, 59, 59);
        long reservasDelDia = reservaRepository.contarReservasInstructorPorDia(dto.getNombreInstructor(), inicioDia, finDia);
        if (reservasDelDia >= 3) {
            throw new BusinessException("El instructor ya cuenta con el límite de 3 reservas ACTIVAS para este día.", HttpStatus.BAD_REQUEST);
        }

        // Regla 1: Sin cruces de horario (Solapamientos)
        if (reservaRepository.existeSolapamiento(ambiente.getId(), inicio, fin)) {
            throw new BusinessException("El ambiente ya se encuentra reservado en el rango de tiempo seleccionado.", HttpStatus.CONFLICT);
        }

        // Mapeo e inserción
        Reserva reserva = new Reserva();
        reserva.setAmbiente(ambiente);
        reserva.setNombreInstructor(dto.getNombreInstructor());
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(fin);
        reserva.setNumeroAprendices(dto.getNumeroAprendices());
        reserva.setEstado(EstadoReserva.ACTIVA);

        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Reserva no encontrada.", HttpStatus.BAD_REQUEST));

        // Regla 6: Cancelación con anticipación mínima de 2 horas
        if (LocalDateTime.now().isAfter(reserva.getFechaHoraInicio().minusHours(2))) {
            throw new BusinessException("Solo se permiten cancelaciones con un mínimo de 2 horas de anticipación.", HttpStatus.BAD_REQUEST);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorAmbienteYFecha(Long ambienteId, LocalDate fecha) {
        return reservaRepository.buscarReservasPorAmbienteYFecha(ambienteId, fecha.atStartOfDay(), fecha.atTime(23, 59, 59));
    }

    public List<Ambiente> listarDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        List<Ambiente> todos = ambienteRepository.findAll();
        List<Ambiente> disponibles = new ArrayList<>();
        for (Ambiente a : todos) {
            if (a.getActivo() && !reservaRepository.existeSolapamiento(a.getId(), inicio, fin)) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public List<OcupacionReporteDTO> generarReporteOcupacion(LocalDate fecha) {
        List<Ambiente> ambientes = ambienteRepository.findAll();
        List<Reserva> reservasDelDia = reservaRepository.buscarReservasActivasDelDia(fecha.atStartOfDay(), fecha.atTime(23, 59, 59));
        List<OcupacionReporteDTO> reporte = new ArrayList<>();

        for (Ambiente amb : ambientes) {
            double horasOcupadas = 0;
            for (Reserva res : reservasDelDia) {
                if (res.getAmbiente().getId().equals(amb.getId())) {
                    horasOcupadas += Duration.between(res.getFechaHoraInicio(), res.getFechaHoraFin()).toHours();
                }
            }
            double porcentaje = (horasOcupadas / 16.0) * 100.0; // 16 horas institucionales
            reporte.add(new OcupacionReporteDTO(amb.getId(), amb.getNombre(), horasOcupadas, porcentaje));
        }
        return reporte;
    }
}