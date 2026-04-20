package com.seleccion.backend.repositories.seleccion.comentarios_pp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.seleccion.backend.entities.seleccion.comentarios_pp.comentarios_pp_seleccion_enty;

@Repository
public interface comentarios_pp_seleccion_repo extends JpaRepository<comentarios_pp_seleccion_enty, String> {
    Optional<comentarios_pp_seleccion_enty> findByAcronimo(String acronimo);
}
