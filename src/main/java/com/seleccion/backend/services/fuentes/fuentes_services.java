package com.seleccion.backend.services.fuentes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.fuentes.fuentes_dto;
import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

import jakarta.transaction.Transactional;
@Service
public class fuentes_services {
    @Autowired
    private fuentes_repo repo;


    @Autowired
    private variables_repo variableRepo;

    @Autowired
    private pertinencia_repo pertinenciaRepo;

    @Autowired
    private mdea_repo mdeaRepo;

    @Autowired
    private ods_repo odsRepo;


    public fuentes_enty create(fuentes_enty fuente) {
        if (repo.existsById(fuente.getIdFuente())) {
            throw new IllegalArgumentException("Ya existe una fuente con ese ID");
        }

        return repo.save(fuente);
    }    

    public List<fuentes_dto> getByAcronimoAndResponsable(String acronimo, Integer responsableRegister) {
    List<fuentes_enty> fuentes = repo.findByAcronimoAndResponsableRegisterOrderByEdicionDesc(acronimo, responsableRegister);

    return fuentes.stream().map(f -> {
        Long totalVariables = variableRepo.countByIdFuente(f.getIdFuente());

        return new fuentes_dto(
                f.getIdFuente(),
                f.getAcronimo(),
                f.getFuente(),
                f.getUrl(),
                f.getEdicion(),
                f.getComentarioS(),
                f.getResponsableRegister(),
                f.getResponsableActualizacion(),
                totalVariables
        );
    }).toList();
}


    @Transactional
    public Map<String, Object> deleteFuenteById(String idFuente) {
        Optional<fuentes_enty> optional = repo.findById(idFuente);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        repo.deleteById(idFuente);

        return Map.of(
                "message", "Fuente eliminada correctamente",
                "id", idFuente);
    }

    public fuentes_enty update(String id, fuentes_enty nuevaFuente) {
        Optional<fuentes_enty> optional = repo.findById(id);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        fuentes_enty existente = optional.get();

        existente.setFuente(nuevaFuente.getFuente());
        existente.setUrl(nuevaFuente.getUrl());
        existente.setEdicion(nuevaFuente.getEdicion());
        existente.setComentarioS(nuevaFuente.getComentarioS());
        existente.setResponsableActualizacion(nuevaFuente.getResponsableActualizacion());
        existente.setAcronimo(nuevaFuente.getAcronimo());
        existente.setResponsableRegister(nuevaFuente.getResponsableRegister());

        return repo.save(existente);
    }

    @Transactional
    public Map<String, Object> deleteFuenteAndCascade(String idFuente) {
        Optional<fuentes_enty> optionalRecord = repo.findById(idFuente);

        if (optionalRecord.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        // Obtener las variables asociadas a esta fuente
        List<variables_enty> variables = variableRepo.findByIdFuente(idFuente);

        for (variables_enty var : variables) {
            String idA = var.getIdA();

            // Eliminar relaciones por idA
            pertinenciaRepo.deleteByIdA(idA);
            odsRepo.deleteByIdA(idA);
            mdeaRepo.deleteByIdA(idA);

            // Eliminar variable
            variableRepo.deleteById(idA);
        }

        // Eliminar la fuente
        repo.deleteById(idFuente);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fuente y datos relacionados eliminados correctamente");
        response.put("variablesEliminadas", variables.size());
        return response;
    }

    

   


}
