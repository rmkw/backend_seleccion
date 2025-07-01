package com.seleccion.backend.services.unidades;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.unidades.unidades_enty;
import com.seleccion.backend.repositories.unidades.unidades_repo;

@Service
public class unidades_services {
    
    @Autowired
    private unidades_repo repo;

    public List<unidades_enty> getAllDir() {
        return repo.findAll(Sort.by("idUnidad"));
    }
}
