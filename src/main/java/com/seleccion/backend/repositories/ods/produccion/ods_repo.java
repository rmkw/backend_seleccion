
package com.seleccion.backend.repositories.ods.produccion;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seleccion.backend.entities.ods.produccion.ods_enty;



public interface ods_repo extends JpaRepository<ods_enty, Integer> {

    // Custom query methods can be defined here if needed
    // For example, to find by name:
    // List<cat_componente_enty> findByName(String name);

    // You can also define methods for pagination and sorting if required

}