package com.seleccion.backend.entities.comentarios_pp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comentarios_pp", schema = "seleccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class comentarios_pp_enty {

    @Id
    @Column(name = "acronimo", nullable = false, length = 50)
    private String acronimo;

    @Column(name = "comentario_s", nullable = false)
    private String comentarioS;
}