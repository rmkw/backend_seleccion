package com.seleccion.backend.repositories.armonizacion.comentarios_pp;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seleccion.backend.entities.armonizacion.comentarios_pp.comentarios_pp_armo_enty;

@Repository
public interface comentarios_pp_armo_repo extends JpaRepository<comentarios_pp_armo_enty, String> {
    Optional<comentarios_pp_armo_enty> findByAcronimo(String acronimo);
}
