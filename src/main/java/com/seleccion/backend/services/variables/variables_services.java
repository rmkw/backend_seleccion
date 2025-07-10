package com.seleccion.backend.services.variables;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.entities.variables.variables_relacion_dto;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

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
        dto.setPertinencia(pertinenciaRepository.findByIdA(var.getIdA()).map(List::of).orElse(List.of()));

        return dto;
    }).collect(Collectors.toList());
}

    

    

}


