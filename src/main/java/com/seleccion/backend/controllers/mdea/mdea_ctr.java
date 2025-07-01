package com.seleccion.backend.controllers.mdea;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;
import com.seleccion.backend.services.mdea.mdea_services;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mdea")
@RequiredArgsConstructor
public class mdea_ctr {

    private final mdea_services service;

    @GetMapping("/componentes")
    public List<cat_componente_enty> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/subcomponente/comp/{id_comp}")
    public ResponseEntity<List<cat_subcomponente_enty>> getSubcomponentesByCompId(@PathVariable Integer id_comp) {
        List<cat_subcomponente_enty> subcomponentes = service.getSubcomponentesByCompId(id_comp);
        if (subcomponentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subcomponentes);
    }
}
