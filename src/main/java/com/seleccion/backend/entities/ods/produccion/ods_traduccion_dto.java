package com.seleccion.backend.entities.ods.produccion;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ods_traduccion_dto {
    private Integer idUnique;
    private String idA;
    private String idS;

    private String objetivo;
    private String objetivoNombre;

    private String meta;
    private String metaNombre;

    private String indicador;
    private String indicadorNombre;

    private String contribucion;
    private String comentarioS;
}
