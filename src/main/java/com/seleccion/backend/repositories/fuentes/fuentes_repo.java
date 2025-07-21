package com.seleccion.backend.repositories.fuentes;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seleccion.backend.entities.fuentes.fuentes_enty;

public interface fuentes_repo extends JpaRepository<fuentes_enty, String> {

    List<fuentes_enty> findByAcronimoAndResponsableRegister(String acronimo, Integer responsableRegister);

    List<fuentes_enty> findByAcronimoAndResponsableRegisterOrderByEdicionDesc(String acronimo,
            Integer responsableRegister);

}

