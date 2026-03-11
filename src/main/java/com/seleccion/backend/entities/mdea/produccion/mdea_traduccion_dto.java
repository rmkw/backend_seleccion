package com.seleccion.backend.entities.mdea.produccion;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class mdea_traduccion_dto {
    private Integer idUnique;
    private String idA;
    private String idS;

    private String componente;
    private String componenteNombre;

    private String subcomponente;
    private String subcomponenteNombre;

    private String tema;
    private String temaNombre;

    private String estadistica1;
    private String estadistica1Nombre;

    private String estadistica2;
    private String estadistica2Nombre;

    private String contribucion;
    private String comentarioS;
}
