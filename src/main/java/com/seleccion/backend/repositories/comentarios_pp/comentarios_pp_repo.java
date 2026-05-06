package com.seleccion.backend.repositories.comentarios_pp;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.comentarios_pp.comentarios_pp_enty;

public interface comentarios_pp_repo extends JpaRepository<comentarios_pp_enty, String> {
}