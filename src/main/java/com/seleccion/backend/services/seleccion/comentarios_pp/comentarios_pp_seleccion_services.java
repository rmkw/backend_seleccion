package com.seleccion.backend.services.seleccion.comentarios_pp;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.seleccion.comentarios_pp.comentarios_pp_seleccion_dto;
import com.seleccion.backend.entities.seleccion.comentarios_pp.comentarios_pp_seleccion_enty;
import com.seleccion.backend.repositories.seleccion.comentarios_pp.comentarios_pp_seleccion_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class comentarios_pp_seleccion_services {
    private final comentarios_pp_seleccion_repo repo;

    public comentarios_pp_seleccion_enty obtenerPorAcronimo(String acronimo) {
        if (acronimo == null || acronimo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el acrónimo");
        }

        return repo.findByAcronimo(acronimo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró comentario para el acrónimo: " + acronimo
                ));
    }

    public comentarios_pp_seleccion_enty guardarComentario(comentarios_pp_seleccion_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        if (repo.existsById(dto.getAcronimo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un comentario para ese acrónimo. Usa actualizar."
            );
        }

        comentarios_pp_seleccion_enty entity = new comentarios_pp_seleccion_enty();
        entity.setAcronimo(dto.getAcronimo().trim());
        entity.setComentarioS(dto.getComentarioS());

        return repo.save(entity);
    }

    public comentarios_pp_seleccion_enty actualizarComentario(comentarios_pp_seleccion_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        comentarios_pp_seleccion_enty existente = repo.findByAcronimo(dto.getAcronimo().trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe comentario para actualizar con el acrónimo: " + dto.getAcronimo()
                ));

        existente.setComentarioS(dto.getComentarioS());

        return repo.save(existente);
    }

    public comentarios_pp_seleccion_enty guardarOActualizar(comentarios_pp_seleccion_dto dto) {
        if (dto.getAcronimo() == null || dto.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        Optional<comentarios_pp_seleccion_enty> existenteOpt = repo.findByAcronimo(dto.getAcronimo().trim());

        comentarios_pp_seleccion_enty entity = existenteOpt.orElseGet(comentarios_pp_seleccion_enty::new);
        entity.setAcronimo(dto.getAcronimo().trim());
        entity.setComentarioS(dto.getComentarioS());

        return repo.save(entity);
    }
}
