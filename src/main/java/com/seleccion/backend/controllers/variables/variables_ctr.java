package com.seleccion.backend.controllers.variables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.services.variables.variables_services;

@RestController
@RequestMapping("/api/variables")
public class variables_ctr {
    @Autowired
    private variables_services service;

    @PostMapping
    public ResponseEntity<variables_enty> crearVariable(@RequestBody variables_enty variable) {
        variables_enty nueva = service.crearVariable(variable);
        return ResponseEntity.ok(nueva);
    }
}
