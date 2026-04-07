package com.seleccion.backend.services.tematicas_temas;



import java.util.List;

import com.seleccion.backend.entities.tematicas_temas.tematicas_enty;



public interface tematicas_service {

    List<tematicas_enty> obtenerPorAcronimo(String acronimo);
}