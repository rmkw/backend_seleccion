package com.seleccion.backend.services.fuentes;

import java.util.List;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class fuentes_services {

    private final fuentes_repo repo;
    private final variables_repo variableRepo;
    private final mdea_repo mdeaRepo;
    private final ods_repo odsRepo;
    private final pertinencia_repo pertinenciaRepo;

    public List<fuentes_dto> getByAcronimo(String acronimo) {
        if (acronimo == null || acronimo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        List<fuentes_enty> fuentes = repo.findByAcronimoOrderByIdFuenteSeleccionDesc(acronimo.trim());

        return fuentes.stream().map(f -> {
            Long totalVariables = variableRepo.countByIdFuente(f.getIdFuenteSeleccion());

            return fuentes_dto.builder()
                    .idFuenteSeleccion(f.getIdFuenteSeleccion())
                    .idFuente(f.getIdFuente())
                    .acronimo(f.getAcronimo())
                    .fuente(f.getFuente())
                    .url(f.getUrl())
                    .edicion(f.getEdicion())
                    .comentarioS(f.getComentarioS())
                    .comentarioA(f.getComentarioA())
                    .totalVariables(totalVariables)
                    .build();
        }).toList();
    }

    public fuentes_enty getByIdFuenteSeleccion(String idFuenteSeleccion) {
        if (idFuenteSeleccion == null || idFuenteSeleccion.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idFuenteSeleccion es obligatorio");
        }

        return repo.findById(idFuenteSeleccion.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fuente no encontrada"));
    }

    @Transactional
    public fuentes_enty create(fuentes_enty fuente) {
        validarFuenteParaCrear(fuente);

        String idFuenteSeleccion = fuente.getIdFuenteSeleccion().trim();

        if (repo.existsById(idFuenteSeleccion)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una fuente con ese idFuenteSeleccion");
        }

        fuentes_enty nueva = fuentes_enty.builder()
                .idFuenteSeleccion(idFuenteSeleccion)
                .acronimo(fuente.getAcronimo().trim())
                .fuente(fuente.getFuente().trim())
                .url(limpiarObligatorio(fuente.getUrl()))
                .edicion(limpiarObligatorio(fuente.getEdicion()))
                .comentarioS(limpiarNullable(fuente.getComentarioS()))
                .comentarioA(limpiarNullable(fuente.getComentarioA()))
                .build();

        return repo.save(nueva);
    }

    @Transactional
    public fuentes_enty update(String idFuenteSeleccionActual, fuentes_enty nuevaFuente) {
        if (idFuenteSeleccionActual == null || idFuenteSeleccionActual.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idFuenteSeleccionActual es obligatorio");
        }

        fuentes_enty existente = repo.findById(idFuenteSeleccionActual.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fuente no encontrada"));

        validarFuenteParaActualizar(nuevaFuente);

        /*
         * IMPORTANTE:
         * No se modifica idFuenteSeleccion.
         * Esa es la PK estable.
         *
         * idFuente se recalcula automáticamente en PostgreSQL porque es GENERATED
         * ALWAYS
         * usando acronimo, fuente, edicion y url.
         */

        existente.setAcronimo(nuevaFuente.getAcronimo().trim());
        existente.setFuente(nuevaFuente.getFuente().trim());
        existente.setUrl(limpiarObligatorio(nuevaFuente.getUrl()));
        existente.setEdicion(limpiarObligatorio(nuevaFuente.getEdicion()));
        existente.setComentarioS(limpiarNullable(nuevaFuente.getComentarioS()));
        existente.setComentarioA(limpiarNullable(nuevaFuente.getComentarioA()));

        return repo.save(existente);
    }

    @Transactional
public void deleteByIdFuenteSeleccion(String idFuenteSeleccion) {
    if (idFuenteSeleccion == null || idFuenteSeleccion.isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idFuenteSeleccion es obligatorio");
    }

    String id = idFuenteSeleccion.trim();

    fuentes_enty fuente = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Fuente no encontrada"));

    List<variables_enty> variables = variableRepo.findByIdFuente(id);

    for (variables_enty variable : variables) {
        String idA = variable.getIdA();

        pertinenciaRepo.deleteByIdA(idA);
        odsRepo.deleteByIdA(idA);
        mdeaRepo.deleteByIdA(idA);
    }

    variableRepo.deleteAll(variables);

    repo.delete(fuente);
}

    public Long contarFuentes() {
        return repo.count();
    }

    private void validarFuenteParaCrear(fuentes_enty fuente) {
        if (fuente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fuente es obligatoria");
        }

        if (fuente.getIdFuenteSeleccion() == null || fuente.getIdFuenteSeleccion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idFuenteSeleccion es obligatorio");
        }

        validarCamposBase(fuente);
    }

    private void validarFuenteParaActualizar(fuentes_enty fuente) {
        if (fuente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fuente es obligatoria");
        }

        validarCamposBase(fuente);
    }

    private void validarCamposBase(fuentes_enty fuente) {
        if (fuente.getAcronimo() == null || fuente.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        if (fuente.getFuente() == null || fuente.getFuente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fuente es obligatoria");
        }

        if (fuente.getUrl() == null || fuente.getUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La URL es obligatoria");
        }

        if (fuente.getEdicion() == null || fuente.getEdicion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La edición es obligatoria");
        }
    }

    private String limpiarObligatorio(String value) {
        return value == null ? "" : value.trim();
    }

    private String limpiarNullable(String value) {
        if (value == null) {
            return null;
        }

        String limpio = value.trim();
        return limpio.isBlank() ? null : limpio;
    }
}