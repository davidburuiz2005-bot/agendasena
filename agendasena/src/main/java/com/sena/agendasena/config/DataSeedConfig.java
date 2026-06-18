package com.sena.agendasena.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sena.agendasena.model.Ambiente;
import com.sena.agendasena.model.TipoAmbiente;
import com.sena.agendasena.repository.AmbienteRepository;

@Component
public class DataSeedConfig implements CommandLineRunner {

    private final AmbienteRepository ambienteRepository;

    public DataSeedConfig(AmbienteRepository ambienteRepository) {
        this.ambienteRepository = ambienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ambienteRepository.count() == 0) {
            Ambiente amb1 = new Ambiente();
            amb1.setNombre("Sistemas 101");
            amb1.setTipo(TipoAmbiente.LABORATORIO);
            amb1.setCapacidad(30);

            Ambiente amb2 = new Ambiente();
            amb2.setNombre("Auditorio Principal");
            amb2.setTipo(TipoAmbiente.AUDITORIO);
            amb2.setCapacidad(100);

            Ambiente amb3 = new Ambiente();
            amb3.setNombre("Torre de Control");
            amb3.setTipo(TipoAmbiente.SALA);
            amb3.setCapacidad(15);

            Ambiente amb4 = new Ambiente();
            amb4.setNombre("Laboratorio de Electrónica");
            amb4.setTipo(TipoAmbiente.LABORATORIO);
            amb4.setCapacidad(25);

            ambienteRepository.saveAll(List.of(amb1, amb2, amb3, amb4));
            System.out.println("🌱 Datos de prueba iniciales de ambientes cargados con éxito.");
        }
    }
}