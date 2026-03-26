package com.seleccion.backend.entities.variables.armo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class variables_armo_dto {
    private String idA;
    private String idFuente;
    private String acronimo;
    private String idS;
    private String variableS;
    private String variableA;
    private String url;
    private String pregunta;
    private String definicion;
    private String universo;
    private Integer anioReferencia;
    private String tematica;
    private String tema1;
    private String subtema1;
    private String tema2;
    private String subtema2;
    private Boolean tabulados;
    private Boolean clasificacion;
    private String microdatos;
    private Boolean datosabiertos;
    private Boolean mdea;
    private Boolean ods;
    private String comentarioS;
    private String comentarioA;
}
