package com.seleccion.backend.controllers.tematicas_temas;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seleccion.backend.entities.tematicas_temas.tematicas_enty;
import com.seleccion.backend.services.tematicas_temas.tematicas_service;



@RestController
@RequestMapping("/api/catalog/tematicas")
@CrossOrigin(origins = "*")
public class tematicas_controller {

    private final tematicas_service tematicasService;

    public tematicas_controller(tematicas_service tematicasService) {
        this.tematicasService = tematicasService;
    }

    @GetMapping("/proceso/{acronimo}")
    public ResponseEntity<List<tematicas_enty>> obtenerPorAcronimo(@PathVariable String acronimo) {
        List<tematicas_enty> tematicas = tematicasService.obtenerPorAcronimo(acronimo);
        return ResponseEntity.ok(tematicas);
    }
}