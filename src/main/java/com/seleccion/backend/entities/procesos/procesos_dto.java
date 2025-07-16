package com.seleccion.backend.entities.procesos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class procesos_dto {
    private String acronimo;
    private String proceso;
    private String metodo;
    private String objetivo;
    private String pobjeto;
    private String uobservacion;
    private String unidad;
    private String periodicidad;
    private String iin;
    private String estatus;
    private String comentarioS;
    private String comentarioA;
    private Long totalVariables;
}
