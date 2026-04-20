package com.seleccion.backend.controllers.armonizacion.comentarios_pp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.armonizacion.comentarios_pp.comentarios_pp_armo_dto;
import com.seleccion.backend.entities.armonizacion.comentarios_pp.comentarios_pp_armo_enty;
import com.seleccion.backend.services.armonizacion.comentarios_pp.comentarios_pp_armo_services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/armo/comentarios-pp")
@RequiredArgsConstructor
public class comentarios_pp_armo_ctr {
    private final comentarios_pp_armo_services service;

    @GetMapping("/buscar")
    public ResponseEntity<comentarios_pp_armo_enty> obtenerPorAcronimo(
            @RequestParam String acronimo
    ) {
        return ResponseEntity.ok(service.obtenerPorAcronimo(acronimo));
    }

    @PostMapping("/guardar")
    public ResponseEntity<comentarios_pp_armo_enty> guardarComentario(
            @RequestBody comentarios_pp_armo_dto dto
    ) {
        return ResponseEntity.ok(service.guardarComentario(dto));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<comentarios_pp_armo_enty> actualizarComentario(
            @RequestBody comentarios_pp_armo_dto dto
    ) {
        return ResponseEntity.ok(service.actualizarComentario(dto));
    }

    @PostMapping("/guardar-o-actualizar")
    public ResponseEntity<comentarios_pp_armo_enty> guardarOActualizar(
            @RequestBody comentarios_pp_armo_dto dto
    ) {
        return ResponseEntity.ok(service.guardarOActualizar(dto));
    }
    
}
