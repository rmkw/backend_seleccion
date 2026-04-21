package com.seleccion.backend.entities.fuentes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fuentes", schema = "seleccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class fuentes_enty {

    @Id
    @Column(name = "id_fuente", insertable = false, updatable = false)
    private String idFuente;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "fuente", nullable = false)
    private String fuente;

    @Column(name = "url")
    private String url;

    @Column(name = "edicion")
    private String edicion;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "comentario_a")
    private String comentarioA;

    @Column(name = "responsable_register", nullable = false)
    private Integer responsableRegister;

    @Column(name = "responsable_actualizacion")
    private Integer responsableActualizacion;

    @Column(name = "id_fuente_seleccion")
    private String idFuenteSeleccion;
}