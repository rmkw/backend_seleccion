package com.seleccion.backend.entities.variables;


import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variable_tabla_dto {
    private String idA;
    private String idS;
    private String idFuente;
    private String acronimo;
    private String nombre;
    private String definicion;
    private String url;
    private String comentarioS;

    private Boolean mdea;
    private Boolean ods;

    private Integer responsableRegister;
    private Integer responsableActualizacion;

    private Short prioridad;
    private Boolean revisada;
    private LocalDateTime fechaRevision;
    private Integer responsableRevision;
}
