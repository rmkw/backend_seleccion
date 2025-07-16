package com.seleccion.backend.repositories.procesos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.seleccion.backend.entities.procesos.procesos_dto;
import com.seleccion.backend.entities.procesos.procesos_enty;



public interface procesos_repo extends JpaRepository<procesos_enty, String>  {
    List<procesos_enty> findByunidadIgnoreCaseOrderByProcesoAsc(String unidad);

    @Modifying
    @Transactional
    @Query("UPDATE procesos_enty p SET p.comentarioS = :comentario WHERE p.acronimo = :acronimo")
    void actualizarComentarioPorAcronimo(@Param("acronimo") String acronimo, @Param("comentario") String comentario);


    @Query("""
                SELECT new com.seleccion.backend.entities.procesos.procesos_dto(
                    p.acronimo,
                    p.proceso,
                    p.metodo,
                    p.objetivo,
                    p.pobjeto,
                    p.uobservacion,
                    p.unidad,
                    p.periodicidad,
                    p.iin,
                    p.estatus,
                    p.comentarioS,
                    p.comentarioA,
                    (SELECT COUNT(v) FROM variables_enty v WHERE v.acronimo = p.acronimo)
                )
                FROM procesos_enty p
                WHERE LOWER(p.unidad) = LOWER(:unidad)
                ORDER BY p.proceso ASC
            """)
    List<procesos_dto> findProcesosConConteoVariablesByUnidad(@Param("unidad") String unidad);

} 
