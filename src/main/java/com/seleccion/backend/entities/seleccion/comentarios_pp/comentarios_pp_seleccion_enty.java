package com.seleccion.backend.entities.seleccion.comentarios_pp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "comentarios_pp", schema = "seleccion")
@Data
public class comentarios_pp_seleccion_enty {
    @Id
    @Column(name = "acronimo", nullable = false, length = 50)
    private String acronimo;

    @Column(name = "comentario_s", columnDefinition = "TEXT")
    private String comentarioS;
}
