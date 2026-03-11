package com.seleccion.backend.entities.import_excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class import_excel_error_fila_dto {
    /** Número de fila en Excel (1-based) */
    private int fila;

    /** Columna donde ocurrió el error (A, B, C, etc.) */
    private String columna;

    /** Descripción del error */
    private String detalle;
}
