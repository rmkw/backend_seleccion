package com.seleccion.backend.entities.mdea.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mdea_componentes", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_componente_enty {
    @Id
    @Column(name = "id_componente")
    private Integer idComponente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "definicion", nullable = false)
    private String definicion;

    @Column(name = "definicion_corta", nullable = false)
    private String definicionCorta;
}
