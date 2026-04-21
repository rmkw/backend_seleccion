package com.seleccion.backend.services.fuentes;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seleccion.backend.entities.fuentes.fuentes_dto;
import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;
import com.seleccion.backend.repositories.variables.variables_repo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class fuentes_services {

    private final fuentes_repo repo;
    private final variables_repo variableRepo;

    private String construirIdFuente(String acronimo, String fuente, String edicion, String url) {
        String edicionSafe = edicion != null ? edicion : "";
        String urlSafe = url != null ? url : "";
        return acronimo + "-" + fuente + "-" + edicionSafe + "-" + urlSafe;
    }

    public List<fuentes_dto> getByAcronimo(String acronimo) {
        if (acronimo == null || acronimo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        List<fuentes_enty> fuentes = repo.findByAcronimoOrderByIdFuenteDesc(acronimo.trim());

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
                    totalVariables);
        }).toList();
    }

    public fuentes_enty getByIdFuente(String idFuente) {
        return repo.findById(idFuente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fuente no encontrada"));
    }

    @Transactional
    public fuentes_enty create(fuentes_enty fuente) {
        if (fuente.getAcronimo() == null || fuente.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        if (fuente.getFuente() == null || fuente.getFuente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fuente es obligatoria");
        }

        String acronimo = fuente.getAcronimo().trim();
        String nombreFuente = fuente.getFuente().trim();
        String url = fuente.getUrl() != null ? fuente.getUrl().trim() : null;
        String edicion = fuente.getEdicion() != null ? fuente.getEdicion().trim() : null;
        String comentarioS = fuente.getComentarioS();
        String comentarioA = fuente.getComentarioA();
        Integer responsableRegister = fuente.getResponsableRegister();
        Integer responsableActualizacion = fuente.getResponsableActualizacion();
        String idFuenteSeleccion = fuente.getIdFuenteSeleccion();

        String nuevoId = construirIdFuente(acronimo, nombreFuente, edicion, url);

        if (repo.existsById(nuevoId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una fuente con esos datos");
        }

        repo.insertarFuente(
                acronimo,
                nombreFuente,
                url,
                edicion,
                comentarioS,
                comentarioA,
                responsableRegister,
                responsableActualizacion,
                idFuenteSeleccion);

        return repo.findById(nuevoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La fuente se insertó pero no se pudo recuperar"));
    }

    @Transactional
    public fuentes_enty update(String idFuenteActual, fuentes_enty nuevaFuente) {
        if (!repo.existsById(idFuenteActual)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        if (nuevaFuente.getAcronimo() == null || nuevaFuente.getAcronimo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acrónimo es obligatorio");
        }

        if (nuevaFuente.getFuente() == null || nuevaFuente.getFuente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fuente es obligatoria");
        }

        String acronimo = nuevaFuente.getAcronimo().trim();
        String nombreFuente = nuevaFuente.getFuente().trim();
        String url = nuevaFuente.getUrl() != null ? nuevaFuente.getUrl().trim() : null;
        String edicion = nuevaFuente.getEdicion() != null ? nuevaFuente.getEdicion().trim() : null;
        String comentarioS = nuevaFuente.getComentarioS();
        String comentarioA = nuevaFuente.getComentarioA();
        Integer responsableActualizacion = nuevaFuente.getResponsableActualizacion();
        String idFuenteSeleccion = nuevaFuente.getIdFuenteSeleccion();

        repo.actualizarFuente(
                idFuenteActual,
                acronimo,
                nombreFuente,
                url,
                edicion,
                comentarioS,
                comentarioA,
                responsableActualizacion,
                idFuenteSeleccion);

        String nuevoId = construirIdFuente(acronimo, nombreFuente, edicion, url);

        return repo.findById(nuevoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La fuente se actualizó pero no se pudo recuperar"));
    }

    @Transactional
    public void deleteById(String idFuente) {
        if (!repo.existsById(idFuente)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuente no encontrada");
        }

        repo.deleteById(idFuente);
    }

    public Long contarFuentes() {
        return repo.count();
    }
}