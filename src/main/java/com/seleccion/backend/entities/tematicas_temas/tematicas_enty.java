package com.seleccion.backend.entities.tematicas_temas;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tematicas", schema = "catalogos")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class tematicas_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tematica")
    private Integer idTematica;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "tematica", nullable = false)
    private String tematica;
}
