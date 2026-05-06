package com.seleccion.backend.repositories.variables;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.seleccion.backend.entities.variables.variables_enty;

public interface variables_repo extends JpaRepository<variables_enty, String>{
    boolean existsByIdA(String idA);

    boolean existsByIdSAndIdFuente(String idS, String idFuente);

    List<variables_enty> findByIdS(String idS);

    List<variables_enty> findByIdFuente(String idFuente);

    List<variables_enty> findByIdFuenteInOrderByIdFuenteDescIdAAsc(List<String> idFuentes);

    Long countByIdFuente(String idFuente);

    Long countByPrioridad(Integer prioridad);
    
    Page<variables_enty> findByIdFuente(String idFuente, Pageable pageable);

}
