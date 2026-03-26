package com.seleccion.backend.entities.variables.armo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variables", schema = "armonizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class variables_armo_enty {
    @Id
    @Column(name = "id_a")
    private String idA;

    @Column(name = "id_fuente", nullable = false)
    private String idFuente;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "id_s")
    private String idS;

    @Column(name = "variable_s")
    private String variableS;

    @Column(name = "variable_a")
    private String variableA;

    @Column(name = "url")
    private String url;

    @Column(name = "pregunta")
    private String pregunta;

    @Column(name = "definicion")
    private String definicion;

    @Column(name = "universo")
    private String universo;

    @Column(name = "anio_referencia")
    private Integer anioReferencia;

    @Column(name = "tematica")
    private String tematica;

    @Column(name = "tema1")
    private String tema1;

    @Column(name = "subtema1")
    private String subtema1;

    @Column(name = "tema2")
    private String tema2;

    @Column(name = "subtema2")
    private String subtema2;

    @Column(name = "tabulados")
    private Boolean tabulados;

    @Column(name = "clasificacion")
    private Boolean clasificacion;

    @Column(name = "microdatos")
    private String microdatos;

    @Column(name = "datosabiertos")
    private Boolean datosabiertos;

    @Column(name = "mdea")
    private Boolean mdea;

    @Column(name = "ods")
    private Boolean ods;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "comentario_a")
    private String comentarioA;
}
