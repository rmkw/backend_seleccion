package com.seleccion.backend.repositories.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seleccion.backend.entities.usuario.usuario_enty;

@Repository
public interface usuario_repo extends JpaRepository<usuario_enty, Long> {
    usuario_enty findByNombre(String nombre);
} 
