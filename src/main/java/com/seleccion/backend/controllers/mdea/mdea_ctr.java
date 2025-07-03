package com.seleccion.backend.controllers.mdea;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico1_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico2_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_tema_enty;
import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
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

    @GetMapping("/subcomponente/comp/{id_componente}")
    public ResponseEntity<List<cat_subcomponente_enty>> getSubcomponentesByCompId(@PathVariable Integer id_componente) {
        List<cat_subcomponente_enty> subcomponentes = service.getSubcomponentesByCompId(id_componente);
        if (subcomponentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subcomponentes);
    }

    @GetMapping("/topicos/comp/{id_componente}/sub/{id_subcomponente}")
    public ResponseEntity<List<cat_tema_enty>> getTopicosByCompAndSub(
            @PathVariable Integer id_componente,
            @PathVariable Integer id_subcomponente) {

        List<cat_tema_enty> topicos = service.getTopicosByCompAndSub(id_componente, id_subcomponente);
        if (topicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/estadistico1/id_componente/{id_componente}/id_subcomponente/{id_subcomponente}/id_tema/{id_tema}")
    public ResponseEntity<List<cat_estadistico1_enty>> getVariablesByCompSubTop(
            @PathVariable("id_componente") Integer idComponente,
            @PathVariable("id_subcomponente") Integer idSubcomponente,
            @PathVariable("id_tema") Integer idTema) {

        List<cat_estadistico1_enty> variables = service.getVariablesByCompSubTop(idComponente, idSubcomponente, idTema);

        if (variables.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(variables);
    }

    @GetMapping("estadistico2/componente/{id_componente}/subcomponente/{id_subcomponente}/tema/{id_tema}/estadistico1/{id_estadistico1}")
    public ResponseEntity<List<cat_estadistico2_enty>> getEstadisticosByCompSubTopVar(
            @PathVariable("id_componente") Integer idComponente,
            @PathVariable("id_subcomponente") Integer idSubcomponente,
            @PathVariable("id_tema") Integer idTema,
            @PathVariable("id_estadistico1") String idEstadistico1) {

        List<cat_estadistico2_enty> estadisticos = service.getEstadisticosByCompSubTopVar(
                idComponente, idSubcomponente, idTema, idEstadistico1);

        if (estadisticos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadisticos);
    }

    @PostMapping
    public mdea_enty crearRelacion(@RequestBody mdea_enty relation) {
        return service.save(relation);
    }

    @GetMapping("/{idA}")
    public List<mdea_enty> getPorIdVariableUnique(@PathVariable String idA) {
        return service.getByIdA(idA);
    }

}
