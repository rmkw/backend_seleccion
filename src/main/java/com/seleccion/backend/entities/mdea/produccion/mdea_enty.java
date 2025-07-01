package com.seleccion.backend.entities.mdea.produccion;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mdea", schema = "seleccion_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class mdea_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unique")
    private Integer idUnique;

    @Column(name = "id_a")
    private String idA;

    @Column(name = "id_s", nullable = false)
    private String idS;

    @Column(name = "componente")
    private String componente;

    @Column(name = "subcomponente")
    private String subcomponente;

    @Column(name = "tema")
    private String tema;

    @Column(name = "estadistica1")
    private String estadistica1;

    @Column(name = "estadistica2")
    private String estadistica2;

    @Column(name = "contribucion")
    private String contribucion;

    @Column(name = "comentario_s")
    private String comentarioS;
}
