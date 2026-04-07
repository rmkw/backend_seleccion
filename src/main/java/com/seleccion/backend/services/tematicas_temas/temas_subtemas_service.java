package com.seleccion.backend.services.tematicas_temas;

import java.util.List;

import com.seleccion.backend.entities.tematicas_temas.temas_subtemas_enty;

public interface temas_subtemas_service {
    List<String> obtenerTemas();

    List<temas_subtemas_enty> obtenerSubtemasPorTema(String tema);
}


