package com.seleccion.backend.services.variables;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
import com.seleccion.backend.entities.mdea.produccion.mdea_traduccion_dto;
import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.entities.ods.produccion.ods_traduccion_dto;
import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.entities.variables.variable_revision_masiva_update_dto;
import com.seleccion.backend.entities.variables.variable_revision_prioridad_dto;
import com.seleccion.backend.entities.variables.variable_revision_update_dto;
import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.entities.variables.variables_relacion_dto;
import com.seleccion.backend.repositories.mdea.catalogo.cat_componente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico1_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_estadistico2_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_subcomponente_repo;
import com.seleccion.backend.repositories.mdea.catalogo.cat_tema_repo;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;
import com.seleccion.backend.repositories.ods.catalogo.cat_indicador_repo;
import com.seleccion.backend.repositories.ods.catalogo.cat_meta_repo;
import com.seleccion.backend.repositories.ods.catalogo.cat_objetivo_repo;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class variables_services {
    private final variables_repo repository;


    @Autowired
    private mdea_repo mdeaRepository;
    @Autowired
    private ods_repo odsRepository;
    @Autowired
    private pertinencia_repo pertinenciaRepository;

    @Autowired
private cat_componente_repo catComponenteRepository;

@Autowired
private cat_subcomponente_repo catSubcomponenteRepository;

@Autowired
private cat_tema_repo catTemaRepository;

@Autowired
private cat_estadistico1_repo catEstadistico1Repository;

@Autowired
private cat_estadistico2_repo catEstadistico2Repository;

@Autowired
private cat_objetivo_repo catObjetivoRepository;

@Autowired
private cat_meta_repo catMetaRepository;

@Autowired
private cat_indicador_repo catIndicadorRepository;

    

    public variables_enty crearVariable(variables_enty variable) {

        if (repository.existsByIdA(variable.getIdA())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una relación de pertinencia registrada para esta variable");
        }        
        
        if (repository.existsByIdSAndIdFuente(variable.getIdS(), variable.getIdFuente())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una variable con ese ID_S y esa fuente");
        }
        if (variable.getRevisada() == null) {
            variable.setRevisada(false);
        }

        if (variable.getPrioridad() == null) {
            variable.setPrioridad(null);
        }

        if (variable.getFechaRevision() == null) {
            variable.setFechaRevision(null);
        }

        if (variable.getResponsableRevision() == null) {
            variable.setResponsableRevision(null);
        }

        try {
            variables_enty nueva = repository.save(variable);
            System.out.println("Nuevo ID registrado: " + nueva.getIdA());
            return nueva;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto al guardar la variable (posible duplicado o error de integridad)", e);
        }
    }

    public List<variables_enty> getByResponsableAndFuente(Integer responsableRegister, String idFuente) {
        return repository.findByResponsableRegisterAndIdFuenteOrderByIdA(responsableRegister,
                idFuente);
    }


    public void deleteByIdA(String idA) {
        if (!repository.existsById(idA)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada");
        }
        repository.deleteById(idA);
    }

    public List<variables_relacion_dto> getWithRelationsByIdS(String idS) {
    List<variables_enty> variables = repository.findByIdS(idS); // este método debe existir

    return variables.stream().map(var -> {
        variables_relacion_dto dto = new variables_relacion_dto();
        BeanUtils.copyProperties(var, dto);

        dto.setMdeas(mdeaRepository.findByIdA(var.getIdA()));
        dto.setOdsList(odsRepository.findByIdA(var.getIdA()));
        
        // Este depende si el repo devuelve Optional o List
        dto.setPertinencia(pertinenciaRepository.findByIdA(var.getIdA()).orElse(null));

        return dto;
    }).collect(Collectors.toList());
}

    @Transactional
    public Map<String, Object> deleteVariableAndCascade(String idA) {
        Optional<variables_enty> optionalVar = repository.findById(idA);

        if (optionalVar.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada");
        }

        // Eliminar relaciones por idA
        pertinenciaRepository.deleteByIdA(idA);
        odsRepository.deleteByIdA(idA);
        mdeaRepository.deleteByIdA(idA);

        // Eliminar variable
        repository.deleteById(idA);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Variable y relaciones eliminadas correctamente");
        return response;
    }

    @Transactional
    public Map<String, Object> editarVariable(String idA, variables_relacion_dto dto) {
        variables_enty variable = repository.findById(idA)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada"));

        variable.setNombre(dto.getNombre());
        variable.setDefinicion(dto.getDefinicion());
        variable.setUrl(dto.getUrl());
        variable.setComentarioS(dto.getComentarioS());
        variable.setMdea(dto.getMdea());
        variable.setOds(dto.getOds());
        variable.setResponsableActualizacion(dto.getResponsableActualizacion());

        // Lógica para eliminar relaciones si se desactiva MDEA u ODS
        if (!Boolean.TRUE.equals(dto.getMdea())) {
            List<mdea_enty> relacionesMdea = mdeaRepository.findByIdA(idA);
            mdeaRepository.deleteAll(relacionesMdea);
        }

        if (!Boolean.TRUE.equals(dto.getOds())) {
            List<ods_enty> relacionesOds = odsRepository.findByIdA(idA);
            odsRepository.deleteAll(relacionesOds);
        }

        repository.save(variable);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Variable actualizada correctamente");
        return response;
    }

    public variables_relacion_dto getWithRelationsByIdA(String idA) {
        Optional<variables_enty> variableOpt = repository.findById(idA);
        if (variableOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada");
        }

        variables_enty variable = variableOpt.get();

        List<mdea_enty> mdeas = mdeaRepository.findByIdA(idA);
        List<ods_enty> odsList = odsRepository.findByIdA(idA);
        Optional<pertinencia_enty> pertinenciaOpt = pertinenciaRepository.findByIdA(idA);

        return variables_relacion_dto.builder()
                .idA(variable.getIdA())
                .idS(variable.getIdS())
                .idFuente(variable.getIdFuente())
                .acronimo(variable.getAcronimo())
                .nombre(variable.getNombre())
                .definicion(variable.getDefinicion())
                .url(variable.getUrl())
                .comentarioS(variable.getComentarioS())
                .mdea(variable.getMdea())
                .ods(variable.getOds())
                .responsableRegister(variable.getResponsableRegister())
                .responsableActualizacion(variable.getResponsableActualizacion())
                .mdeas(mdeas)
                .odsList(odsList)
                .pertinencia(pertinenciaOpt.orElse(null))
                .build();
    }
    
    private Integer parseInteger(String value) {
        try {
            return (value == null || value.isBlank() || value.equals("-")) ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private mdea_traduccion_dto traducirMdea(mdea_enty mdea) {
        Integer componenteId = parseInteger(mdea.getComponente());
        Integer subcomponenteUniqueId = parseInteger(mdea.getSubcomponente());
        Integer temaUniqueId = parseInteger(mdea.getTema());
        String estadistica1UniqueId = normalizarTexto(mdea.getEstadistica1());
        String estadistica2UniqueId = normalizarTexto(mdea.getEstadistica2());

        String componenteNombre = componenteId != null
                ? catComponenteRepository.findByIdComponente(componenteId)
                        .map(c -> c.getNombre())
                        .orElse(null)
                : null;

        String subcomponenteNombre = subcomponenteUniqueId != null
                ? catSubcomponenteRepository.findByUniqueId(subcomponenteUniqueId)
                        .map(s -> s.getNombre())
                        .orElse(null)
                : null;

        String temaNombre = temaUniqueId != null
                ? catTemaRepository.findByUniqueId(temaUniqueId)
                        .map(t -> t.getNombre())
                        .orElse(null)
                : null;

        String estadistica1Nombre = (estadistica1UniqueId != null && !estadistica1UniqueId.equals("-"))
                ? catEstadistico1Repository.findByUniqueId(estadistica1UniqueId)
                        .map(e -> e.getNombre())
                        .orElse(null)
                : null;

        String estadistica2Nombre = (estadistica2UniqueId != null && !estadistica2UniqueId.equals("-"))
                ? catEstadistico2Repository.findByUniqueId(estadistica2UniqueId)
                        .map(e -> e.getNombre())
                        .orElse(null)
                : null;

        return mdea_traduccion_dto.builder()
                .idUnique(mdea.getIdUnique())
                .idA(mdea.getIdA())
                .idS(mdea.getIdS())
                .componente(mdea.getComponente())
                .componenteNombre(componenteNombre)
                .subcomponente(mdea.getSubcomponente())
                .subcomponenteNombre(subcomponenteNombre)
                .tema(mdea.getTema())
                .temaNombre(temaNombre)
                .estadistica1(mdea.getEstadistica1())
                .estadistica1Nombre(estadistica1Nombre)
                .estadistica2(mdea.getEstadistica2())
                .estadistica2Nombre(estadistica2Nombre)
                .contribucion(mdea.getContribucion())
                .comentarioS(mdea.getComentarioS())
                .build();
    }


    private ods_traduccion_dto traducirOds(ods_enty ods) {
        Integer objetivoId = parseInteger(ods.getObjetivo());
        String metaUniqueId = normalizarTexto(ods.getMeta());
        String indicadorUniqueId = normalizarTexto(ods.getIndicador());

        String objetivoNombre = objetivoId != null
                ? catObjetivoRepository.findByIdObjetivo(objetivoId)
                        .map(o -> o.getObjetivo())
                        .orElse(null)
                : null;

        String metaNombre = (metaUniqueId != null && !metaUniqueId.equals("-"))
                ? catMetaRepository.findByUniqueId(metaUniqueId)
                        .map(m -> m.getMeta())
                        .orElse(null)
                : null;

        String indicadorNombre = (indicadorUniqueId != null && !indicadorUniqueId.equals("-"))
                ? catIndicadorRepository.findByUniqueId(indicadorUniqueId)
                        .map(i -> i.getIndicador())
                        .orElse(null)
                : null;

        return ods_traduccion_dto.builder()
                .idUnique(ods.getIdUnique())
                .idA(ods.getIdA())
                .idS(ods.getIdS())
                .objetivo(ods.getObjetivo())
                .objetivoNombre(objetivoNombre)
                .meta(ods.getMeta())
                .metaNombre(metaNombre)
                .indicador(ods.getIndicador())
                .indicadorNombre(indicadorNombre)
                .contribucion(ods.getContribucion())
                .comentarioS(ods.getComentarioS())
                .build();
    }



    public List<variable_revision_prioridad_dto> getVariablesByFuentes(List<String> idFuentes) {
    if (idFuentes == null || idFuentes.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar al menos un id_fuente");
    }

    List<variables_enty> variables = repository.findByIdFuenteInOrderByIdFuenteDescIdAAsc(idFuentes);

    return variables.stream().map(var -> {
        return variable_revision_prioridad_dto.builder()
                .idA(var.getIdA())
                .idS(var.getIdS())
                .idFuente(var.getIdFuente())
                .acronimo(var.getAcronimo())
                .nombre(var.getNombre())
                .url(var.getUrl())
                .definicion(var.getDefinicion())
                .prioridad(var.getPrioridad())
                .revisada(var.getRevisada())
                .fechaRevision(var.getFechaRevision())
                .responsableRevision(var.getResponsableRevision())
                .mdea(var.getMdea())
                .ods(var.getOds())
                .mdeas(
                    mdeaRepository.findByIdA(var.getIdA())
                        .stream()
                        .map(this::traducirMdea)
                        .collect(Collectors.toList())
                )
                .odsList(
                    odsRepository.findByIdA(var.getIdA())
                        .stream()
                        .map(this::traducirOds)
                        .collect(Collectors.toList())
                )
                .pertinencia(pertinenciaRepository.findByIdA(var.getIdA()).orElse(null))
                .build();
    }).collect(Collectors.toList());
}

private String normalizarTexto(String value) {
    if (value == null)
        return null;
    String limpio = value.trim();
    return limpio.isBlank() ? null : limpio;
}

    @Transactional
public Map<String, Object> actualizarRevisionPrioridad(String idA, variable_revision_update_dto dto) {
    variables_enty variable = repository.findById(idA)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variable no encontrada"));

    variable.setPrioridad(dto.getPrioridad());
    variable.setRevisada(dto.getRevisada());
    variable.setResponsableRevision(dto.getResponsableRevision());
    variable.setFechaRevision(java.time.LocalDateTime.now());

    repository.save(variable);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Revisión de prioridad actualizada correctamente");
    response.put("idA", variable.getIdA());
    response.put("prioridad", variable.getPrioridad());
    response.put("revisada", variable.getRevisada());

    return response;
}
    @Transactional
public Map<String, Object> actualizarRevisionPrioridadMasiva(variable_revision_masiva_update_dto dto) {
    if (dto.getIdsA() == null || dto.getIdsA().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar al menos un idA");
    }

    if (dto.getPrioridad() == null || (dto.getPrioridad() != 1 && dto.getPrioridad() != 2)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La prioridad debe ser 1 o 2");
    }

    if (dto.getResponsableRevision() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar responsableRevision");
    }

    List<variables_enty> variables = repository.findAllById(dto.getIdsA());

    if (variables.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron variables para actualizar");
    }

    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();

    for (variables_enty variable : variables) {
        variable.setPrioridad(dto.getPrioridad());
        variable.setRevisada(Boolean.TRUE.equals(dto.getRevisada()));
        variable.setResponsableRevision(dto.getResponsableRevision());
        variable.setFechaRevision(ahora);
    }

    repository.saveAll(variables);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Revisión de prioridad masiva actualizada correctamente");
    response.put("totalActualizadas", variables.size());
    response.put("prioridad", dto.getPrioridad());
    response.put("revisada", dto.getRevisada());

    return response;
}
    

}


