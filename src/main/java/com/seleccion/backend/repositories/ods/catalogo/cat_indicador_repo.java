
package com.seleccion.backend.repositories.ods.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seleccion.backend.entities.ods.catalogo.cat_indicador_enty;

public interface cat_indicador_repo extends JpaRepository<cat_indicador_enty, Integer> {
    
    // Custom query methods can be defined here if needed
    // For example, to find by name:
    // List<cat_componente_enty> findByName(String name);
    
    // You can also define methods for pagination and sorting if required

    
}