package com.seleccion.backend.services.fuentes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;

import jakarta.transaction.Transactional;
@Service
public class fuentes_services {
    @Autowired
    private fuentes_repo repo;


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
    

   


}
