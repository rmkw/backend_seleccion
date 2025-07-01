package com.seleccion.backend.services.variables;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.repositories.variables.variables_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class variables_services {
    private final variables_repo repository;

    public variables_enty crearVariable(variables_enty variable) {
        // Validar si ya existe una variable con la misma combinación de id_s +
        // id_fuente
        if (repository.existsByIdSAndIdFuente(variable.getIdS(), variable.getIdFuente())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una variable con ese ID_S y esa fuente");
        }

        try {
            variables_enty nueva = repository.save(variable);
            System.out.println("Nuevo ID registrado: " + nueva.getIdA());
            return nueva;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto al guardar la variable (posible duplicado o error de integridad)", e);
        }
    }
    
}


