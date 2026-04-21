package com.seleccion.backend.controllers.fuentes;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seleccion.backend.entities.fuentes.fuentes_dto;
import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.services.fuentes.fuentes_services;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
public class fuentes_ctr {

    private final fuentes_services service;

    @GetMapping("/por-acronimo")
    public ResponseEntity<List<fuentes_dto>> getByAcronimo(
            @RequestParam String acronimo) {
        return ResponseEntity.ok(service.getByAcronimo(acronimo));
    }

    @GetMapping("/by-id")
    public ResponseEntity<fuentes_enty> getByIdFuente(
            @RequestParam String idFuente) {
        return ResponseEntity.ok(service.getByIdFuente(idFuente));
    }

    @PostMapping
    public ResponseEntity<fuentes_enty> create(
            @RequestBody fuentes_enty fuente) {
        return ResponseEntity.ok(service.create(fuente));
    }

    @PutMapping("/update")
    public ResponseEntity<fuentes_enty> update(
            @RequestParam String idFuente,
            @RequestBody fuentes_enty datos) {
        return ResponseEntity.ok(service.update(idFuente, datos));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFuente(
            @RequestParam String idFuente) {
        service.deleteById(idFuente);
        return ResponseEntity.ok(Collections.singletonMap("message", "Fuente eliminada correctamente"));
    }

    @GetMapping("/count")
    public ResponseEntity<?> countFuentes() {
        return ResponseEntity.ok(Collections.singletonMap("total", service.contarFuentes()));
    }
}