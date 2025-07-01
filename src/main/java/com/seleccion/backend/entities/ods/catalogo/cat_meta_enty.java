package com.seleccion.backend.entities.ods.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ods_meta", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_meta_enty {
    @Id
    @Column(name = "id_meta")
    private String idMeta;

    @Column(name = "id_objetivo", nullable = false)
    private Integer idObjetivo;

    @Column(name = "meta", nullable = false)
    private String meta;
}
