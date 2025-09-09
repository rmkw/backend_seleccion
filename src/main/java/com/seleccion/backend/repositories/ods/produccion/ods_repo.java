
package com.seleccion.backend.repositories.ods.produccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.ods.produccion.ods_enty;



public interface ods_repo extends JpaRepository<ods_enty, Integer> {

    List<ods_enty> findByIdA(String idA);

    List<ods_enty> findByIdS(String idS);

    void deleteByIdA(String idA);

    boolean existsByIdAAndObjetivoAndMetaAndIndicador(
            String idA,
            String objetivo,
            String meta,
            String indicador);

}