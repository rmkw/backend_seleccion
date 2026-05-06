package com.seleccion.backend.entities.fuentes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class fuentes_dto {

    private String idFuenteSeleccion;
    private String idFuente;

    private String acronimo;
    private String fuente;
    private String url;
    private String edicion;

    private String comentarioS;
    private String comentarioA;

    private Long totalVariables;
}