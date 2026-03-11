package com.seleccion.backend.entities.variables;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variables", schema = "seleccion_variables")
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

    @Column(name = "definicion")
    private String definicion;

    @Column(name = "url")
    private String url;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "mdea")
    private Boolean mdea;

    @Column(name = "ods")
    private Boolean ods;

    @Column(name = "responsable_register", nullable = false)
    private Integer responsableRegister;

    @Column(name = "responsable_actualizacion")
    private Integer responsableActualizacion;

    @Column(name = "prioridad")
    private Short prioridad;

    @Column(name = "revisada", nullable = false)
    private Boolean revisada;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(name = "responsable_revision")
    private Integer responsableRevision;
}
