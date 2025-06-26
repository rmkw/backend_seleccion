package com.seleccion.backend.repositories.fuentes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seleccion.backend.entities.fuentes.fuentes_enty;

public interface fuentes_repo extends JpaRepository<fuentes_enty, Integer> {

    @Query("""
                SELECT f FROM fuentes_enty f
                WHERE f.proceso.acronimo = :acronimo
                  AND f.responsableRegister = :responsableRegister
                ORDER BY f.idFuente DESC
            """)
    List<fuentes_enty> findByProcesoAcronimoAndResponsableRegister(
            @Param("acronimo") String acronimo,
            @Param("responsableRegister") Integer responsableRegister);
}
