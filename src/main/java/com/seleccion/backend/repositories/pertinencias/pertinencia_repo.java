package com.seleccion.backend.repositories.pertinencias;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seleccion.backend.entities.pertinencias.pertinencia_enty;

public interface pertinencia_repo extends JpaRepository<pertinencia_enty, Integer>{
    Optional<pertinencia_enty> findByIdA(String idA);

    void deleteByIdA(String idA);

    

    
}
