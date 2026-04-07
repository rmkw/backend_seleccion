package com.seleccion.backend.controllers.tematicas_temas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.tematicas_temas.temas_subtemas_enty;
import com.seleccion.backend.services.tematicas_temas.temas_subtemas_service;

@RestController
@RequestMapping("/api/catalog/temas-subtemas")
@CrossOrigin(origins = "*")
public class temas_subtemas_controller {
    private final temas_subtemas_service temasSubtemasService;

    public temas_subtemas_controller(temas_subtemas_service temasSubtemasService) {
        this.temasSubtemasService = temasSubtemasService;
    }

    @GetMapping("/temas")
    public ResponseEntity<List<String>> obtenerTemas() {
        return ResponseEntity.ok(temasSubtemasService.obtenerTemas());
    }

    @GetMapping("/subtemas/{tema}")
    public ResponseEntity<List<temas_subtemas_enty>> obtenerSubtemasPorTema(@PathVariable String tema) {
        return ResponseEntity.ok(temasSubtemasService.obtenerSubtemasPorTema(tema));
    }
}
