package com.seleccion.backend.services.mdea;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.mdea.catalogo.cat_componente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico1_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_estadistico2_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_subcomponente_enty;
import com.seleccion.backend.entities.mdea.catalogo.cat_tema_enty;
import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.repositories.mdea.catalogo.cat_componente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico1_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico2_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_subcomponente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_tema_repo;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

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

    private final variables_repo repo_variables;


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

    @Transactional
    public mdea_enty save(mdea_enty relation) {
        if (relation.getIdA() == null || relation.getIdA().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta idA");
        }

        if (relation.getIdS() == null || relation.getIdS().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta idS");
        }

        if (relation.getComponente() == null || relation.getComponente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un componente");
        }

        boolean exists = repo_mdea.existsByIdAAndComponenteAndSubcomponenteAndTemaAndEstadistica1AndEstadistica2(
                relation.getIdA(),
                relation.getComponente(),
                relation.getSubcomponente(),
                relation.getTema(),
                relation.getEstadistica1(),
                relation.getEstadistica2()
        );

        if (exists) {
            throw new IllegalArgumentException("Ya existe una relación con estos valores para esta variable.");
        }

        mdea_enty nueva = repo_mdea.save(relation);

        variables_enty variable = repo_variables.findById(relation.getIdA())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada"));

        variable.setMdea(true);
        repo_variables.save(variable);

        return nueva;
    }  

    public List<mdea_enty> getByIdA(String idA) {
        return repo_mdea.findByIdA(idA);
    }

    @Transactional
    public void deleteById(Integer idUnique) {
        mdea_enty relacion = repo_mdea.findById(idUnique)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relación MDEA no encontrada"));

        String idA = relacion.getIdA();

        repo_mdea.deleteById(idUnique);

        List<mdea_enty> restantes = repo_mdea.findByIdA(idA);

        if (restantes.isEmpty()) {
            repo_variables.findById(idA).ifPresent(variable -> {
                variable.setMdea(false);
                repo_variables.save(variable);
            });
        }
    }
    
}
