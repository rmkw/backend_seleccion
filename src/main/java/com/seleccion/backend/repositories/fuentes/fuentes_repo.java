package com.seleccion.backend.repositories.fuentes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seleccion.backend.entities.fuentes.fuentes_enty;

public interface fuentes_repo extends JpaRepository<fuentes_enty, String> {

    List<fuentes_enty> findByAcronimo(String acronimo);

    List<fuentes_enty> findByAcronimoOrderByIdFuenteDesc(String acronimo);

    Optional<fuentes_enty> findByIdFuente(String idFuente);

    boolean existsByIdFuente(String idFuente);

    @Modifying
    @Query(value = """
            INSERT INTO seleccion.fuentes
            (acronimo, fuente, url, edicion, comentario_s, comentario_a,
             responsable_register, responsable_actualizacion, id_fuente_seleccion)
            VALUES
            (:acronimo, :fuente, :url, :edicion, :comentarioS, :comentarioA,
             :responsableRegister, :responsableActualizacion, :idFuenteSeleccion)
            """, nativeQuery = true)
    int insertarFuente(
            @Param("acronimo") String acronimo,
            @Param("fuente") String fuente,
            @Param("url") String url,
            @Param("edicion") String edicion,
            @Param("comentarioS") String comentarioS,
            @Param("comentarioA") String comentarioA,
            @Param("responsableRegister") Integer responsableRegister,
            @Param("responsableActualizacion") Integer responsableActualizacion,
            @Param("idFuenteSeleccion") String idFuenteSeleccion);

    @Modifying
    @Query(value = """
            UPDATE seleccion.fuentes
            SET acronimo = :acronimo,
                fuente = :fuente,
                url = :url,
                edicion = :edicion,
                comentario_s = :comentarioS,
                comentario_a = :comentarioA,
                responsable_actualizacion = :responsableActualizacion,
                id_fuente_seleccion = :idFuenteSeleccion
            WHERE id_fuente = :idFuenteActual
            """, nativeQuery = true)
    int actualizarFuente(
            @Param("idFuenteActual") String idFuenteActual,
            @Param("acronimo") String acronimo,
            @Param("fuente") String fuente,
            @Param("url") String url,
            @Param("edicion") String edicion,
            @Param("comentarioS") String comentarioS,
            @Param("comentarioA") String comentarioA,
            @Param("responsableActualizacion") Integer responsableActualizacion,
            @Param("idFuenteSeleccion") String idFuenteSeleccion);
}