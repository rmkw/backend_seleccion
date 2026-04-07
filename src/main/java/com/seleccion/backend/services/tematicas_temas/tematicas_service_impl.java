package com.seleccion.backend.services.tematicas_temas;




import java.util.List;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.tematicas_temas.tematicas_enty;
import com.seleccion.backend.repositories.tematicas_temas.tematicas_repo;



@Service
public class tematicas_service_impl implements tematicas_service {

    private final tematicas_repo tematicasRepo;

    public tematicas_service_impl(tematicas_repo tematicasRepo) {
        this.tematicasRepo = tematicasRepo;
    }

    @Override
    public List<tematicas_enty> obtenerPorAcronimo(String acronimo) {
        return tematicasRepo.findByAcronimoOrderByTematicaAsc(acronimo);
    }
}