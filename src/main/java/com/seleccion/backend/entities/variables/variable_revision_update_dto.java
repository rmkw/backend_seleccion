package com.seleccion.backend.entities.variables;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variable_revision_update_dto {
    
    private Short prioridad;
    private Boolean revisada;
    private Integer responsableRevision;
}
