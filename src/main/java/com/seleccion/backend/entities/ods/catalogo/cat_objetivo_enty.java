package com.seleccion.backend.entities.ods.catalogo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ods_objetivo", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cat_objetivo_enty {
    @Id
    @Column(name = "id_objetivo")
    private Integer idObjetivo;

    @Column(name = "objetivo", nullable = false)
    private String objetivo;
}
