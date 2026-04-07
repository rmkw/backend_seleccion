package com.seleccion.backend.repositories.tematicas_temas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seleccion.backend.entities.tematicas_temas.temas_subtemas_enty;
@Repository
public interface temas_subtemas_repo extends JpaRepository<temas_subtemas_enty, Integer>{
    @Query("SELECT DISTINCT t.tema FROM temas_subtemas_enty t ORDER BY t.tema ASC")
    List<String> findTemasDistinct();

    List<temas_subtemas_enty> findByTemaOrderBySubtemaAsc(String tema);
}
