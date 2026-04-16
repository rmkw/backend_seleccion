package com.seleccion.backend.entities.tematicas_temas;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Table(name = "temas_subtemas", schema = "catalogos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class temas_subtemas_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema_subtema")
    private Integer idTemaSubtema;

    @Column(name = "tema", nullable = false)
    private String tema;

    @Column(name = "subtema", nullable = false)
    private String subtema;
}
