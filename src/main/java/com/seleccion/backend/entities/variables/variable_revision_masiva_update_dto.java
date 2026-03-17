package com.seleccion.backend.entities.variables;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variable_revision_masiva_update_dto {
    private List<String> idsA;
    private Short prioridad;
    private Boolean revisada;
    private Integer responsableRevision;
}
