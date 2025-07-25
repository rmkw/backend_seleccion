package com.seleccion.backend.controllers.variables;

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
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.entities.variables.variables_relacion_dto;
import com.seleccion.backend.services.variables.variables_services;

@RestController
@RequestMapping("/api/variables")
public class variables_ctr {
    @Autowired
    private variables_services service;

    @PostMapping
    public ResponseEntity<variables_enty> crearVariable(@RequestBody variables_enty variable) {
        variables_enty nueva = service.crearVariable(variable);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/filtered/{responsableRegister}/{idFuente}")
    public List<variables_enty> getByResponsableAndFuente(
            @PathVariable Integer responsableRegister,
            @PathVariable String idFuente) {

        return service.getByResponsableAndFuente(responsableRegister, idFuente);
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
