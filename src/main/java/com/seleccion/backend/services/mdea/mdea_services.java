package com.seleccion.backend.services.mdea;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;
import com.seleccion.backend.repositories.mdea.catalogo.cat_componente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_subcomponente_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class mdea_services {

    private final cat_componente_repo repo_com;

    private final cat_subcomponente_repo repo_sub;


    public List<cat_componente_enty> obtenerTodos() {
        return repo_com.findAll();
    }

    public List<cat_subcomponente_enty> getSubcomponentesByCompId(Integer idComponente) {
        return repo_sub.findByIdComponente(idComponente);
    }
}
