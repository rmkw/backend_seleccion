package com.seleccion.backend.entities.ods.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ods_indicador", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_indicador_enty {
     @Id
    @Column(name = "id_indicador")
    private Integer idIndicador;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "id_objetivo", nullable = false)
    private Integer idObjetivo;

    @Column(name = "id_meta", nullable = false)
    private String idMeta;

    @Column(name = "indicador", nullable = false)
    private String indicador;
}
