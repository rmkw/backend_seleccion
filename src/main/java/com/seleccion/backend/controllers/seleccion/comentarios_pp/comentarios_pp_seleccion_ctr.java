package com.seleccion.backend.controllers.seleccion.comentarios_pp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.seleccion.comentarios_pp.comentarios_pp_seleccion_dto;
import com.seleccion.backend.entities.seleccion.comentarios_pp.comentarios_pp_seleccion_enty;
import com.seleccion.backend.services.seleccion.comentarios_pp.comentarios_pp_seleccion_services;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sele/comentarios-pp")
@RequiredArgsConstructor
public class comentarios_pp_seleccion_ctr {
    private final comentarios_pp_seleccion_services service;

    @GetMapping("/buscar")
    public ResponseEntity<comentarios_pp_seleccion_enty> obtenerPorAcronimo(
            @RequestParam String acronimo
    ) {
        return ResponseEntity.ok(service.obtenerPorAcronimo(acronimo));
    }

    @PostMapping("/guardar")
    public ResponseEntity<comentarios_pp_seleccion_enty> guardarComentario(
            @RequestBody comentarios_pp_seleccion_dto dto
    ) {
        return ResponseEntity.ok(service.guardarComentario(dto));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<comentarios_pp_seleccion_enty> actualizarComentario(
            @RequestBody comentarios_pp_seleccion_dto dto
    ) {
        return ResponseEntity.ok(service.actualizarComentario(dto));
    }

    @PostMapping("/guardar-o-actualizar")
    public ResponseEntity<comentarios_pp_seleccion_enty> guardarOActualizar(
            @RequestBody comentarios_pp_seleccion_dto dto
    ) {
        return ResponseEntity.ok(service.guardarOActualizar(dto));
    }
}
