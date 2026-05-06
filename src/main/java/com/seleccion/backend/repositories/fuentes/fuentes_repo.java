package com.seleccion.backend.repositories.fuentes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.fuentes.fuentes_enty;

public interface fuentes_repo extends JpaRepository<fuentes_enty, String> {

        List<fuentes_enty> findByAcronimo(String acronimo);

        List<fuentes_enty> findByAcronimoOrderByIdFuenteSeleccionDesc(String acronimo);

        Optional<fuentes_enty> findByIdFuenteSeleccion(String idFuenteSeleccion);

        boolean existsByIdFuenteSeleccion(String idFuenteSeleccion);

        Optional<fuentes_enty> findByIdFuente(String idFuente);

        boolean existsByIdFuente(String idFuente);
}