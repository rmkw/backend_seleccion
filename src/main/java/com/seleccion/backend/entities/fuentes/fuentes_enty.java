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
    @Column(name = "id_fuente_seleccion", nullable = false)
    private String idFuenteSeleccion;

    @Column(name = "id_fuente", insertable = false, updatable = false)
    private String idFuente;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "fuente", nullable = false)
    private String fuente;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "edicion", nullable = false)
    private String edicion;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "comentario_a")
    private String comentarioA;
}