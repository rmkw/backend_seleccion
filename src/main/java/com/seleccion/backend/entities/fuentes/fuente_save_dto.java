package com.seleccion.backend.entities.fuentes;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class fuente_save_dto {
    private String acronimo;
    private String fuente;
    private String url;
    private String edicion;
    private String comentarioS;
    private String comentarioA;
    private String idFuenteSeleccion;
}
