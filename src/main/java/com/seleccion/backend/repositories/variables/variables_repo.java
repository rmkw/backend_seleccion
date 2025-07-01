package com.seleccion.backend.repositories.variables;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.variables.variables_enty;

public interface variables_repo extends JpaRepository<variables_enty, String>{
    boolean existsByIdSAndIdFuente(String idS, Integer idFuente);

}
