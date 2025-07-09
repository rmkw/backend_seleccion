package com.seleccion.backend.services.mdea;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico1_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico2_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_tema_enty;
import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
import com.seleccion.backend.repositories.mdea.catalogo.cat_componente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico1_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico2_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_subcomponente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_tema_repo;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class mdea_services {

    private final cat_componente_repo repo_comp;

    private final cat_subcomponente_repo repo_subc;

    private final cat_tema_repo repo_tema;

    private final cat_estadistico1_repo repo_est1;

    private final cat_estadistico2_repo repo_est2;

    private final mdea_repo repo_mdea;


    public List<cat_componente_enty> obtenerTodos() {
        return repo_comp.findAll();
    }

    public List<cat_subcomponente_enty> getSubcomponentesByCompId(Integer idComponente) {
        return repo_subc.findByIdComponente(idComponente);
    }

    

    public List<cat_tema_enty> getTopicosByCompAndSub(Integer idComponente, Integer idSubcomponente) {
        return repo_tema.findByIdComponenteAndIdSubcomponenteOrderByUniqueId(idComponente, idSubcomponente);
    }

    public List<cat_estadistico1_enty> getVariablesByCompSubTop(Integer idComponente, Integer idSubcomponente,
            Integer idTema) {
        return repo_est1.findByIdComponenteAndIdSubcomponenteAndIdTema(idComponente, idSubcomponente, idTema);
    }

    public List<cat_estadistico2_enty> getEstadisticosByCompSubTopVar(
            Integer idComponente, Integer idSubcomponente, Integer idTema, String idEstadistico1) {

        return repo_est2.findByIdComponenteAndIdSubcomponenteAndIdTemaAndIdEstadistico1OrderByUniqueId(
                idComponente, idSubcomponente, idTema, idEstadistico1);
    }

    public mdea_enty save(mdea_enty relation) {
        return repo_mdea.save(relation);
    }

    public List<mdea_enty> getByIdA(String idA) {
        return repo_mdea.findByIdA(idA);
    }

    public void deleteById(Integer idUnique) {
        repo_mdea.deleteById(idUnique);
    }
    
}
