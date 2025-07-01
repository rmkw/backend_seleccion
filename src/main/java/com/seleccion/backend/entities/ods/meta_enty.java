package com.seleccion.backend.entities.ods;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

public class meta_enty {
    @Id
    @Column(name = "id_objetivo")
    private Integer idObjetivo;
    
    @Id
    @Column(name = "id_meta")
    private String idMeta;
    
    @Column(name = "name_meta", nullable = false)
    private String nombreMeta;
    
    @ManyToOne
    @MapsId("idObjetivo")
    @JoinColumn(name = "id_objetivo", insertable = false, updatable = false)
    @JsonIgnore
    private objetivo_enty objetivo;
}
