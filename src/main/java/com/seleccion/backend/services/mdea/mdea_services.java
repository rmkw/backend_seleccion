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
import com.seleccion.backend.entities.mdea.produccion.mdea_traduccion_dto;
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


    public List<mdea_traduccion_dto> getTablaByIdA(String idA) {
    return repo_mdea.findByIdA(idA)
            .stream()
            .map(this::traducirRelacionMdea)
            .toList();
}

    private mdea_traduccion_dto traducirRelacionMdea(mdea_enty rel) {
        mdea_traduccion_dto dto = new mdea_traduccion_dto();

        dto.setIdUnique(rel.getIdUnique());
        dto.setIdA(rel.getIdA());
        dto.setIdS(rel.getIdS());
        dto.setContribucion(rel.getContribucion());
        dto.setComentarioS(rel.getComentarioS());

        traducirComponente(rel, dto);
        traducirSubcomponente(rel, dto);
        traducirTema(rel, dto);
        traducirEstadistica1(rel, dto);
        traducirEstadistica2(rel, dto);

        return dto;
    }

    private void traducirComponente(mdea_enty rel, mdea_traduccion_dto dto) {
        if (rel.getComponente() == null || rel.getComponente().equals("-")) {
            dto.setComponente("-");
            dto.setComponenteNombre("-");
            return;
        }

        try {
            Integer idComponente = Integer.valueOf(rel.getComponente());

            repo_comp.findByIdComponente(idComponente).ifPresentOrElse(
                    comp -> {
                        dto.setComponente(String.valueOf(comp.getIdComponente()));
                        dto.setComponenteNombre(comp.getNombre());
                    },
                    () -> {
                        dto.setComponente(rel.getComponente());
                        dto.setComponenteNombre("-");
                    });
        } catch (NumberFormatException e) {
            dto.setComponente(rel.getComponente());
            dto.setComponenteNombre("-");
        }
    }

    private void traducirSubcomponente(mdea_enty rel, mdea_traduccion_dto dto) {
        if (rel.getSubcomponente() == null || rel.getSubcomponente().equals("-")) {
            dto.setSubcomponente("-");
            dto.setSubcomponenteNombre("-");
            return;
        }

        try {
            Integer uniqueId = Integer.valueOf(rel.getSubcomponente());

            repo_subc.findByUniqueId(uniqueId).ifPresentOrElse(
                    sub -> {
                        dto.setSubcomponente(String.valueOf(sub.getIdSubcomponente()));
                        dto.setSubcomponenteNombre(sub.getNombre());
                    },
                    () -> {
                        dto.setSubcomponente(rel.getSubcomponente());
                        dto.setSubcomponenteNombre("-");
                    });
        } catch (NumberFormatException e) {
            dto.setSubcomponente(rel.getSubcomponente());
            dto.setSubcomponenteNombre("-");
        }
    }

    private void traducirTema(mdea_enty rel, mdea_traduccion_dto dto) {
        if (rel.getTema() == null || rel.getTema().equals("-")) {
            dto.setTema("-");
            dto.setTemaNombre("-");
            return;
        }

        try {
            Integer uniqueId = Integer.valueOf(rel.getTema());

            repo_tema.findByUniqueId(uniqueId).ifPresentOrElse(
                    tema -> {
                        dto.setTema(String.valueOf(tema.getIdTema()));
                        dto.setTemaNombre(tema.getNombre());
                    },
                    () -> {
                        dto.setTema(rel.getTema());
                        dto.setTemaNombre("-");
                    });
        } catch (NumberFormatException e) {
            dto.setTema(rel.getTema());
            dto.setTemaNombre("-");
        }
    }

    private void traducirEstadistica1(mdea_enty rel, mdea_traduccion_dto dto) {
        if (rel.getEstadistica1() == null || rel.getEstadistica1().equals("-")) {
            dto.setEstadistica1("-");
            dto.setEstadistica1Nombre("-");
            return;
        }

        repo_est1.findByUniqueId(rel.getEstadistica1()).ifPresentOrElse(
                est -> {
                    dto.setEstadistica1(est.getIdEstadistico1());
                    dto.setEstadistica1Nombre(est.getNombre());
                },
                () -> {
                    dto.setEstadistica1(rel.getEstadistica1());
                    dto.setEstadistica1Nombre("-");
                });
    }

    private void traducirEstadistica2(mdea_enty rel, mdea_traduccion_dto dto) {
        if (rel.getEstadistica2() == null || rel.getEstadistica2().equals("-")) {
            dto.setEstadistica2("-");
            dto.setEstadistica2Nombre("-");
            return;
        }

        repo_est2.findByUniqueId(rel.getEstadistica2()).ifPresentOrElse(
                est -> {
                    dto.setEstadistica2(String.valueOf(est.getIdEstadistico2()));
                    dto.setEstadistica2Nombre(est.getNombre());
                },
                () -> {
                    dto.setEstadistica2(rel.getEstadistica2());
                    dto.setEstadistica2Nombre("-");
                });
    }


    
}
