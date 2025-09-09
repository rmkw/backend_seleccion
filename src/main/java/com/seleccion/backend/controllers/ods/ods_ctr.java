package com.seleccion.backend.controllers.ods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.seleccion.backend.entities.ods.catalogo.cat_indicador_enty;
import com.seleccion.backend.entities.ods.catalogo.cat_meta_enty;
import com.seleccion.backend.entities.ods.catalogo.cat_objetivo_enty;
import com.seleccion.backend.entities.ods.produccion.ods_enty;

import com.seleccion.backend.repositories.ods.catalogo.cat_indicador_repo;
import com.seleccion.backend.repositories.ods.catalogo.cat_meta_repo;
import com.seleccion.backend.repositories.ods.catalogo.cat_objetivo_repo;
import com.seleccion.backend.services.ods.ods_services;

@RestController
@RequestMapping("/api/ods")
public class ods_ctr {
    @Autowired
    private ods_services service;

    @Autowired
    private cat_objetivo_repo objetivo_repo;

    @Autowired
    private cat_meta_repo meta_repo;

    @Autowired
    private cat_indicador_repo indicador_repo;

    @GetMapping("/objetivos")
    public List<cat_objetivo_enty> getAllObjetivos() {
        return objetivo_repo.findAll();
    }

    @GetMapping("/metas/{idObjetivo}")
    public List<cat_meta_enty> getMetasByObjetivo(@PathVariable Integer idObjetivo) {
        return meta_repo.findByIdObjetivo(idObjetivo);
    }

    @GetMapping("/indicadores/{idObjetivo}/{idMeta}")
    public List<cat_indicador_enty> getIndicadoresByMeta(
            @PathVariable("idObjetivo") Integer idObjetivo,
            @PathVariable String idMeta) {
        return indicador_repo.findByIdObjetivoAndIdMeta(idObjetivo, idMeta);
    }





    @PostMapping
    public ResponseEntity<?> crearRelacion(@RequestBody ods_enty relacion) {
        try {
            ods_enty nueva = service.save(relacion);
            return ResponseEntity.ok(nueva);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    @GetMapping("/{IdA}")
    public List<ods_enty> obtenerPorIdA(@PathVariable String IdA) {
        return service.getByIdA(IdA);
    }

    @DeleteMapping("/{id}")
    public void eliminarRelacion(@PathVariable Integer id) {
        service.deleteById(id);
    }
}
