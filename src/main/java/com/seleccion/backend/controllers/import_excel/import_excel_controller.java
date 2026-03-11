package com.seleccion.backend.controllers.import_excel;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.seleccion.backend.entities.import_excel.import_excel_import_result_dto;
import com.seleccion.backend.entities.import_excel.import_excel_validacion_dto;
import com.seleccion.backend.services.import_excel.import_excel_service;

@RestController
@RequestMapping("/api/import-excel")
public class import_excel_controller {
    private final import_excel_service service;

    public import_excel_controller(import_excel_service service) {
        this.service = service;
    }

    @PostMapping(value = "/validar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public import_excel_validacion_dto validar(@RequestPart("file") MultipartFile file) {
        return service.validar(file);
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public import_excel_import_result_dto importar(@RequestPart("file") MultipartFile file, Principal principal) {
        return service.importar(file, principal);
    }

}
