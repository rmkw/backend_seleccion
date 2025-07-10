package com.seleccion.backend.entities.variables;

import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.entities.pertinencias.pertinencia_enty;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class variables_relacion_dto {
    private String idA;
    private String idS;
    private Integer idFuente;
    private String acronimo;
    private String nombre;
    private String definicion;
    private String url;
    private String comentarioS;
    private Boolean mdea;
    private Boolean ods;
    private Integer responsableRegister;
    private Integer responsableActualizacion;

    private List<mdea_enty> mdeas;
    private List<ods_enty> odsList;
    private List<pertinencia_enty> pertinencia;
}
