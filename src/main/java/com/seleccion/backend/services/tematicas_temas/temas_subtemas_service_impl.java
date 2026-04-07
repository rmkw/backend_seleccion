package com.seleccion.backend.services.tematicas_temas;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.tematicas_temas.temas_subtemas_enty;
import com.seleccion.backend.repositories.tematicas_temas.temas_subtemas_repo;

@Service
public class temas_subtemas_service_impl implements temas_subtemas_service {
    private final temas_subtemas_repo temasSubtemasRepo;

    public temas_subtemas_service_impl(temas_subtemas_repo temasSubtemasRepo) {
        this.temasSubtemasRepo = temasSubtemasRepo;
    }

    @Override
    public List<String> obtenerTemas() {
        return temasSubtemasRepo.findTemasDistinct();
    }

    @Override
    public List<temas_subtemas_enty> obtenerSubtemasPorTema(String tema) {
        return temasSubtemasRepo.findByTemaOrderBySubtemaAsc(tema);
}


}