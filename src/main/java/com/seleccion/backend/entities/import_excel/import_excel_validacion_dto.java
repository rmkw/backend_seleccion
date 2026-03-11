package com.seleccion.backend.entities.import_excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class import_excel_validacion_dto {
     /** Indica si el archivo pasó la validación */
    private boolean ok;

    /** Mensaje general de la validación */
    private String message;

    /** Lista de errores por fila */
    private List<import_excel_error_fila_dto> errors = new ArrayList<>();

    /* Helpers para facilitar el uso */
    public static import_excel_validacion_dto ok(String message) {
        return new import_excel_validacion_dto(true, message, new ArrayList<>());
    }

    public static import_excel_validacion_dto fail(String message, List<import_excel_error_fila_dto> errors) {
        return new import_excel_validacion_dto(false, message, errors);
    }
}
