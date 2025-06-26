package com.seleccion.backend.entities.fuentes;

import com.seleccion.backend.entities.procesos.procesos_enty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fuentes", schema = "seleccion_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class fuentes_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fuente")
    private Integer idFuente;

    @Column(name = "fuente", nullable = false)
    private String fuente;

    @Column(name = "url")
    private String url;

    @Column(name = "edicion")
    private String edicion;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "responsable_register", nullable = false)
    private Integer responsableRegister;

    @Column(name = "responsable_actualizacion")
    private Integer responsableActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acronimo", nullable = false)
    private procesos_enty proceso;
}
