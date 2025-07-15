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
import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.entities.variables.variables_relacion_dto;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;
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

    

    public variables_enty crearVariable(variables_enty variable) {
        
        if (repository.existsByIdSAndIdFuente(variable.getIdS(), variable.getIdFuente())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una variable con ese ID_S y esa fuente");
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

    public List<variables_enty> getByResponsableAndFuente(Integer responsableRegister, Integer idFuente) {
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



    

    

}


