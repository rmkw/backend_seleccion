package com.seleccion.backend.services.armonizacion.comentarios_pp;

import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.armonizacion.comentarios_pp.comentarios_pp_armo_dto;
import com.seleccion.backend.entities.armonizacion.comentarios_pp.comentarios_pp_armo_enty;
import com.seleccion.backend.repositories.armonizacion.comentarios_pp.comentarios_pp_armo_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class comentarios_pp_armo_services {
    private final comentarios_pp_armo_repo repo;

    public comentarios_pp_armo_enty obtenerPorAcronimo(String acronimo) {
        if (acronimo == null || acronimo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el acrónimo");
        }

        return repo.findByAcronimo(acronimo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró comentario para el acrónimo: " + acronimo
                ));
    }

    public comentarios_pp_armo_enty guardarComentario(comentarios_pp_armo_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        if (repo.existsById(dto.getAcronimo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un comentario para ese acrónimo. Usa actualizar."
            );
        }

        comentarios_pp_armo_enty newEntity = new comentarios_pp_armo_enty();
        newEntity.setAcronimo(dto.getAcronimo().trim());
        newEntity.setComentarioS(dto.getComentarioS());

        return repo.save(newEntity);
    }

    public comentarios_pp_armo_enty actualizarComentario(comentarios_pp_armo_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        comentarios_pp_armo_enty existing = repo.findByAcronimo(dto.getAcronimo().trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe comentario para actualizar con el acrónimo: " + dto.getAcronimo()
                ));

        existing.setComentarioS(dto.getComentarioS());

        return repo.save(existing);
    }

    public comentarios_pp_armo_enty guardarOActualizar(comentarios_pp_armo_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        Optional<comentarios_pp_armo_enty> existenteOpt = repo.findByAcronimo(dto.getAcronimo().trim());

        comentarios_pp_armo_enty entity = existenteOpt.orElseGet(comentarios_pp_armo_enty::new);
        entity.setAcronimo(dto.getAcronimo().trim());
        entity.setComentarioS(dto.getComentarioS());

        return repo.save(entity);

        
    }
    
}
