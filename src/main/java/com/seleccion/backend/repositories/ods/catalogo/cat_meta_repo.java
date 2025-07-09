
package com.seleccion.backend.repositories.ods.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.ods.catalogo.cat_meta_enty;

public interface cat_meta_repo extends JpaRepository<cat_meta_enty, String> {
    
    List<cat_meta_enty> findByIdObjetivo(Integer idObjetivo);
    
}