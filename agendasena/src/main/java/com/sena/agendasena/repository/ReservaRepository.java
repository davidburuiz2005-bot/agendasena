package com.sena.agendasena.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sena.agendasena.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Regla 1: Valida si se cruzan los horarios en el mismo ambiente
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.ambiente.id = :ambienteId " +
           "AND r.estado = 'ACTIVA' " +
           "AND :inicio < r.fechaHoraFin AND :fin > r.fechaHoraInicio")
    boolean existeSolapamiento(@Param("ambienteId") Long ambienteId, 
                               @Param("inicio") LocalDateTime inicio, 
                               @Param("fin") LocalDateTime fin);

    // Regla 5: Cuenta reservas activas de un instructor en un rango de fecha (mismo día)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.nombreInstructor = :instructor " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaHoraInicio >= :inicioDelDia AND r.fechaHoraInicio <= :finDelDia")
    long contarReservasInstructorPorDia(@Param("instructor") String instructor,
                                        @Param("inicioDelDia") LocalDateTime inicioDelDia,
                                        @Param("finDelDia") LocalDateTime finDelDia);

    // Consulta para listar reservas por fecha exacta de inicio (Endpoint 5)
    @Query("SELECT r FROM Reserva r WHERE r.ambiente.id = :ambienteId " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaHoraInicio >= :inicio AND r.fechaHoraInicio <= :fin")
    List<Reserva> buscarReservasPorAmbienteYFecha(@Param("ambienteId") Long ambienteId, 
                                                  @Param("inicio") LocalDateTime inicio, 
                                                  @Param("fin") LocalDateTime fin);

    // Consulta de reservas activas totales para un día (Para reportes)
    @Query("SELECT r FROM Reserva r WHERE r.estado = 'ACTIVA' " +
           "AND r.fechaHoraInicio >= :inicio AND r.fechaHoraInicio <= :fin")
    List<Reserva> buscarReservasActivasDelDia(@Param("inicio") LocalDateTime inicio, 
                                              @Param("fin") LocalDateTime fin);
}