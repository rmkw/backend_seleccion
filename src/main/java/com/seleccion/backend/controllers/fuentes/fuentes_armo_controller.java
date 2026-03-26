package com.seleccion.backend.controllers.fuentes;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.fuentes.fuente_save_dto;
import com.seleccion.backend.entities.fuentes.fuentes_armo_enty;
import com.seleccion.backend.services.fuentes.fuentes_armo_service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/armo/fuentes")
@RequiredArgsConstructor
public class fuentes_armo_controller {
    private final fuentes_armo_service service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public fuentes_armo_enty createFuente(@RequestBody fuente_save_dto dto) {
        return service.createFuente(dto);
    }

    @GetMapping("/{idFuente}")
    public fuentes_armo_enty getFuenteById(@PathVariable String idFuente) {
        return service.getFuenteById(idFuente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fuente no encontrada"));
    }

    

    @GetMapping("/exists/{idFuente}")
    public Map<String, Boolean> existsFuenteById(@PathVariable String idFuente) {
        return Map.of("exists", service.existsFuenteById(idFuente));
    }

    @GetMapping("/exists-by-id-fuente-seleccion/{idFuenteSeleccion}")
    public Map<String, Boolean> existsFuenteByIdFuenteSeleccion(@PathVariable String idFuenteSeleccion) {
        return Map.of("exists", service.existsFuenteByIdFuenteSeleccion(idFuenteSeleccion));
    }

    @PostMapping("/exists-by-data")
    public ResponseEntity<Map<String, Object>> existsFuenteByData(@RequestBody fuente_save_dto dto) {
        fuentes_armo_enty fuente = service.getFuenteByData(dto).orElse(null);

        if (fuente != null) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "idFuente", fuente.getIdFuente(),
                    "idFuenteSeleccion", fuente.getIdFuenteSeleccion()));
        }

        String idFuenteCalculado = service.construirIdFuentePublic(dto);

        return ResponseEntity.ok(Map.of(
                "exists", false,
                "idFuente", idFuenteCalculado,
                "idFuenteSeleccion", dto.getIdFuenteSeleccion()));
    }

    @PutMapping
    public fuentes_armo_enty updateFuente(@RequestBody fuente_save_dto dto) {
        return service.updateFuente(dto);
    }
    
    @GetMapping("/by-id-fuente-seleccion")
    public fuentes_armo_enty getFuenteByIdFuenteSeleccion(@RequestParam String idFuenteSeleccion) {
        return service.getFuenteByIdFuenteSeleccion(idFuenteSeleccion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fuente no encontrada por idFuenteSeleccion"));
    }
}