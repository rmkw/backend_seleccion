package com.seleccion.backend.entities.fuentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class fuentes_dto {
    private String idFuente;
    private String acronimo;
    private String fuente;
    private String url;
    private String edicion;
    private String comentarioS;
    private Integer responsableRegister;
    private Integer responsableActualizacion;
    private Long totalVariables;
}
