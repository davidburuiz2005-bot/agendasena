package com.sena.agendasena.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sena.agendasena.model.Ambiente;
import com.sena.agendasena.repository.AmbienteRepository;

@Service
public class AmbienteService {
    private final AmbienteRepository ambienteRepository;

    public AmbienteService(AmbienteRepository ambienteRepository) {
        this.ambienteRepository = ambienteRepository;
    }

    public Ambiente registrar(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public List<Ambiente> listarTodos() {
        return ambienteRepository.findAll();
    }
}
