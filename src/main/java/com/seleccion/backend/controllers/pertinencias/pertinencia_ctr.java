package com.seleccion.backend.controllers.pertinencias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.services.pertinencias.pertinencia_services;

@RestController
@RequestMapping("/api/pertinencia")
public class pertinencia_ctr {
    @Autowired
    private pertinencia_services service;

    @PostMapping
    public ResponseEntity<pertinencia_enty> crear(@RequestBody pertinencia_enty pertinencia_enty) {
        pertinencia_enty nuevo = service.guardar(pertinencia_enty);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/{idVariableUnique}")
    public ResponseEntity<pertinencia_enty> obtenerPorIdVariable(@PathVariable String idVariableUnique) {
        return service.buscarPorIdA(idVariableUnique)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
