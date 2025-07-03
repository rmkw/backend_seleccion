package com.seleccion.backend.repositories.mdea.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico2_enty;


public interface cat_estadistico2_repo extends JpaRepository<cat_estadistico2_enty, String> {
    List<cat_estadistico2_enty> findByIdComponenteAndIdSubcomponenteAndIdTemaAndIdEstadistico1OrderByUniqueId(
            Integer idComponente,
            Integer idSubcomponente,
            Integer idTema,
            String idEstadistico1);

}
