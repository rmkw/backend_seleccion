package com.seleccion.backend.services.procesos;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public List<procesos_enty> obtenerPorunidad(String unidad) {
        return repo.findByunidadIgnoreCaseOrderByProcesoAsc(unidad);
    }

    public void actualizarComentario(String acronimo, String comentario) {
        repo.actualizarComentarioPorAcronimo(acronimo, comentario);
    }
    
}
