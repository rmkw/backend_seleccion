package com.seleccion.backend.controllers.unidades;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.unidades.unidades_enty;
import com.seleccion.backend.services.unidades.unidades_service;

@RestController
@RequestMapping("/api/unidad")
public class unidades_ctr {
    @Autowired
    private unidades_service service;

    @GetMapping
    public List<unidades_enty> getAllDirecciones(){
        return service.getAllDir();
    }
}
