package com.seleccion.backend.controllers.procesos;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.procesos.procesos_dto;
import com.seleccion.backend.entities.procesos.procesos_enty;
import com.seleccion.backend.services.procesos.procesos_services;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/procesos")
@RequiredArgsConstructor
public class procesos_ctr {
    
    private final procesos_services service;

    @GetMapping
    public List<procesos_enty> getTodosLosProcesos(){
        return service.obtenerTodos();
    }

    @GetMapping("/buscar")
    public List<procesos_dto> buscarPorunidad(@RequestParam(value = "unidad_administrativa", required = false) String unidad){
        if (unidad == null || unidad.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el parámetro unidad_administrativa");
        }
        return service.obtenerTodosPorUnidad(unidad);
    }

    

    public class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }    


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarProceso(@RequestBody procesos_enty nuevoProceso) {
        return service.registrarProceso(nuevoProceso);
    }



    


}
