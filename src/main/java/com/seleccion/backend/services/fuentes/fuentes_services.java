package com.seleccion.backend.services.fuentes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return repo.save(fuente);
    }

    public List<fuentes_enty> getByAcronimoAndResponsable(String acronimo, Integer responsableRegister) {
        return repo.findByAcronimoAndResponsableRegisterOrderByEdicionDesc(acronimo, responsableRegister);
    }

    @Transactional
    public Map<String, Object> deleteFuenteById(Integer idFuente) {
        Optional<fuentes_enty> optional = repo.findById(idFuente);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        repo.deleteById(idFuente);

        return Map.of(
                "message", "Fuente eliminada correctamente",
                "id", idFuente);
    }

    public fuentes_enty update(Integer id, fuentes_enty nuevaFuente) {
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
    public Map<String, Object> deleteFuenteAndCascade(Integer idFuente) {
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
