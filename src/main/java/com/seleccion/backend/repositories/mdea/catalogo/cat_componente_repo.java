package com.seleccion.backend.repositories.mdea.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;

public interface cat_componente_repo extends JpaRepository<cat_componente_enty, Integer> {
    
    // Custom query methods can be defined here if needed
    // For example, to find by name:
    // List<cat_componente_enty> findByName(String name);
    
    // You can also define methods for pagination and sorting if required

    
} 