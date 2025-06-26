package com.seleccion.backend.repositories.procesos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.seleccion.backend.entities.procesos.procesos_enty;



public interface procesos_repo extends JpaRepository<procesos_enty, String>  {
    List<procesos_enty> findByunidadIgnoreCaseOrderByProcesoAsc(String unidad);

    @Modifying
    @Transactional
    @Query("UPDATE procesos_enty p SET p.comentarioS = :comentario WHERE p.acronimo = :acronimo")
    void actualizarComentarioPorAcronimo(@Param("acronimo") String acronimo, @Param("comentario") String comentario);

} 
