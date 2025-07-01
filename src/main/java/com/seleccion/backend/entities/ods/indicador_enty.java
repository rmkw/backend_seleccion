package com.seleccion.backend.entities.ods;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

public class indicador_enty {
    @Id
    @Column(name = "id_objetivo")
    private Integer idObjetivo;
    
    @Id
    @Column(name = "id_meta")
    private String idMeta;
    
    @Id
    @Column(name = "id_indicador")
    private Integer idIndicador;
    
    @Column(name = "name_indicador", nullable = false)
    private String nombreIndicador;
    
    @ManyToOne
    @MapsId
    @JoinColumns({
        @JoinColumn(name = "id_objetivo", referencedColumnName = "id_objetivo"),
        @JoinColumn(name = "id_meta", referencedColumnName = "id_meta")
    })
    @JsonIgnore
    private meta_enty meta;
}
