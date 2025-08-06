package com.seleccion.backend.services.procesos;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.procesos.procesos_dto;
import com.seleccion.backend.entities.procesos.procesos_enty;
import com.seleccion.backend.repositories.procesos.procesos_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class procesos_services {

    private final procesos_repo repo;

    public List<procesos_enty> obtenerTodos(){
        return repo.findAll();
    }

    public List<procesos_dto> obtenerPorunidad(String unidad) {
        return repo.findProcesosConConteoVariablesByUnidad(unidad);
    }

    public void actualizarComentario(String acronimo, String comentario) {
        repo.actualizarComentarioPorAcronimo(acronimo, comentario);
    }
    
    public List<procesos_dto> obtenerTodosPorUnidad(String unidad) {
        return repo.findProcesosConConteoVariablesByUnidad(unidad);
    }

    public ResponseEntity<?> registrarProceso(procesos_enty nuevoProceso) {
    String acronimo = nuevoProceso.getAcronimo();

    if (repo.existsById(acronimo)) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Ya existe un proceso con ese acrónimo"));
    }

    repo.save(nuevoProceso);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Map.of("message", "Proceso registrado exitosamente"));
}

    
    
}
