package com.seleccion.backend.services.variables;

import java.util.Optional;

import com.seleccion.backend.entities.variables.armo.variables_armo_dto;


public interface variables_armo_service {

    Optional<variables_armo_dto> obtenerPorIdA(String idA);

    boolean existePorIdA(String idA);

    variables_armo_dto guardarVariable(variables_armo_dto dto);

    variables_armo_dto actualizarVariable(String idA, variables_armo_dto dto);

    void eliminarVariable(String idA);

    Long contarVariablesArmonizadas();
    
}