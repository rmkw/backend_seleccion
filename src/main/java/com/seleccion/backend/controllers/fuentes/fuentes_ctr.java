package com.seleccion.backend.controllers.fuentes;




import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.fuentes.fuentes_dto;
import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;
import com.seleccion.backend.services.fuentes.fuentes_services;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
public class fuentes_ctr {

    @Autowired
    private fuentes_services service;

    private final fuentes_repo repository;

    @GetMapping("/byResponsable")
    public ResponseEntity<?> getByAcronimoAndResponsable(
            @RequestParam String acronimo,
            @RequestParam Integer responsableRegister) {

        List<fuentes_dto> result = service.getByAcronimoAndResponsable(acronimo, responsableRegister);

        return ResponseEntity.ok(Map.of(
            "message", "Registros encontrados",
            "fuentes", result
        ));
    }



    @PostMapping
    public ResponseEntity<?> create(@RequestBody fuentes_enty fuente) {
        try {
            fuentes_enty creada = service.create(fuente);
            return ResponseEntity.ok(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al registrar la fuente.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFuente(@PathVariable String id) {
        Map<String, Object> result = service.deleteFuenteById(id);
        return ResponseEntity.ok(result);
    }

    


    @PutMapping("/update")
    public ResponseEntity<fuentes_enty> update(
            @RequestParam("idFuente") String id,
            @RequestBody fuentes_enty datos) {

        return repository.findById(id)
                .map(actual -> {
                    actual.setFuente(datos.getFuente());
                    actual.setUrl(datos.getUrl());
                    actual.setEdicion(datos.getEdicion());
                    actual.setComentarioS(datos.getComentarioS());
                    actual.setResponsableActualizacion(datos.getResponsableActualizacion());

                    // NO tocar responsableRegister
                    return ResponseEntity.ok(repository.save(actual));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-full")
    public ResponseEntity<?> deleteFuenteCascade(@RequestParam("idFuente") String idFuente) {
         System.out.println("Llega ID a backend: " + idFuente);
        Map<String, Object> result = service.deleteFuenteAndCascade(idFuente);
        return ResponseEntity.ok(result);
    }

    
}
