package com.seleccion.backend.entities.unidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidades", schema = "catalogos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class unidades_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long idUnidad;

    private String nameUnidad; 
}
