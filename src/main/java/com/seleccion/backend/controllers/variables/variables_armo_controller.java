package com.seleccion.backend.controllers.variables;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seleccion.backend.entities.variables.armo.variables_armo_dto;
import com.seleccion.backend.services.variables.variables_armo_service;

@RestController
@RequestMapping("/api/armo/variables")
public class variables_armo_controller {
    private final variables_armo_service variablesArmoService;

    public variables_armo_controller(variables_armo_service variablesArmoService) {
        this.variablesArmoService = variablesArmoService;
    }

    @GetMapping("/{idA}")
    public ResponseEntity<?> obtenerPorIdA(@PathVariable String idA) {
        Optional<variables_armo_dto> variable = variablesArmoService.obtenerPorIdA(idA);

        if (variable.isPresent()) {
            return ResponseEntity.ok(variable.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No existe la variable en armonización con id_a: " + idA);
    }

    @GetMapping("/existe/{idA}")
    public ResponseEntity<Boolean> existePorIdA(@PathVariable String idA) {
        boolean existe = variablesArmoService.existePorIdA(idA);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<?> guardarVariable(@RequestBody variables_armo_dto dto) {
        try {
            variables_armo_dto guardada = variablesArmoService.guardarVariable(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al guardar la variable en armonización: " + e.getMessage());
        }
    }

    @PutMapping("/{idA}")
    public ResponseEntity<?> actualizarVariable(@PathVariable String idA,
                                                @RequestBody variables_armo_dto dto) {
        try {
            variables_armo_dto actualizada = variablesArmoService.actualizarVariable(idA, dto);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar la variable en armonización: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idA}")
    public ResponseEntity<?> eliminarVariable(@PathVariable String idA) {
        try {
            variablesArmoService.eliminarVariable(idA);
            return ResponseEntity.ok("Variable eliminada correctamente con id_a: " + idA);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar la variable en armonización: " + e.getMessage());
        }
    }
}
