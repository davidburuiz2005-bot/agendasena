package com.sena.agendasena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.agendasena.model.Ambiente;

@Repository
public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {
}
