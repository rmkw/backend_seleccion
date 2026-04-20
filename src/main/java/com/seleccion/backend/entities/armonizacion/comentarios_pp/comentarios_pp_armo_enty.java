package com.seleccion.backend.entities.armonizacion.comentarios_pp;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "comentarios_pp", schema = "armonizacion")
@Data
public class comentarios_pp_armo_enty {
    @Id
    @Column(name = "acronimo", nullable = false, length = 50)
    private String acronimo;

    @Column(name = "comentario_s", columnDefinition = "TEXT")
    private String comentarioS;
}
