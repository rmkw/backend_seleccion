

package com.seleccion.backend.repositories.mdea.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.seleccion.backend.entities.mdea.catalogo.cat_tema_enty;


public interface cat_tema_repo extends JpaRepository<cat_tema_enty, Integer> {
    List<cat_tema_enty> findByIdComponenteAndIdSubcomponenteOrderByUniqueId(Integer idComponente, Integer idSubcomponente);
}
