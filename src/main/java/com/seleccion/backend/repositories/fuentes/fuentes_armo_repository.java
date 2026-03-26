package com.seleccion.backend.repositories.fuentes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seleccion.backend.entities.fuentes.fuentes_armo_enty;

import jakarta.transaction.Transactional;

public interface fuentes_armo_repository extends JpaRepository<fuentes_armo_enty, String> {

    Optional<fuentes_armo_enty> findByIdFuente(String idFuente);

    boolean existsByIdFuente(String idFuente);

    Optional<fuentes_armo_enty> findByIdFuenteSeleccion(String idFuenteSeleccion);

    boolean existsByIdFuenteSeleccion(String idFuenteSeleccion);
    
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO armonizacion.fuentes
            (acronimo, fuente, url, edicion, comentario_s, comentario_a, id_fuente_seleccion)
            VALUES (:acronimo, :fuente, :url, :edicion, :comentarioS, :comentarioA, :idFuenteSeleccion)
            """, nativeQuery = true)
    void insertFuente(
            @Param("acronimo") String acronimo,
            @Param("fuente") String fuente,
            @Param("url") String url,
            @Param("edicion") String edicion,
            @Param("comentarioS") String comentarioS,
            @Param("comentarioA") String comentarioA,
            @Param("idFuenteSeleccion") String idFuenteSeleccion);

    @Modifying
    @Transactional
    @Query(value = """
                    UPDATE armonizacion.fuentes
                    SET acronimo = :acronimo,
                        fuente = :fuente,
                        url = :url,
                        edicion = :edicion,
                        comentario_s = :comentarioS,
                        comentario_a = :comentarioA
                    WHERE id_fuente_seleccion = :idFuenteSeleccion
                    """, nativeQuery = true)
    int updateFuenteByIdFuenteSeleccion(
                    @Param("acronimo") String acronimo,
                    @Param("fuente") String fuente,
                    @Param("url") String url,
                    @Param("edicion") String edicion,
                    @Param("comentarioS") String comentarioS,
                    @Param("comentarioA") String comentarioA,
                    @Param("idFuenteSeleccion") String idFuenteSeleccion);
}