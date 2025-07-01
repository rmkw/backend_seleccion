
package com.seleccion.backend.repositories.mdea.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;


public interface cat_subcomponente_repo extends JpaRepository<cat_subcomponente_enty, Integer> {
    List<cat_subcomponente_enty> findByIdComponente(Integer idComponente);
}

    

