package com.seleccion.backend.entities.mdea.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mdea_temas", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_tema_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id")
    private Integer uniqueId;

    @Column(name = "id_componente", nullable = false)
    private Integer idComponente;

    @Column(name = "id_subcomponente", nullable = false)
    private Integer idSubcomponente;

    @Column(name = "id_tema", nullable = false)
    private Integer idTema;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "definicion", nullable = false)
    private String definicion;
}
