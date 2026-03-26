package com.seleccion.backend.repositories.variables;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.seleccion.backend.entities.variables.armo.variables_armo_enty;

@Repository
public interface variables_armo_repo extends JpaRepository<variables_armo_enty, String> {
}