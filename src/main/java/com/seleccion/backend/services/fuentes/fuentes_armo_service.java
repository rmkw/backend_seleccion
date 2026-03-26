package com.seleccion.backend.services.fuentes;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.fuentes.fuente_save_dto;
import com.seleccion.backend.entities.fuentes.fuentes_armo_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_armo_repository;
import com.seleccion.backend.repositories.procesos.procesos_repo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class fuentes_armo_service {
    private final fuentes_armo_repository repository;
    private final procesos_repo procesosRepo;

    public fuentes_armo_enty createFuente(fuente_save_dto dto) {
        validarDto(dto);

        String acronimo = dto.getAcronimo().trim();
        validarAcronimoExisteEnProcesos(acronimo);
        String fuente = dto.getFuente().trim();
        String url = normalizarTexto(dto.getUrl());
        String edicion = normalizarTexto(dto.getEdicion());
        String comentarioS = normalizarTexto(dto.getComentarioS());
        String comentarioA = normalizarTexto(dto.getComentarioA());
        String idFuenteSeleccion = normalizarTexto(dto.getIdFuenteSeleccion());

        String idFuente = construirIdFuente(acronimo, fuente, edicion, url);

        if (idFuenteSeleccion == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "idFuenteSeleccion es obligatorio");
        }

        if (repository.existsByIdFuenteSeleccion(idFuenteSeleccion)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La fuente de selección ya está vinculada en armonización");
        }

        if (repository.existsByIdFuente(idFuente)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La fuente ya existe en armonización");
        }

        repository.insertFuente(
                acronimo,
                fuente,
                url,
                edicion,
                comentarioS,
                comentarioA,
                idFuenteSeleccion);

        return repository.findByIdFuenteSeleccion(idFuenteSeleccion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La fuente se insertó pero no pudo recuperarse"));
    }

    public Optional<fuentes_armo_enty> getFuenteById(String idFuente) {
        return repository.findByIdFuente(idFuente);
    }

    

    public boolean existsFuenteById(String idFuente) {
        return repository.existsByIdFuente(idFuente);
    }

    public boolean existsFuenteByIdFuenteSeleccion(String idFuenteSeleccion) {
        return repository.existsByIdFuenteSeleccion(idFuenteSeleccion);
    }

    private void validarDto(fuente_save_dto dto) {
        if (dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe proporcionar los datos de la fuente");
        }

        if (dto.getAcronimo() == null || dto.getAcronimo().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El acrónimo es obligatorio");
        }

        if (dto.getFuente() == null || dto.getFuente().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fuente es obligatoria");
        }
    }

    private String construirIdFuente(String acronimo, String fuente, String edicion, String url) {
        return String.format(
                "%s-%s-%s-%s",
                limpiarSegmento(acronimo),
                limpiarSegmento(fuente),
                limpiarSegmento(edicion),
                limpiarSegmento(url));
    }

    private String limpiarSegmento(String valor) {
        if (valor == null)
            return "";
        return valor.trim();
    }

    private String normalizarTexto(String valor) {
        if (valor == null)
            return null;
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    public fuentes_armo_enty updateFuente(fuente_save_dto dto) {
        validarDto(dto);

        String acronimo = dto.getAcronimo().trim();
        validarAcronimoExisteEnProcesos(acronimo);
        
        String fuente = dto.getFuente().trim();
        String url = normalizarTexto(dto.getUrl());
        String edicion = normalizarTexto(dto.getEdicion());
        String comentarioS = normalizarTexto(dto.getComentarioS());
        String comentarioA = normalizarTexto(dto.getComentarioA());
        String idFuenteSeleccion = normalizarTexto(dto.getIdFuenteSeleccion());

        if (idFuenteSeleccion == null || idFuenteSeleccion.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "idFuenteSeleccion es obligatorio para actualizar");
        }

        int updated = repository.updateFuenteByIdFuenteSeleccion(
                acronimo,
                fuente,
                url,
                edicion,
                comentarioS,
                comentarioA,
                idFuenteSeleccion);

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Fuente no encontrada en armonización");
        }

        return repository.findByIdFuenteSeleccion(idFuenteSeleccion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La fuente se actualizó pero no pudo recuperarse"));
    }
    public String construirIdFuentePublic(fuente_save_dto dto) {
        validarDto(dto);

        return construirIdFuente(
                dto.getAcronimo(),
                dto.getFuente(),
                dto.getEdicion(),
                dto.getUrl());
    }

    public boolean existsFuenteByData(fuente_save_dto dto) {
        String idFuenteSeleccion = normalizarTexto(dto.getIdFuenteSeleccion());

        if (idFuenteSeleccion != null) {
            return repository.existsByIdFuenteSeleccion(idFuenteSeleccion);
        }

        String idFuente = construirIdFuentePublic(dto);
        return repository.existsByIdFuente(idFuente);
    }

    public Optional<fuentes_armo_enty> getFuenteByData(fuente_save_dto dto) {
        String idFuenteSeleccion = normalizarTexto(dto.getIdFuenteSeleccion());

        if (idFuenteSeleccion != null) {
            Optional<fuentes_armo_enty> fuente = repository.findByIdFuenteSeleccion(idFuenteSeleccion);
            if (fuente.isPresent()) {
                return fuente;
            }
        }

        String idFuente = construirIdFuentePublic(dto);
        return repository.findByIdFuente(idFuente);
    }

    public Optional<fuentes_armo_enty> getFuenteByIdFuenteSeleccion(String idFuenteSeleccion) {
        return repository.findByIdFuenteSeleccion(idFuenteSeleccion);
    }

    private void validarAcronimoExisteEnProcesos(String acronimo) {
        if (acronimo == null || acronimo.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El acrónimo es obligatorio");
        }

        String acronimoLimpio = acronimo.trim();

        if (!procesosRepo.existsById(acronimoLimpio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El acrónimo '" + acronimoLimpio + "' no existe en la tabla de procesos de producción");
        }
    }
}