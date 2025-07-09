package com.seleccion.backend.services.pertinencias;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;

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
}
