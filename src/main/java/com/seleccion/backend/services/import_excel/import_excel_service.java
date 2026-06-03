package com.seleccion.backend.services.import_excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.seleccion.backend.entities.import_excel.import_excel_error_fila_dto;
import com.seleccion.backend.entities.import_excel.import_excel_import_result_dto;
import com.seleccion.backend.entities.import_excel.import_excel_validacion_dto;

import com.seleccion.backend.entities.usuario.usuario_enty;
import com.seleccion.backend.repositories.usuario.usuario_repo;

import com.seleccion.backend.entities.variables.variables_enty;
import com.seleccion.backend.repositories.variables.variables_repo;

import com.seleccion.backend.entities.fuentes.fuentes_enty;
import com.seleccion.backend.repositories.fuentes.fuentes_repo;

import com.seleccion.backend.entities.mdea.produccion.mdea_enty;
import com.seleccion.backend.repositories.mdea.produccion.mdea_repo;

import com.seleccion.backend.entities.ods.produccion.ods_enty;
import com.seleccion.backend.repositories.ods.produccion.ods_repo;

import com.seleccion.backend.entities.pertinencias.pertinencia_enty;
import com.seleccion.backend.repositories.pertinencias.pertinencia_repo;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.InputStream;
import java.security.Principal;
import java.util.*;


import com.seleccion.backend.entities.comentarios_pp.comentarios_pp_enty;
import com.seleccion.backend.repositories.comentarios_pp.comentarios_pp_repo;

@Service
public class import_excel_service {

    private final variables_repo variablesRepo;
    private final fuentes_repo fuentesRepo;
    private final mdea_repo mdeaRepo;
    private final ods_repo odsRepo;
    private final pertinencia_repo pertinenciaRepo;
    private final usuario_repo usuarioRepo;

    private final comentarios_pp_repo comentariosPpRepo;

    public import_excel_service(
            variables_repo variablesRepo,
            fuentes_repo fuentesRepo,
            mdea_repo mdeaRepo,
            ods_repo odsRepo,
            pertinencia_repo pertinenciaRepo,
            usuario_repo usuarioRepo,
            comentarios_pp_repo comentariosPpRepo) {
        this.variablesRepo = variablesRepo;
        this.fuentesRepo = fuentesRepo;
        this.mdeaRepo = mdeaRepo;
        this.odsRepo = odsRepo;
        this.pertinenciaRepo = pertinenciaRepo;
        this.usuarioRepo = usuarioRepo;
        this.comentariosPpRepo = comentariosPpRepo;
    }

    private static final List<String> REQUIRED_HEADERS = List.of(
              "proceso", "acronimo", "metodo", "comentario_sProceso",
            "fuente", "url", "urlVariable", "edicion", "comentario_sFuente",
            "id_s", "nombre", "definicion", "comentario_sVariable",
            "mdea", "componente", "subcomponente", "tema", "estadistica1", "estadistica2",
            "contribucionMdea", "comentario_sMdea",
            "ods", "objetivo", "meta", "indicador", "contribucionOds", "comentario_sOds",
            "pertinencia", "contribucionPertinencia", "viabilidad", "propuesta", "comentario_sPertinencia");

    private Integer getResponsableIdFromSession(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new RuntimeException("No se pudo identificar al usuario de la sesión.");
        }

        String username = principal.getName();
        usuario_enty user = usuarioRepo.findByNombre(username);

        if (user == null) {
            throw new RuntimeException("Usuario de sesión no existe en BD: " + username);
        }

        return Math.toIntExact(user.getId());
    }

    public import_excel_validacion_dto validar(MultipartFile file) {

        List<import_excel_error_fila_dto> errors = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            errors.add(new import_excel_error_fila_dto(0, "-", "No se recibió archivo o viene vacío."));
            return import_excel_validacion_dto.fail("Archivo vacío.", errors);
        }

        String originalName = file.getOriginalFilename();
        String fileName = originalName == null ? "" : originalName.toLowerCase();

        if (!fileName.endsWith(".xlsx")) {
            errors.add(new import_excel_error_fila_dto(0, "-", "Formato inválido. El archivo debe ser .xlsx"));
            return import_excel_validacion_dto.fail("Formato inválido.", errors);
        }

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;

            if (sheet == null) {
                errors.add(new import_excel_error_fila_dto(0, "-", "El archivo Excel no contiene hojas."));
                return import_excel_validacion_dto.fail("Excel sin hojas.", errors);
            }

            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                errors.add(new import_excel_error_fila_dto(1, "-", "No se encontró la fila de encabezados."));
                return import_excel_validacion_dto.fail("Encabezados faltantes.", errors);
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(headerRow);

            for (String required : REQUIRED_HEADERS) {
                if (!headerIndex.containsKey(normalize(required))) {
                    errors.add(new import_excel_error_fila_dto(
                            1,
                            required,
                            "Falta el encabezado requerido: " + required));
                }
            }

            if (!errors.isEmpty()) {
                return import_excel_validacion_dto.fail("La plantilla no coincide con el formato esperado.", errors);
            }

            int filasConDatos = 0;

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                filasConDatos++;

                
                
                String idS = getByHeader(row, headerIndex, "id_s");
                String acronimo = getByHeader(row, headerIndex, "acronimo");
                String fuente = getByHeader(row, headerIndex, "fuente");
                String edicion = getByHeader(row, headerIndex, "edicion");
                String url = getByHeader(row, headerIndex, "url");
                String nombre = getByHeader(row, headerIndex, "nombre");

                

                

                if (isBlank(idS)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "id_s", "id_s está vacío."));
                }

                if (isBlank(acronimo)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "acronimo", "acronimo está vacío."));
                }

                if (isBlank(nombre)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "nombre", "nombre está vacío."));
                }

                if (isBlank(fuente)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "fuente", "fuente está vacío."));
                }

                if (isBlank(edicion)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "edicion", "edicion está vacío."));
                }

                if (isBlank(url)) {
                    errors.add(new import_excel_error_fila_dto(r + 1, "url", "url está vacío."));
                }
            }

            if (filasConDatos == 0) {
                errors.add(new import_excel_error_fila_dto(
                        2,
                        "-",
                        "El archivo no contiene registros para importar. Pegue información desde la fila 2."));

                return import_excel_validacion_dto.fail("Archivo sin datos.", errors);
            }

            if (!errors.isEmpty()) {
                return import_excel_validacion_dto.fail("Se encontraron errores en el archivo.", errors);
            }

            return import_excel_validacion_dto.ok("Archivo válido. Ya puedes importar.");

        } catch (Exception e) {
            errors.add(new import_excel_error_fila_dto(
                    0,
                    "-",
                    "No se pudo leer el archivo Excel: " + e.getMessage()));

            return import_excel_validacion_dto.fail("Error al leer el Excel.", errors);
        }
    }

    @Transactional
    public import_excel_import_result_dto importar(MultipartFile file, Principal principal) {

        int fuentesInsertadas = 0;
        int fuentesActualizadas = 0;
        int variablesInsertadas = 0;
        int variablesActualizadas = 0;
        int mdeaInsertados = 0;
        int mdeaOmitidos = 0;
        int odsInsertados = 0;
        int odsOmitidos = 0;
        int pertinenciaInsertadas = 0;
        int pertinenciaActualizadas = 0;

        List<import_excel_error_fila_dto> errors = new ArrayList<>();

        int filasTotales = 0;
        int filasConDatos = 0;
        int filasImportadas = 0;

        Integer responsableId;

        try {
            responsableId = getResponsableIdFromSession(principal);
        } catch (RuntimeException ex) {
            return import_excel_import_result_dto.fail(
                    "No se pudo importar.",
                    0, 0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    List.of(new import_excel_error_fila_dto(0, "-", ex.getMessage())));
        }

        import_excel_validacion_dto validacion = validar(file);

        if (!validacion.isOk()) {
            return import_excel_import_result_dto.fail(
                    "No se importó porque el archivo tiene errores. Corrige y vuelve a intentar.",
                    0, 0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    validacion.getErrors());
        }

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;

            if (sheet == null) {
                errors.add(new import_excel_error_fila_dto(0, "-", "El archivo Excel no contiene hojas."));

                return import_excel_import_result_dto.fail(
                        "Excel sin hojas.",
                        0, 0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        errors);
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(sheet.getRow(0));
            filasTotales = sheet.getLastRowNum();

            Map<String, fuentes_enty> fuentesPorIdSeleccion = new LinkedHashMap<>();
            Map<String, variables_enty> variablesPorIdA = new LinkedHashMap<>();
            Map<String, pertinencia_enty> pertinenciaPorIdA = new LinkedHashMap<>();

            Set<String> mdeaKeysArchivo = new HashSet<>();
            Set<String> odsKeysArchivo = new HashSet<>();

            Map<String, Integer> filaFuente = new HashMap<>();
            Map<String, Integer> filaVariable = new HashMap<>();

            Map<String, comentarios_pp_enty> comentariosPorAcronimo = new LinkedHashMap<>();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                filasConDatos++;

                
                
                String idS = getByHeader(row, headerIndex, "id_s");
                String acronimo = getByHeader(row, headerIndex, "acronimo");
                
                String comentarioSProceso = getByHeader(row, headerIndex, "comentario_sProceso");
                String nombre = getByHeader(row, headerIndex, "nombre");
                String definicion = getByHeader(row, headerIndex, "definicion");
                
                String urlVariable = getByHeader(row, headerIndex, "urlVariable");
                String comentarioSVariable = getByHeader(row, headerIndex, "comentario_sVariable");

                Boolean flagMdea = parseBoolean(getByHeader(row, headerIndex, "mdea"));
                Boolean flagOds = parseBoolean(getByHeader(row, headerIndex, "ods"));

                String fuenteNombre = getByHeader(row, headerIndex, "fuente");
                String urlFuente = getByHeader(row, headerIndex, "url");
                String edicion = getByHeader(row, headerIndex, "edicion");

                String idA = buildIdA(idS, edicion);
                String idFuenteSeleccion = buildIdFuente(acronimo, fuenteNombre, edicion, urlFuente);

                if (  isBlank(idS)
                        || isBlank(acronimo)
                        || isBlank(fuenteNombre)
                        || isBlank(edicion)
                        || isBlank(urlFuente)
                        || isBlank(nombre)) {
                    errors.add(new import_excel_error_fila_dto(
                            r + 1,
                            "-",
                            "Fila incompleta. Requiere id_s, acronimo y nombre."));

                    throw new ImportExcelAbortException(
                            "El archivo tiene filas incompletas. Se canceló la importación.",
                            errors);
                }
                if (!isBlank(comentarioSProceso)) {
                    comentariosPorAcronimo.put(
                            acronimo,
                            comentarios_pp_enty.builder()
                                    .acronimo(acronimo)
                                    .comentarioS(comentarioSProceso)
                                    .build());
                }

                
                String comentarioSFuente = getByHeader(row, headerIndex, "comentario_sFuente");

                fuentes_enty fuente = fuentesPorIdSeleccion.get(idFuenteSeleccion);

                if (fuente == null) {
                    fuente = new fuentes_enty();
                    fuente.setIdFuenteSeleccion(idFuenteSeleccion);

                    fuentesPorIdSeleccion.put(idFuenteSeleccion, fuente);
                    filaFuente.put(idFuenteSeleccion, r + 1);
                }

                fuente.setAcronimo(acronimo);
                fuente.setFuente(isBlank(fuenteNombre) ? "SIN_FUENTE" : fuenteNombre);
                fuente.setUrl(isBlank(urlFuente) ? "" : urlFuente);
                fuente.setEdicion(isBlank(edicion) ? "" : edicion);
                fuente.setComentarioS(isBlank(comentarioSFuente) ? null : comentarioSFuente);

                variables_enty variable = variablesPorIdA.get(idA);

                if (variable == null) {
                    variable = new variables_enty();
                    variable.setIdA(idA);
                    

                    variable.setRevisada(false);
                    variable.setPrioridad(1);
                    variable.setFechaRevision(null);
                    variable.setResponsableRevision(null);

                    variablesPorIdA.put(idA, variable);
                    filaVariable.put(idA, r + 1);
                }

                variable.setIdFuente(idFuenteSeleccion);
                variable.setIdS(idS);
                variable.setAcronimo(acronimo);
                variable.setNombre(nombre);
                variable.setDefinicion(isBlank(definicion) ? "" : definicion);
                variable.setUrl(isBlank(urlVariable) ? "" : urlVariable);
                variable.setComentarioS(isBlank(comentarioSVariable) ? "" : comentarioSVariable);
                variable.setMdea(Boolean.TRUE.equals(flagMdea));
                variable.setOds(Boolean.TRUE.equals(flagOds));

                String pertinenciaTxt = getByHeader(row, headerIndex, "pertinencia");

                if (!isBlank(pertinenciaTxt)) {
                    String contribucionP = getByHeader(row, headerIndex, "contribucionPertinencia");
                    String viabilidad = getByHeader(row, headerIndex, "viabilidad");
                    String propuesta = getByHeader(row, headerIndex, "propuesta");
                    String comentarioSP = getByHeader(row, headerIndex, "comentario_sPertinencia");

                    pertinencia_enty pertinencia = pertinenciaPorIdA.get(idA);

                    if (pertinencia == null) {
                        pertinencia = new pertinencia_enty();
                        pertinencia.setIdA(idA);
                        pertinenciaPorIdA.put(idA, pertinencia);
                    }

                    pertinencia.setIdS(idS);
                    pertinencia.setPertinencia(pertinenciaTxt);
                    pertinencia.setContribucion(isBlank(contribucionP) ? "" : contribucionP);
                    pertinencia.setViabilidad(isBlank(viabilidad) ? "" : viabilidad);
                    pertinencia.setPropuesta(isBlank(propuesta) ? "" : propuesta);
                    pertinencia.setComentarioS(isBlank(comentarioSP) ? "" : comentarioSP);
                }

                if (Boolean.TRUE.equals(flagMdea)) {
                    String componente = extractTrailingNumber(
                            clean(blankToNull(getByHeader(row, headerIndex, "componente"))));
                    String subcomponente = extractTrailingCodeWithoutDots(
                            clean(blankToNull(getByHeader(row, headerIndex, "subcomponente"))));
                    String tema = extractTrailingCodeWithoutDots(
                            clean(blankToNull(getByHeader(row, headerIndex, "tema"))));
                    String estadistica1 = buildLetterCode(
                            tema,
                            clean(blankToNull(getByHeader(row, headerIndex, "estadistica1"))));
                    String estadistica2 = buildNumberCode(
                            estadistica1,
                            clean(blankToNull(getByHeader(row, headerIndex, "estadistica2"))));

                    String key = String.join("|",
                            normalize(idA),
                            normalize(String.valueOf(componente)),
                            normalize(String.valueOf(subcomponente)),
                            normalize(String.valueOf(tema)),
                            normalize(String.valueOf(estadistica1)),
                            normalize(String.valueOf(estadistica2)));

                    if (!mdeaKeysArchivo.add(key)) {
                        mdeaOmitidos++;
                    }
                }

                if (Boolean.TRUE.equals(flagOds)) {
                    String objetivo = extractTrailingNumberOrDash(
                            clean(blankToNull(getByHeader(row, headerIndex, "objetivo"))));
                    String meta = extractTrailingCodeWithoutDotsOrDash(
                            clean(blankToNull(getByHeader(row, headerIndex, "meta"))));
                    String indicador = extractTrailingCodeWithoutDotsOrDash(
                            clean(blankToNull(getByHeader(row, headerIndex, "indicador"))));

                    String key = String.join("|",
                            normalize(idA),
                            normalize(String.valueOf(objetivo)),
                            normalize(String.valueOf(meta)),
                            normalize(String.valueOf(indicador)));

                    if (!odsKeysArchivo.add(key)) {
                        odsOmitidos++;
                    }
                }

                filasImportadas++;
            }

            if (filasConDatos == 0) {
                errors.add(new import_excel_error_fila_dto(
                        2,
                        "-",
                        "El archivo no contiene registros para importar. Pegue información desde la fila 2."));

                return import_excel_import_result_dto.fail(
                        "Archivo sin datos.",
                        filasTotales, 0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        0, 0,
                        errors);
            }

            for (Map.Entry<String, comentarios_pp_enty> entry : comentariosPorAcronimo.entrySet()) {
                String acronimo = entry.getKey();
                comentarios_pp_enty data = entry.getValue();

                try {
                    comentarios_pp_enty existente = comentariosPpRepo.findById(acronimo).orElse(null);

                    if (existente == null) {
                        comentariosPpRepo.save(data);
                    } else {
                        existente.setComentarioS(data.getComentarioS());
                        comentariosPpRepo.save(existente);
                    }

                } catch (Exception ex) {
                    Throwable root = ex;

                    while (root.getCause() != null) {
                        root = root.getCause();
                    }

                    errors.add(new import_excel_error_fila_dto(
                            0,
                            "comentario_sProceso",
                            "Error guardando comentario del proceso (" + acronimo + "): " + root.getMessage()));

                    throw new ImportExcelAbortException(
                            "Error guardando COMENTARIOS_PP. Se canceló la importación.",
                            errors);
                }
            }

            for (Map.Entry<String, fuentes_enty> entry : fuentesPorIdSeleccion.entrySet()) {
                String idFuenteSeleccion = entry.getKey();
                fuentes_enty data = entry.getValue();
                int fila = filaFuente.getOrDefault(idFuenteSeleccion, 0);

                try {
                    fuentes_enty existente = fuentesRepo.findById(idFuenteSeleccion).orElse(null);

                    if (existente == null) {
                        fuentesRepo.save(data);
                        fuentesInsertadas++;
                    } else {
                        existente.setAcronimo(data.getAcronimo());
                        existente.setFuente(data.getFuente());
                        existente.setUrl(data.getUrl());
                        existente.setEdicion(data.getEdicion());
                        existente.setComentarioS(data.getComentarioS());

                        fuentesRepo.save(existente);
                        fuentesActualizadas++;
                    }

                } catch (Exception ex) {
                    Throwable root = ex;

                    while (root.getCause() != null) {
                        root = root.getCause();
                    }

                    errors.add(new import_excel_error_fila_dto(
                            fila,
                            "fuentes",
                            "Error guardando fuente (" + idFuenteSeleccion + "): " + root.getMessage()));

                    throw new ImportExcelAbortException("Error guardando FUENTES. Se canceló la importación.", errors);
                }
            }

            for (Map.Entry<String, variables_enty> entry : variablesPorIdA.entrySet()) {
                String idA = entry.getKey();
                variables_enty data = entry.getValue();
                int fila = filaVariable.getOrDefault(idA, 0);

                try {
                    variables_enty existente = variablesRepo.findById(idA).orElse(null);

                    if (existente == null) {
                        

                        data.setRevisada(false);
                        data.setPrioridad(1);
                        data.setFechaRevision(null);
                        data.setResponsableRevision(null);

                        variablesRepo.save(data);
                        variablesInsertadas++;
                    } else {
                        

                        existente.setIdFuente(data.getIdFuente());
                        existente.setIdS(data.getIdS());
                        existente.setAcronimo(data.getAcronimo());
                        existente.setNombre(data.getNombre());
                        existente.setDefinicion(data.getDefinicion());
                        existente.setUrl(data.getUrl());
                        existente.setComentarioS(data.getComentarioS());
                        existente.setMdea(data.getMdea());
                        existente.setOds(data.getOds());

                        variablesRepo.save(existente);
                        variablesActualizadas++;
                    }

                } catch (Exception ex) {
                    Throwable root = ex;

                    while (root.getCause() != null) {
                        root = root.getCause();
                    }

                    errors.add(new import_excel_error_fila_dto(
                            fila,
                            "variables",
                            "Error guardando variable (" + idA + "): " + root.getMessage()));

                    throw new ImportExcelAbortException("Error guardando VARIABLES. Se canceló la importación.",
                            errors);
                }
            }

            for (Map.Entry<String, pertinencia_enty> entry : pertinenciaPorIdA.entrySet()) {
                String idA = entry.getKey();
                pertinencia_enty data = entry.getValue();
                int fila = filaVariable.getOrDefault(idA, 0);

                try {
                    pertinencia_enty existente = pertinenciaRepo.findByIdA(idA).orElse(null);

                    if (existente == null) {
                        data.setIdA(idA);
                        pertinenciaRepo.save(data);
                        pertinenciaInsertadas++;
                    } else {
                        existente.setIdS(data.getIdS());
                        existente.setPertinencia(data.getPertinencia());
                        existente.setContribucion(data.getContribucion());
                        existente.setViabilidad(data.getViabilidad());
                        existente.setPropuesta(data.getPropuesta());
                        existente.setComentarioS(data.getComentarioS());

                        pertinenciaRepo.save(existente);
                        pertinenciaActualizadas++;
                    }

                } catch (Exception ex) {
                    Throwable root = ex;

                    while (root.getCause() != null) {
                        root = root.getCause();
                    }

                    errors.add(new import_excel_error_fila_dto(
                            fila,
                            "pertinencia",
                            "Error guardando pertinencia (" + idA + "): " + root.getMessage()));

                    throw new ImportExcelAbortException("Error guardando PERTINENCIA. Se canceló la importación.",
                            errors);
                }
            }

            Set<String> mdeaInsertadosArchivo = new HashSet<>();
            Set<String> odsInsertadosArchivo = new HashSet<>();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                
                String idS = getByHeader(row, headerIndex, "id_s");
                String edicion = getByHeader(row, headerIndex, "edicion");
                String idA = buildIdA(idS, edicion);

                Boolean flagMdea = parseBoolean(getByHeader(row, headerIndex, "mdea"));
                Boolean flagOds = parseBoolean(getByHeader(row, headerIndex, "ods"));

                if (Boolean.TRUE.equals(flagMdea)) {
                    try {
                        String componente = extractTrailingNumber(
                                clean(blankToNull(getByHeader(row, headerIndex, "componente"))));
                        String subcomponente = extractTrailingCodeWithoutDots(
                                clean(blankToNull(getByHeader(row, headerIndex, "subcomponente"))));
                        String tema = extractTrailingCodeWithoutDots(
                                clean(blankToNull(getByHeader(row, headerIndex, "tema"))));
                        String estadistica1 = buildLetterCode(
                                tema,
                                clean(blankToNull(getByHeader(row, headerIndex, "estadistica1"))));
                        String estadistica2 = buildNumberCode(
                                estadistica1,
                                clean(blankToNull(getByHeader(row, headerIndex, "estadistica2"))));
                        String contribucionMdea = clean(blankToNull(getByHeader(row, headerIndex, "contribucionMdea")));
                        String comentarioSMdea = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sMdea")));

                        String key = String.join("|",
                                normalize(idA),
                                normalize(String.valueOf(componente)),
                                normalize(String.valueOf(subcomponente)),
                                normalize(String.valueOf(tema)),
                                normalize(String.valueOf(estadistica1)),
                                normalize(String.valueOf(estadistica2)));

                        if (!mdeaInsertadosArchivo.add(key)) {
                            continue;
                        }

                        boolean existe = mdeaRepo
                                .existsByIdAAndComponenteAndSubcomponenteAndTemaAndEstadistica1AndEstadistica2(
                                        idA,
                                        componente,
                                        subcomponente,
                                        tema,
                                        estadistica1,
                                        estadistica2);

                        if (!existe) {
                            mdea_enty mdea = mdea_enty.builder()
                                    .idA(idA)
                                    .idS(idS)
                                    .componente(componente)
                                    .subcomponente(subcomponente)
                                    .tema(tema)
                                    .estadistica1(estadistica1)
                                    .estadistica2(estadistica2)
                                    .contribucion(contribucionMdea)
                                    .comentarioS(comentarioSMdea)
                                    .build();

                            mdeaRepo.save(mdea);
                            mdeaInsertados++;
                        } else {
                            mdeaOmitidos++;
                        }

                    } catch (Exception ex) {
                        Throwable root = ex;

                        while (root.getCause() != null) {
                            root = root.getCause();
                        }

                        errors.add(new import_excel_error_fila_dto(
                                r + 1,
                                "mdea",
                                "Error guardando MDEA: " + root.getMessage()));

                        throw new ImportExcelAbortException("Error guardando MDEA. Se canceló la importación.", errors);
                    }
                }

                if (Boolean.TRUE.equals(flagOds)) {
                    try {
                        String objetivo = extractTrailingNumberOrDash(
                                clean(blankToNull(getByHeader(row, headerIndex, "objetivo"))));
                        String meta = extractTrailingCodeWithoutDotsOrDash(
                                clean(blankToNull(getByHeader(row, headerIndex, "meta"))));
                        String indicador = extractTrailingCodeWithoutDotsOrDash(
                                clean(blankToNull(getByHeader(row, headerIndex, "indicador"))));
                        String contribucionOds = clean(blankToNull(getByHeader(row, headerIndex, "contribucionOds")));
                        String comentarioSOds = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sOds")));

                        String key = String.join("|",
                                normalize(idA),
                                normalize(String.valueOf(objetivo)),
                                normalize(String.valueOf(meta)),
                                normalize(String.valueOf(indicador)));

                        if (!odsInsertadosArchivo.add(key)) {
                            continue;
                        }

                        boolean existe = odsRepo.existsByIdAAndObjetivoAndMetaAndIndicador(
                                idA,
                                objetivo,
                                meta,
                                indicador);

                        if (!existe) {
                            ods_enty ods = ods_enty.builder()
                                    .idA(idA)
                                    .idS(idS)
                                    .objetivo(objetivo)
                                    .meta(meta)
                                    .indicador(indicador)
                                    .contribucion(contribucionOds)
                                    .comentarioS(comentarioSOds)
                                    .build();

                            odsRepo.save(ods);
                            odsInsertados++;
                        } else {
                            odsOmitidos++;
                        }

                    } catch (Exception ex) {
                        Throwable root = ex;

                        while (root.getCause() != null) {
                            root = root.getCause();
                        }

                        errors.add(new import_excel_error_fila_dto(
                                r + 1,
                                "ods",
                                "Error guardando ODS: " + root.getMessage()));

                        throw new ImportExcelAbortException("Error guardando ODS. Se canceló la importación.", errors);
                    }
                }
            }

            return import_excel_import_result_dto.ok(
                    "Importación completada correctamente.",
                    filasTotales,
                    filasConDatos,
                    filasImportadas,

                    fuentesInsertadas,
                    fuentesActualizadas,

                    variablesInsertadas,
                    variablesActualizadas,

                    mdeaInsertados,
                    mdeaOmitidos,

                    odsInsertados,
                    odsOmitidos,

                    pertinenciaInsertadas,
                    pertinenciaActualizadas);

        } catch (ImportExcelAbortException abort) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return import_excel_import_result_dto.fail(
                    abort.getMessage(),
                    filasTotales,
                    filasConDatos,
                    filasImportadas,

                    fuentesInsertadas,
                    fuentesActualizadas,

                    variablesInsertadas,
                    variablesActualizadas,

                    mdeaInsertados,
                    mdeaOmitidos,

                    odsInsertados,
                    odsOmitidos,

                    pertinenciaInsertadas,
                    pertinenciaActualizadas,

                    abort.getErrors());

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Throwable root = e;

            while (root.getCause() != null) {
                root = root.getCause();
            }

            errors.add(new import_excel_error_fila_dto(
                    0,
                    "-",
                    "Error al importar: " + root.getMessage()));

            return import_excel_import_result_dto.fail(
                    "Error al importar. Se canceló la importación.",
                    filasTotales,
                    filasConDatos,
                    filasImportadas,

                    fuentesInsertadas,
                    fuentesActualizadas,

                    variablesInsertadas,
                    variablesActualizadas,

                    mdeaInsertados,
                    mdeaOmitidos,

                    odsInsertados,
                    odsOmitidos,

                    pertinenciaInsertadas,
                    pertinenciaActualizadas,

                    errors);
        }
    }

    private String blankToNull(String s) {
        if (s == null) {
            return null;
        }

        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private Boolean parseBoolean(String raw) {
        if (raw == null) {
            return null;
        }

        String v = raw.trim().toLowerCase();
        v = v.replace("í", "i");

        if (v.isEmpty()) {
            return null;
        }

        if (v.equals("true")
                || v.equals("t")
                || v.equals("1")
                || v.equals("si")
                || v.equals("s")
                || v.equals("x")
                || v.equals("verdadero")
                || v.equals("v")) {
            return true;
        }

        if (v.equals("false")
                || v.equals("f")
                || v.equals("0")
                || v.equals("no")
                || v.equals("n")
                || v.equals("falso")
                || v.equals("-")) {
            return false;
        }

        return null;
    }

    private Map<String, Integer> buildHeaderIndex(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();

        short last = headerRow.getLastCellNum();

        for (int i = 0; i < last; i++) {
            String header = normalize(getCellAsString(headerRow.getCell(i)));

            if (!header.isBlank()) {
                map.put(header, i);
            }
        }

        return map;
    }

    private String getByHeader(Row row, Map<String, Integer> index, String header) {
        Integer col = index.get(normalize(header));

        if (col == null) {
            return "";
        }

        return getCellAsString(row.getCell(col));
    }

    private boolean isRowEmpty(Row row) {
        short last = row.getLastCellNum();

        for (int i = 0; i < last; i++) {
            if (!isBlank(getCellAsString(row.getCell(i)))) {
                return false;
            }
        }

        return true;
    }

    private String getCellAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();

            case NUMERIC -> {
                double d = cell.getNumericCellValue();

                if (d == Math.rint(d)) {
                    yield String.valueOf((long) d);
                }

                yield String.valueOf(d);
            }

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());

            case FORMULA -> {
                try {
                    yield cell.getStringCellValue().trim();
                } catch (Exception ex) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }

            default -> "";
        };
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    private String clean(String s) {
        if (s == null) {
            return null;
        }

        return s.trim();
    }

    private String extractTrailingNumber(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        int underscore = trimmed.lastIndexOf("_");

        if (underscore >= 0 && underscore < trimmed.length() - 1) {
            String suffix = trimmed.substring(underscore + 1).trim();

            if (suffix.matches("\\d+")) {
                return suffix;
            }
        }

        if (trimmed.matches("\\d+")) {
            return trimmed;
        }

        return trimmed;
    }
    
    
    
    private String buildLetterCode(String parentCode, String value) {
        if (value == null) {
            return "-";
        }

        String trimmed = value.trim();

        if (trimmed.isEmpty() || trimmed.equals("-")) {
            return "-";
        }

        int dotIndex = trimmed.indexOf(".");

        if (dotIndex > 0) {
            String letter = trimmed.substring(0, dotIndex).trim();

            if (letter.matches("[a-zA-Z]")) {
                return isBlank(parentCode) ? letter.toLowerCase() : clean(parentCode) + letter.toLowerCase();
            }
        }

        if (trimmed.matches("[a-zA-Z]")) {
            return isBlank(parentCode) ? trimmed.toLowerCase() : clean(parentCode) + trimmed.toLowerCase();
        }

        return trimmed;
    }

    private String buildNumberCode(String parentCode, String value) {
        if (value == null) {
            return "-";
        }

        String trimmed = value.trim();

        if (trimmed.isEmpty()
                || trimmed.equals("-")
                || trimmed.equalsIgnoreCase("No cuenta con estadístico")) {
            return "-";
        }

        int dotIndex = trimmed.indexOf(".");

        if (dotIndex > 0) {
            String number = trimmed.substring(0, dotIndex).trim();

            if (number.matches("\\d+")) {
                return isBlank(parentCode) ? number : clean(parentCode) + number;
            }
        }

        if (trimmed.matches("\\d+")) {
            return isBlank(parentCode) ? trimmed : clean(parentCode) + trimmed;
        }

        return trimmed;
    }
    
    private String buildIdFuente(String acronimo, String fuente, String edicion, String url) {
        return clean(acronimo) + "-" + clean(fuente) + "-" + clean(edicion) + "-" + clean(url);
    }

    private String buildIdA(String idS, String edicion) {
        return clean(idS) + "-" + clean(edicion);
    }
    
    
    
    
    
    

    

    private String extractTrailingNumberOrDash(String value) {
        if (value == null) {
            return "-";
        }

        String trimmed = value.trim();

        if (trimmed.isEmpty() || trimmed.equals("-")) {
            return "-";
        }

        String code = extractTrailingNumericCode(trimmed);

        if (code != null && code.matches("\\d+")) {
            return code;
        }

        return trimmed;
    }

    

    private String extractTrailingNumericCode(String value) {
        String trimmed = value.trim();
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("(\\d+(?:\\.\\d+)*)\\s*$")
                .matcher(trimmed);

        return matcher.find() ? matcher.group(1) : null;
    }

    
    
    private String extractTrailingCodeWithoutDots(String value) {
        if (value == null) {
            return null;
        }

        String code = extractTrailingAlphanumericCode(value);

        if (code != null) {
            return code.replace(".", "").toLowerCase();
        }

        return value.trim();
    }

    private String extractTrailingCodeWithoutDotsOrDash(String value) {
        if (value == null) {
            return "-";
        }

        String trimmed = value.trim();

        if (trimmed.isEmpty() || trimmed.equals("-")) {
            return "-";
        }

        String code = extractTrailingAlphanumericCode(trimmed);

        if (code != null) {
            return code.replace(".", "").toLowerCase();
        }

        return trimmed;
    }

    private String extractTrailingAlphanumericCode(String value) {
        String trimmed = value.trim();

        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("(\\d+(?:\\.[a-zA-Z0-9]+)*)\\s*$")
                .matcher(trimmed);

        return matcher.find() ? matcher.group(1) : null;
    }
}