package com.seleccion.backend.entities.mdea.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mdea_estadisticos2", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_estadistico2_enty {
    @Id
    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "id_componente", nullable = false)
    private Integer idComponente;

    @Column(name = "id_subcomponente", nullable = false)
    private Integer idSubcomponente;

    @Column(name = "id_tema", nullable = false)
    private Integer idTema;

    @Column(name = "id_estadistico1", nullable = false)
    private String idEstadistico1;

    @Column(name = "id_estadistico2", nullable = false)
    private Integer idEstadistico2;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
