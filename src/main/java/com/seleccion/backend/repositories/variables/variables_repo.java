package com.seleccion.backend.repositories.variables;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.seleccion.backend.entities.variables.variables_enty;

public interface variables_repo extends JpaRepository<variables_enty, String>{
    boolean existsByIdSAndIdFuente(String idS, String idFuente);

    boolean existsByIdA(String idA);

    List<variables_enty> findByResponsableRegisterAndIdFuenteOrderByIdA(Integer responsableRegister, String idFuente);

    List<variables_enty> findByIdA(String idA);

    List<variables_enty> findByIdS(String idS);

    List<variables_enty> findByIdFuente(String idFuente);

    void deleteById(@NonNull String idA);


    @Query("SELECT COUNT(v) FROM variables_enty v WHERE v.idFuente = :idFuente")
    Long countByIdFuente(@Param("idFuente") String idFuente);

}
