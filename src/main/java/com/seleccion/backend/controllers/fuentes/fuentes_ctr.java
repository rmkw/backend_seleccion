package com.seleccion.backend.controllers.fuentes;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;
import com.seleccion.backend.services.fuentes.fuentes_services;

public class fuentes_ctr {

    @Autowired
    private fuentes_services service;

    // @GetMapping("/fuentes")
    // public ResponseEntity<?> getByacronimoAndResponsable(
    //         @RequestParam String acronimo,
    //         @RequestParam Integer responsableRegister) {

    //     List<fuentes_enty> result = service.getByacronimoAndResponsable(acronimo, responsableRegister);

    //     return ResponseEntity.ok(Map.of(
    //             "message", "Registros encontrados",
    //             "fuentes", result));
    // }

     @PostMapping
    public ResponseEntity<fuentes_enty> create(@RequestBody fuentes_enty fuente) {
        fuentes_enty creada = service.create(fuente);
        return ResponseEntity.ok(creada);
    }
}
