package com.seleccion.backend.repositories.tematicas_temas;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seleccion.backend.entities.tematicas_temas.tematicas_enty;



@Repository
public interface tematicas_repo extends JpaRepository<tematicas_enty, Integer> {

    List<tematicas_enty> findByAcronimoOrderByTematicaAsc(String acronimo);
}