package com.seleccion.backend.services.import_excel;

import java.util.List;

import com.seleccion.backend.entities.import_excel.import_excel_error_fila_dto;

public class ImportExcelAbortException extends RuntimeException {
    
    private final List<import_excel_error_fila_dto> errors;

    public ImportExcelAbortException(String message, List<import_excel_error_fila_dto> errors) {
        super(message);
        this.errors = errors;
    }

    public List<import_excel_error_fila_dto> getErrors() {
        return errors;
    }
}
