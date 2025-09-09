package com.seleccion.backend.services.ods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;

@Service
public class ods_services {
    @Autowired
    private ods_repo repository;

    public ods_enty save(ods_enty relacion) {
        boolean exists = repository.existsByIdAAndObjetivoAndMetaAndIndicador(
                relacion.getIdA(),
                relacion.getObjetivo(),
                relacion.getMeta(),
                relacion.getIndicador());

        if (exists) {
            throw new IllegalArgumentException("Ya existe esta relación ODS para esta variable.");
        }

        return repository.save(relacion);
    }    

    public List<ods_enty> getByIdA(String idA) {
        return repository.findByIdA(idA);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
