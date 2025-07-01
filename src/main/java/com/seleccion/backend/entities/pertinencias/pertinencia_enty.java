package com.seleccion.backend.entities.pertinencias;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pertinencia", schema = "seleccion_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class pertinencia_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unique")
    private Integer idUnique;

    @Column(name = "id_a", nullable = false)
    private String idA;

    @Column(name = "pertinencia", nullable = false)
    private String pertinencia;

    @Column(name = "contribucion")
    private String contribucion;

    @Column(name = "viabilidad")
    private String viabilidad;

    @Column(name = "propuesta")
    private String propuesta;

    @Column(name = "comentario_s")
    private String comentarioS;
}
