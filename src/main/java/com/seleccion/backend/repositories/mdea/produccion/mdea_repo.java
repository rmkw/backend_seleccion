
package com.seleccion.backend.repositories.mdea.produccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.mdea.produccion.mdea_enty;


public interface mdea_repo extends JpaRepository<mdea_enty, Integer> {
    List<mdea_enty> findByIdA(String idA);

    List<mdea_enty> findByIdS(String idS);

    void deleteByIdA(String idA);

    boolean existsByIdAAndComponenteAndSubcomponenteAndTemaAndEstadistica1AndEstadistica2(
            String idA,
            String componente,
            String subcomponente,
            String tema,
            String estadistica1,
            String estadistica2);

}
