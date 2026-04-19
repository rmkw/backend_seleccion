package com.seleccion.backend.services.variables;

import com.seleccion.backend.repositories.variables.variables_repo;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.seleccion.backend.entities.variables.armo.variables_armo_dto;
import com.seleccion.backend.entities.variables.armo.variables_armo_enty;
import com.seleccion.backend.repositories.variables.variables_armo_repo;




@Service
public class variables_armo_service_impl implements variables_armo_service {

    private final variables_repo variables_repo;
    private final variables_armo_repo variablesArmoRepo;

    public variables_armo_service_impl(variables_armo_repo variablesArmoRepo, variables_repo variables_repo) {
        this.variablesArmoRepo = variablesArmoRepo;
        this.variables_repo = variables_repo;
    }

    @Override
    public Optional<variables_armo_dto> obtenerPorIdA(String idA) {
        return variablesArmoRepo.findById(idA)
                .map(this::convertirA_DTO);
    }

    @Override
    public boolean existePorIdA(String idA) {
        return variablesArmoRepo.existsById(idA);
    }

    @Override
    public variables_armo_dto guardarVariable(variables_armo_dto dto) {
        variables_armo_enty entity = convertirA_Entity(dto);
        variables_armo_enty guardada = variablesArmoRepo.save(entity);
        return convertirA_DTO(guardada);
    }

    @Override
    public variables_armo_dto actualizarVariable(String idA, variables_armo_dto dto) {
        variables_armo_enty existente = variablesArmoRepo.findById(idA)
                .orElseThrow(() -> new RuntimeException("La variable no existe en armonización con id_a: " + idA));

        existente.setIdFuente(dto.getIdFuente());
        existente.setAcronimo(dto.getAcronimo());
        existente.setIdS(dto.getIdS());
        existente.setVariableS(dto.getVariableS());
        existente.setVariableA(dto.getVariableA());
        existente.setUrl(dto.getUrl());
        existente.setPregunta(dto.getPregunta());
        existente.setDefinicion(dto.getDefinicion());
        existente.setUniverso(dto.getUniverso());
        existente.setAnioReferencia(dto.getAnioReferencia());
        existente.setTematica(dto.getTematica());
        existente.setTema1(dto.getTema1());
        existente.setSubtema1(dto.getSubtema1());
        existente.setTema2(dto.getTema2());
        existente.setSubtema2(dto.getSubtema2());
        existente.setTabulados(dto.getTabulados());
        existente.setClasificacion(dto.getClasificacion());
        existente.setMicrodatos(dto.getMicrodatos());
        existente.setDatosabiertos(dto.getDatosabiertos());
        existente.setMdea(dto.getMdea());
        existente.setOds(dto.getOds());
        existente.setComentarioS(dto.getComentarioS());
        existente.setComentarioA(dto.getComentarioA());

        variables_armo_enty actualizada = variablesArmoRepo.save(existente);
        return convertirA_DTO(actualizada);
    }

    @Override
    public void eliminarVariable(String idA) {
        if (!variablesArmoRepo.existsById(idA)) {
            throw new RuntimeException("No existe la variable con id_a: " + idA);
        }
        variablesArmoRepo.deleteById(idA);
    }

    private variables_armo_dto convertirA_DTO(variables_armo_enty entity) {
        return new variables_armo_dto(
                entity.getIdA(),
                entity.getIdFuente(),
                entity.getAcronimo(),
                entity.getIdS(),
                entity.getVariableS(),
                entity.getVariableA(),
                entity.getUrl(),
                entity.getPregunta(),
                entity.getDefinicion(),
                entity.getUniverso(),
                entity.getAnioReferencia(),
                entity.getTematica(),
                entity.getTema1(),
                entity.getSubtema1(),
                entity.getTema2(),
                entity.getSubtema2(),
                entity.getTabulados(),
                entity.getClasificacion(),
                entity.getMicrodatos(),
                entity.getDatosabiertos(),
                entity.getMdea(),
                entity.getOds(),
                entity.getComentarioS(),
                entity.getComentarioA());
    }

    private variables_armo_enty convertirA_Entity(variables_armo_dto dto) {
        return new variables_armo_enty(
                dto.getIdA(),
                dto.getIdFuente(),
                dto.getAcronimo(),
                dto.getIdS(),
                dto.getVariableS(),
                dto.getVariableA(),
                dto.getUrl(),
                dto.getPregunta(),
                dto.getDefinicion(),
                dto.getUniverso(),
                dto.getAnioReferencia(),
                dto.getTematica(),
                dto.getTema1(),
                dto.getSubtema1(),
                dto.getTema2(),
                dto.getSubtema2(),
                dto.getTabulados(),
                dto.getClasificacion(),
                dto.getMicrodatos(),
                dto.getDatosabiertos(),
                dto.getMdea(),
                dto.getOds(),
                dto.getComentarioS(),
                dto.getComentarioA());
    }
    
    @Override
    public Long contarVariablesArmonizadas() {
        return variablesArmoRepo.count();
    }
}