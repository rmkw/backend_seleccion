package com.seleccion.backend.services.pertinencias;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class pertinencia_services {
    @Autowired
    private pertinencia_repo repository;

    public pertinencia_enty guardar(pertinencia_enty pertinencia_enty) {
        return repository.save(pertinencia_enty);
    }

    public Optional<pertinencia_enty> buscarPorIdA(String IdA) {
        return repository.findByIdA(IdA);
    }

    @Transactional
    public Map<String, Object> editarPertinencia(String idA, pertinencia_enty dto) {
        pertinencia_enty existente = repository.findByIdA(idA)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pertinencia no encontrada"));

        existente.setPertinencia(dto.getPertinencia());
        existente.setContribucion(dto.getContribucion());
        existente.setViabilidad(dto.getViabilidad());
        existente.setPropuesta(dto.getPropuesta());
        existente.setComentarioS(dto.getComentarioS());

        repository.save(existente);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pertinencia actualizada correctamente");
        return response;
    }

}
