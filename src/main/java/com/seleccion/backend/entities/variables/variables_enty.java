package com.seleccion.backend.entities.variables;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "variables", schema = "seleccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variables_enty {

    @Id
    @Column(name = "id_a", nullable = false)
    private String idA;

    @Column(name = "id_s", nullable = false)
    private String idS;

    @Column(name = "id_fuente", nullable = false)
    private String idFuente;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "definicion", nullable = false)
    private String definicion;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "comentario_s", nullable = false)
    private String comentarioS;

    @Column(name = "mdea", nullable = false)
    private Boolean mdea;

    @Column(name = "ods", nullable = false)
    private Boolean ods;

    @Column(name = "prioridad")
    private Integer prioridad;

    @Column(name = "revisada", nullable = false)
    private Boolean revisada;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(name = "responsable_revision")
    private Integer responsableRevision;
}