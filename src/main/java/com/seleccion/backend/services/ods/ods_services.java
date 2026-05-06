package com.seleccion.backend.services.ods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

@Service
public class ods_services {
    @Autowired
    private ods_repo repository;

    @Autowired
    private variables_repo variablesRepository;

    @Transactional
    public ods_enty save(ods_enty relacion) {
        if (relacion.getIdA() == null || relacion.getIdA().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta idA");
        }

        if (relacion.getIdS() == null || relacion.getIdS().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta idS");
        }

        if (relacion.getObjetivo() == null || relacion.getObjetivo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un objetivo");
        }

        boolean exists = repository.existsByIdAAndObjetivoAndMetaAndIndicador(
                relacion.getIdA(),
                relacion.getObjetivo(),
                relacion.getMeta(),
                relacion.getIndicador()
        );

        if (exists) {
            throw new IllegalArgumentException("Ya existe esta relación ODS para esta variable.");
        }

        ods_enty nueva = repository.save(relacion);

        variables_enty variable = variablesRepository.findById(relacion.getIdA())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada"));

        variable.setOds(true);
        variablesRepository.save(variable);

        return nueva;
    }   

    public List<ods_enty> getByIdA(String idA) {
        return repository.findByIdA(idA);
    }

    @Transactional
    public void deleteById(Integer id) {
        ods_enty relacion = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relación ODS no encontrada"));

        String idA = relacion.getIdA();

        repository.deleteById(id);

        List<ods_enty> restantes = repository.findByIdA(idA);

        if (restantes.isEmpty()) {
            variablesRepository.findById(idA).ifPresent(variable -> {
                variable.setOds(false);
                variablesRepository.save(variable);
            });
        }
    }
}
