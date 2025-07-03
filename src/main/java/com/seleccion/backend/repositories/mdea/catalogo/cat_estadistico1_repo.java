package com.seleccion.backend.repositories.mdea.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico1_enty;
public interface cat_estadistico1_repo extends JpaRepository<cat_estadistico1_enty, String> {
    List<cat_estadistico1_enty> findByIdComponenteAndIdSubcomponenteAndIdTema(Integer idComponente, Integer idSubcomponente,
            Integer idTema);
    
} 
