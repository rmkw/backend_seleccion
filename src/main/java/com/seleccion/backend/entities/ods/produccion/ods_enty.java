package com.seleccion.backend.entities.ods.produccion;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ods", schema = "seleccion_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ods_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unique")
    private Integer idUnique;

    @Column(name = "id_a", nullable = false)
    private String idA;

    @Column(name = "id_s", nullable = false)
    private String idS;

    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "meta")
    private String meta;

    @Column(name = "indicador")
    private String indicador;

    @Column(name = "contribucion")
    private String contribucion;

    @Column(name = "comentario_s")
    private String comentarioS;
}
