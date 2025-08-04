package com.seleccion.backend.controllers.variables;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.entities.variables.variables_relacion_dto;
import com.seleccion.backend.repositories.variables.variables_repo;
import com.seleccion.backend.services.variables.variables_services;


import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/variables")
public class variables_ctr {
    @Autowired
    private variables_services service;

    @Autowired
    private variables_repo repository;

    @PostMapping
    public ResponseEntity<variables_enty> crearVariable(@RequestBody variables_enty variable) {
        variables_enty nueva = service.crearVariable(variable);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/filtered")
    public Page<variables_enty> getByResponsableAndFuente(
            @RequestParam Integer responsableRegister,
            @RequestParam String idFuente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("idA"));
        return repository.findByResponsableRegisterAndIdFuente(responsableRegister, idFuente, pageable);
    }



    @DeleteMapping("/delete/{idA}")
    public void deleteVariable(@PathVariable String idA) {
        service.deleteByIdA(idA);
    }

    @GetMapping("/por-id/{idS}")
    public List<variables_relacion_dto> getByIdSWithRelations(@PathVariable String idS) {
        return service.getWithRelationsByIdS(idS);
    }

    @DeleteMapping("/delete-full/{idA}")
    public ResponseEntity<Map<String, Object>> deleteVariableCascade(@PathVariable String idA) {
        Map<String, Object> result = service.deleteVariableAndCascade(idA);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/edit/{idA}")
    public ResponseEntity<Map<String, Object>> editarVariable(@PathVariable String idA, @RequestBody variables_relacion_dto dto) {
        Map<String, Object> result = service.editarVariable(idA, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/por-ida/{idA}")
    public variables_relacion_dto getByIdAWithRelations(@PathVariable String idA) {
        return service.getWithRelationsByIdA(idA);
    }

    
}
