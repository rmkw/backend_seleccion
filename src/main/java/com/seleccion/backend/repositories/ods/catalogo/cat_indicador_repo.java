
package com.seleccion.backend.repositories.ods.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seleccion.backend.entities.ods.catalogo.cat_indicador_enty;

public interface cat_indicador_repo extends JpaRepository<cat_indicador_enty, Integer> {
    
    List<cat_indicador_enty> findByIdObjetivoAndIdMeta(Integer idObjetivo, String idMeta);
    
}