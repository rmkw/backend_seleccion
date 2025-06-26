package com.seleccion.backend.services.fuentes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;
@Service
public class fuentes_services {
    @Autowired
    private fuentes_repo repo;

    public List<fuentes_enty> getFuentesPorProcesoYResponsable(String acronimo, Integer responsableRegister) {
    return repo.findByProcesoAcronimoAndResponsableRegister(acronimo, responsableRegister);
    }

    public fuentes_enty create(fuentes_enty fuente) {
        return repo.save(fuente);
    }    

   


}
