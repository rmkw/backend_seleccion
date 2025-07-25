package com.seleccion.backend.entities.fuentes;



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
    @Column(name = "id_fuente")
    private String idFuente;

    @Column(name = "fuente", nullable = false)
    private String fuente;

    @Column(name = "url", unique = true)
    private String url;

    @Column(name = "edicion")
    private String edicion;

    @Column(name = "comentario_s")
    private String comentarioS;

    @Column(name = "responsable_register", nullable = false)
    private Integer responsableRegister;

    @Column(name = "responsable_actualizacion")
    private Integer responsableActualizacion;

    @Column(name = "acronimo", nullable = false)
    private String acronimo;

}
