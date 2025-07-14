package com.seleccion.backend.repositories.variables;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.seleccion.backend.entities.variables.variables_enty;

public interface variables_repo extends JpaRepository<variables_enty, String>{
    boolean existsByIdSAndIdFuente(String idS, Integer idFuente);

    List<variables_enty> findByResponsableRegisterAndIdFuenteOrderByIdA(Integer responsableRegister, Integer idFuente);

    List<variables_enty> findByIdA(String idA);

    List<variables_enty> findByIdS(String idS);

    List<variables_enty> findByIdFuente(Integer idFuente);

    void deleteById(@NonNull String idA);

}
