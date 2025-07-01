package com.seleccion.backend.controllers.fuentes;




import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;

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

        List<fuentes_enty> result = service.getByAcronimoAndResponsable(acronimo, responsableRegister);
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<fuentes_enty> create(@RequestBody fuentes_enty fuente) {
        fuentes_enty creada = service.create(fuente);
        return ResponseEntity.ok(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFuente(@PathVariable Integer id) {
        Map<String, Object> result = service.deleteFuenteById(id);
        return ResponseEntity.ok(result);
    }

    


    @PutMapping("/{id}")
    public ResponseEntity<fuentes_enty> update(@PathVariable Integer id, @RequestBody fuentes_enty datos) {
        return repository.findById(id)
                .map(actual -> {
                    actual.setFuente(datos.getFuente());
                    actual.setUrl(datos.getUrl());
                    actual.setEdicion(datos.getEdicion());
                    actual.setComentarioS(datos.getComentarioS());
                    actual.setResponsableActualizacion(datos.getResponsableActualizacion());

                    // 👇 NO tocar responsableRegister para evitar errores de integridad
                    return ResponseEntity.ok(repository.save(actual));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    
}
