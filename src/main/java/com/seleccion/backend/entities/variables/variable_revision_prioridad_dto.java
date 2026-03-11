package com.seleccion.backend.entities.variables;

import java.time.LocalDateTime;
import java.util.List;

import com.seleccion.backend.entities.mdea.produccion.mdea_traduccion_dto;
import com.seleccion.backend.entities.ods.produccion.ods_traduccion_dto;
import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variable_revision_prioridad_dto {
    private String idA;
    private String idS;
    private String idFuente;
    private String acronimo;
    private String nombre;
    private String url;

    private Short prioridad;
    private Boolean revisada;
    private LocalDateTime fechaRevision;
    private Integer responsableRevision;

    private Boolean mdea;
    private Boolean ods;

    private List<mdea_traduccion_dto> mdeas;
    private List<ods_traduccion_dto> odsList;
    private pertinencia_enty pertinencia;
}
