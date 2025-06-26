package com.seleccion.backend.entities.procesos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "procesos", schema = "seleccion_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class procesos_enty {
    @Id
    @Column(name = "acronimo", nullable = false)
    private String acronimo;

    @Column(name = "proceso")
    private String proceso;

    @Column(name = "metodo")
    private String metodo;

    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "pobjeto")
    private String pobjeto;

    @Column(name = "uobservacion")
    private String uobservacion;

    @Column(name = "unidad")
    private String unidad;

    @Column(name = "periodicidad")
    private String periodicidad;

    @Column(name = "iin", length = 2)
    private String iin;

    @Column(name = "estatus")
    private String estatus;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "comentario_a")
    private String comentarioA;
}
