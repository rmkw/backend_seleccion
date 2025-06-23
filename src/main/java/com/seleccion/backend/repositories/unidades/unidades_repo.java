package com.seleccion.backend.repositories.unidades;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.unidades.unidades_enty;

public interface unidades_repo extends JpaRepository<unidades_enty, Long> {
    
}
