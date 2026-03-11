
package com.seleccion.backend.repositories.ods.catalogo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.ods.catalogo.cat_objetivo_enty;



public interface cat_objetivo_repo extends JpaRepository<cat_objetivo_enty, Integer> {
    Optional<cat_objetivo_enty> findByIdObjetivo(Integer idObjetivo);
    

}