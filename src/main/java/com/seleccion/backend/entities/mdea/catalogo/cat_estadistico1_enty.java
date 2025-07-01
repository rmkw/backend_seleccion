package com.seleccion.backend.entities.mdea.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mdea_estadisticos1", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_estadistico1_enty {
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

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
