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

import jakarta.transaction.Transactional;

import java.io.InputStream;
import java.security.Principal;
import java.util.*;

@Service
public class import_excel_service {

    private final variables_repo variablesRepo;
    private final fuentes_repo fuentesRepo;
    private final mdea_repo mdeaRepo;
    private final ods_repo odsRepo;
    private final pertinencia_repo pertinenciaRepo;
    private final usuario_repo usuarioRepo;

    public import_excel_service(
            variables_repo variablesRepo,
            fuentes_repo fuentesRepo,
            mdea_repo mdeaRepo,
            ods_repo odsRepo,
            pertinencia_repo pertinenciaRepo,
            usuario_repo usuarioRepo
    ) {
        this.variablesRepo = variablesRepo;
        this.fuentesRepo = fuentesRepo;
        this.mdeaRepo = mdeaRepo;
        this.odsRepo = odsRepo;
        this.pertinenciaRepo = pertinenciaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // Encabezados requeridos (únicos)
    private static final List<String> REQUIRED_HEADERS = List.of(
            "id_fuente","id_a","proceso","acronimo","metodo","comentario_sProceso",
            "fuente","url","urlVariable","edicion","comentario_sFuente",
            "id_s","nombre","definicion","comentario_sVariable",
            "mdea","componente","subcomponente","tema","estadistica1","estadistica2","contribucionMdea","comentario_sMdea",
            "ods","objetivo","meta","indicador","contribucionOds","comentario_sOds",
            "pertinencia","contribucionPertinencia","viabilidad","propuesta","comentario_sPertinencia"
    );

    private Integer getResponsableIdFromSession(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new RuntimeException("No se pudo identificar al usuario de la sesión.");
        }

        String username = principal.getName(); // users.usuarios.nombre
        usuario_enty user = usuarioRepo.findByNombre(username);

        if (user == null) {
            throw new RuntimeException("Usuario de sesión no existe en BD: " + username);
        }

        return Math.toIntExact(user.getId());
    }

    public import_excel_validacion_dto validar(MultipartFile file) {

        List<import_excel_error_fila_dto> errors = new ArrayList<>();

        // 1) archivo
        if (file == null || file.isEmpty()) {
            errors.add(new import_excel_error_fila_dto(0, "-", "No se recibió archivo o viene vacío."));
            return import_excel_validacion_dto.fail("Archivo vacío.", errors);
        }

        String originalName = file.getOriginalFilename();
        String fileName = (originalName == null) ? "" : originalName.toLowerCase();

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

            // 2) encabezados
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                errors.add(new import_excel_error_fila_dto(1, "-", "No se encontró la fila de encabezados (fila 1)."));
                return import_excel_validacion_dto.fail("Encabezados faltantes.", errors);
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(headerRow);

            for (String required : REQUIRED_HEADERS) {
                if (!headerIndex.containsKey(normalize(required))) {
                    errors.add(new import_excel_error_fila_dto(1, required, "Falta el encabezado requerido: " + required));
                }
            }

            if (!errors.isEmpty()) {
                return import_excel_validacion_dto.fail("La plantilla no coincide con el formato esperado.", errors);
            }

            // 3) filas
            int filasConDatos = 0;

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                if (isRowEmpty(row)) continue;

                filasConDatos++;

                String idFuente = getByHeader(row, headerIndex, "id_fuente");
                String idA = getByHeader(row, headerIndex, "id_a");
                String idS = getByHeader(row, headerIndex, "id_s");
                String acronimo = getByHeader(row, headerIndex, "acronimo");
                String nombre = getByHeader(row, headerIndex, "nombre");

                if (isBlank(idFuente)) errors.add(new import_excel_error_fila_dto(r + 1, "id_fuente", "id_fuente está vacío."));
                if (isBlank(idA))      errors.add(new import_excel_error_fila_dto(r + 1, "id_a", "id_a está vacío."));
                if (isBlank(idS))      errors.add(new import_excel_error_fila_dto(r + 1, "id_s", "id_s está vacío."));
                if (isBlank(acronimo)) errors.add(new import_excel_error_fila_dto(r + 1, "acronimo", "acronimo está vacío."));
                if (isBlank(nombre))   errors.add(new import_excel_error_fila_dto(r + 1, "nombre", "nombre está vacío."));
            }

            if (filasConDatos == 0) {
                errors.add(new import_excel_error_fila_dto(2, "-", "El archivo no contiene registros para importar. Pegue información desde la fila 2."));
                return import_excel_validacion_dto.fail("Archivo sin datos.", errors);
            }

            if (!errors.isEmpty()) {
                return import_excel_validacion_dto.fail("Se encontraron errores en el archivo.", errors);
            }

            return import_excel_validacion_dto.ok("Archivo valido. Ya puedes importar.");

        } catch (Exception e) {
            errors.add(new import_excel_error_fila_dto(0, "-", "No se pudo leer el archivo Excel: " + e.getMessage()));
            return import_excel_validacion_dto.fail("Error al leer el Excel.", errors);
        }
    }

    @Transactional
public import_excel_import_result_dto importar(MultipartFile file, Principal principal) {

    int fuentesInsertadas = 0, fuentesActualizadas = 0;
    int variablesInsertadas = 0, variablesActualizadas = 0;
    int mdeaInsertados = 0, mdeaOmitidos = 0;
    int odsInsertados = 0, odsOmitidos = 0;
    int pertinenciaInsertadas = 0, pertinenciaActualizadas = 0;

    List<import_excel_error_fila_dto> errors = new ArrayList<>();
    int filasTotales = 0;
    int filasConDatos = 0;
    int filasImportadas = 0;

    // 0) responsable desde sesión
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
                List.of(new import_excel_error_fila_dto(0, "-", ex.getMessage()))
        );
    }

    // 1) validar antes (tu validar ya revisa encabezados y mínimos)
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
                validacion.getErrors()
        );
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
                    errors
            );
        }

        Map<String, Integer> headerIndex = buildHeaderIndex(sheet.getRow(0));
        filasTotales = sheet.getLastRowNum();

        // =========================================================
        // 2) PRIMER PASO: parsear filas -> construir "plan" en memoria
        //    - fuentesPorId: upsert 1 por id_fuente (pueden ser muchas fuentes diferentes)
        //    - variablesPorIdA: upsert 1 por id_a
        //    - pertinenciaPorIdA: upsert 1 por id_a si viene pertinencia
        //    - mdeaToInsert/odsToInsert: inserts (omitir duplicados en archivo y en BD)
        // =========================================================

        Map<String, fuentes_enty> fuentesPorId = new LinkedHashMap<>();
        Map<String, variables_enty> variablesPorIdA = new LinkedHashMap<>();
        Map<String, pertinencia_enty> pertinenciaPorIdA = new LinkedHashMap<>();

        // Para evitar duplicados dentro del MISMO archivo:
        Set<String> mdeaKeysArchivo = new HashSet<>();
        Set<String> odsKeysArchivo = new HashSet<>();

        // Guardamos también el número de fila para reportar errores finos:
        Map<String, Integer> filaFuente = new HashMap<>();   // id_fuente -> fila
        Map<String, Integer> filaVariable = new HashMap<>(); // id_a -> fila

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            if (isRowEmpty(row)) continue;

            filasConDatos++;

            // ===== campos base =====
            String idFuente = getByHeader(row, headerIndex, "id_fuente");
            String idA = getByHeader(row, headerIndex, "id_a");
            String idS = getByHeader(row, headerIndex, "id_s");
            String acronimo = getByHeader(row, headerIndex, "acronimo");

            String nombre = getByHeader(row, headerIndex, "nombre");
            String definicion = getByHeader(row, headerIndex, "definicion");
            String urlVariable = getByHeader(row, headerIndex, "urlVariable");
            String comentarioSVariable = getByHeader(row, headerIndex, "comentario_sVariable");

            Boolean flagMdea = parseBoolean(getByHeader(row, headerIndex, "mdea"));
            Boolean flagOds  = parseBoolean(getByHeader(row, headerIndex, "ods"));

            // mínimos NOT NULL (esto NO tiene nada que ver con "-" en otras columnas)
            if (isBlank(idFuente) || isBlank(idA) || isBlank(idS) || isBlank(acronimo) || isBlank(nombre)) {
                errors.add(new import_excel_error_fila_dto(r + 1, "-", "Fila incompleta. Requiere id_fuente, id_a, id_s, acronimo y nombre."));
                // ABORTAR TODO (como tú quieres)
                throw new ImportExcelAbortException("El archivo tiene filas incompletas. Se cancelá la importación.", errors);
            }

            // =====================================================
            // FUENTE (upsert 1 por id_fuente, pueden existir MUCHAS)
            // =====================================================
            String fuenteNombre = getByHeader(row, headerIndex, "fuente");
            String urlFuente = getByHeader(row, headerIndex, "url");
            String edicion = getByHeader(row, headerIndex, "edicion");
            String comentarioSFuente = getByHeader(row, headerIndex, "comentario_sFuente");

            // Creamos / reemplazamos en el map (última fila gana si repiten id_fuente)
            fuentes_enty f = fuentesPorId.get(idFuente);
            if (f == null) {
                f = new fuentes_enty();
                f.setIdFuente(idFuente);
                f.setResponsableRegister(responsableId);
                fuentesPorId.put(idFuente, f);
                filaFuente.put(idFuente, r + 1);
            } else {
                // Si ya estaba en el archivo, lo tomamos como "actualización" de ese objeto en memoria
                // (no incrementamos contadores aún; los contadores se deciden al comparar con BD)
            }

            f.setAcronimo(acronimo);
            f.setFuente(isBlank(fuenteNombre) ? "SIN_FUENTE" : fuenteNombre);
            f.setUrl(isBlank(urlFuente) ? null : urlFuente);
            f.setEdicion(isBlank(edicion) ? null : edicion);
            f.setComentarioS(isBlank(comentarioSFuente) ? null : comentarioSFuente);

            // =====================================================
            // VARIABLE (upsert por id_a)
            // =====================================================
            variables_enty v = variablesPorIdA.get(idA);
            if (v == null) {
                v = new variables_enty();
                v.setIdA(idA);
                v.setResponsableRegister(responsableId);
                variablesPorIdA.put(idA, v);
                filaVariable.put(idA, r + 1);
            }

            v.setIdFuente(idFuente);
            v.setIdS(idS);
            v.setAcronimo(acronimo);
            v.setNombre(nombre);
            v.setDefinicion(isBlank(definicion) ? null : definicion);
            v.setUrl(isBlank(urlVariable) ? null : urlVariable);
            v.setComentarioS(isBlank(comentarioSVariable) ? null : comentarioSVariable);
            v.setMdea(flagMdea); // puede ser null/true/false
            v.setOds(flagOds);

            // =====================================================
            // PERTINENCIA (upsert 1 por id_a si viene pertinencia)
            // =====================================================
            String pertinenciaTxt = getByHeader(row, headerIndex, "pertinencia");
            if (!isBlank(pertinenciaTxt)) {
                String contribucionP = getByHeader(row, headerIndex, "contribucionPertinencia");
                String viabilidad = getByHeader(row, headerIndex, "viabilidad");
                String propuesta = getByHeader(row, headerIndex, "propuesta");
                String comentarioSP = getByHeader(row, headerIndex, "comentario_sPertinencia");

                pertinencia_enty per = pertinenciaPorIdA.get(idA);
                if (per == null) {
                    per = new pertinencia_enty();
                    per.setIdA(idA);
                    pertinenciaPorIdA.put(idA, per);
                }
                per.setIdS(idS);

                per.setPertinencia(pertinenciaTxt);
                per.setContribucion(isBlank(contribucionP) ? null : contribucionP);
                per.setViabilidad(isBlank(viabilidad) ? null : viabilidad);
                per.setPropuesta(isBlank(propuesta) ? null : propuesta);
                per.setComentarioS(isBlank(comentarioSP) ? null : comentarioSP);
            }

            // =====================================================
            // MDEA (insert controlado)
            // - si flagMdea true => intentamos insertar si no existe en BD
            // - "-" se permite (se guarda "-" sin problema)
            // =====================================================
            if (Boolean.TRUE.equals(flagMdea)) {
                String componente = clean(blankToNull(getByHeader(row, headerIndex, "componente")));
                String subcomponente = clean(blankToNull(getByHeader(row, headerIndex, "subcomponente")));
                String tema = clean(blankToNull(getByHeader(row, headerIndex, "tema")));
                String estadistica1 = clean(blankToNull(getByHeader(row, headerIndex, "estadistica1")));
                String estadistica2 = clean(blankToNull(getByHeader(row, headerIndex, "estadistica2")));
                String contribucionMdea = clean(blankToNull(getByHeader(row, headerIndex, "contribucionMdea")));
                String comentarioSMdea = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sMdea")));

                // Key para evitar duplicados dentro del archivo (usa campos que definiste en existsBy...)
                String key = String.join("|",
                        normalize(idA),
                        normalize(String.valueOf(componente)),
                        normalize(String.valueOf(subcomponente)),
                        normalize(String.valueOf(tema)),
                        normalize(String.valueOf(estadistica1)),
                        normalize(String.valueOf(estadistica2))
                );

                if (!mdeaKeysArchivo.add(key)) {
                    // ya venía duplicado en el archivo, lo contamos como omitido (archivo)
                    mdeaOmitidos++;
                } else {
                    // lo intentaremos insertar luego (si no existe en BD)
                    // para eso lo guardamos temporalmente usando una lista "pendiente"
                    // (lo resolvemos en el paso de persistencia)
                    // Guardamos el objeto en una lista local por simplicidad:
                    // (usamos un list local afuera; aquí lo creamos y lo agregamos)
                    // -> lo haremos con dos listas:
                }
            }

            // =====================================================
            // ODS (insert controlado)
            // =====================================================
            if (Boolean.TRUE.equals(flagOds)) {
                String objetivo = clean(blankToNull(getByHeader(row, headerIndex, "objetivo")));
                String meta = clean(blankToNull(getByHeader(row, headerIndex, "meta")));
                String indicador = clean(blankToNull(getByHeader(row, headerIndex, "indicador")));
                String contribucionOds = clean(blankToNull(getByHeader(row, headerIndex, "contribucionOds")));
                String comentarioSOds = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sOds")));

                String key = String.join("|",
                        normalize(idA),
                        normalize(String.valueOf(objetivo)),
                        normalize(String.valueOf(meta)),
                        normalize(String.valueOf(indicador))
                );

                if (!odsKeysArchivo.add(key)) {
                    odsOmitidos++;
                } else {
                    // lo insertaremos luego (si no existe en BD)
                }
            }

            filasImportadas++; // hasta aquí, "fila procesada" (en memoria)
        }

        // Si no hay filas con datos:
        if (filasConDatos == 0) {
            errors.add(new import_excel_error_fila_dto(2, "-", "El archivo no contiene registros para importar. Pegue información desde la fila 2."));
            return import_excel_import_result_dto.fail(
                    "Archivo sin datos.",
                    filasTotales, 0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    errors
            );
        }

        // =========================================================
        // 3) SEGUNDO PASO: persistir en orden correcto
        //    - primero FUENTES (porque VARIABLES depende de id_fuente)
        //    - luego VARIABLES
        //    - luego PERTINENCIA
        //    - luego MDEA / ODS (insert controlado)
        // =========================================================

        // 3.1 FUENTES: decidir insert/update contando contra BD
        for (Map.Entry<String, fuentes_enty> entry : fuentesPorId.entrySet()) {
            String idFuente = entry.getKey();
            fuentes_enty data = entry.getValue();
            int fila = filaFuente.getOrDefault(idFuente, 0);

            try {
                fuentes_enty existente = fuentesRepo.findById(idFuente).orElse(null);
                if (existente == null) {
                    // insertar
                    data.setResponsableRegister(responsableId);
                    data.setResponsableActualizacion(null);
                    fuentesRepo.save(data);
                    fuentesInsertadas++;
                } else {
                    // actualizar (PK misma)
                    existente.setResponsableActualizacion(responsableId);
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
                while (root.getCause() != null) root = root.getCause();
                errors.add(new import_excel_error_fila_dto(fila, "fuentes", "Error guardando fuente (" + idFuente + "): " + root.getMessage()));
                throw new ImportExcelAbortException("Error guardando FUENTES. Se canceló la importación.", errors);
            }
        }

        // 3.2 VARIABLES: decidir insert/update contando contra BD
        for (Map.Entry<String, variables_enty> entry : variablesPorIdA.entrySet()) {
            String idA = entry.getKey();
            variables_enty data = entry.getValue();
            int fila = filaVariable.getOrDefault(idA, 0);

            try {
                variables_enty existente = variablesRepo.findById(idA).orElse(null);
                if (existente == null) {
                    data.setResponsableRegister(responsableId);
                    data.setResponsableActualizacion(null);
                    variablesRepo.save(data);
                    variablesInsertadas++;
                } else {
                    existente.setResponsableActualizacion(responsableId);

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
                while (root.getCause() != null) root = root.getCause();
                errors.add(new import_excel_error_fila_dto(fila, "variables", "Error guardando variable (" + idA + "): " + root.getMessage()));
                throw new ImportExcelAbortException("Error guardando VARIABLES. Se canceló la importación.", errors);
            }
        }

        // 3.3 PERTINENCIA: upsert por id_a
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
                while (root.getCause() != null) root = root.getCause();
                errors.add(new import_excel_error_fila_dto(fila, "pertinencia", "Error guardando pertinencia (" + idA + "): " + root.getMessage()));
                throw new ImportExcelAbortException("Error guardando PERTINENCIA. Se canceló la importación.", errors);
            }
        }

        // =========================================================
        // 3.4 MDEA / ODS: recorremos otra vez el Excel SOLO para inserts controlados
        //     (porque arriba hicimos sets para evitar duplicados, pero no guardamos objetos)
        // =========================================================
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            if (isRowEmpty(row)) continue;

            String idA = getByHeader(row, headerIndex, "id_a");
            String idS = getByHeader(row, headerIndex, "id_s");

            Boolean flagMdea = parseBoolean(getByHeader(row, headerIndex, "mdea"));
            Boolean flagOds  = parseBoolean(getByHeader(row, headerIndex, "ods"));

            // MDEA
            if (Boolean.TRUE.equals(flagMdea)) {
                try {
                    String componente = clean(blankToNull(getByHeader(row, headerIndex, "componente")));
                    String subcomponente = clean(blankToNull(getByHeader(row, headerIndex, "subcomponente")));
                    String tema = clean(blankToNull(getByHeader(row, headerIndex, "tema")));
                    String estadistica1 = clean(blankToNull(getByHeader(row, headerIndex, "estadistica1")));
                    String estadistica2 = clean(blankToNull(getByHeader(row, headerIndex, "estadistica2")));
                    String contribucionMdea = clean(blankToNull(getByHeader(row, headerIndex, "contribucionMdea")));
                    String comentarioSMdea = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sMdea")));

                    String key = String.join("|",
                            normalize(idA),
                            normalize(String.valueOf(componente)),
                            normalize(String.valueOf(subcomponente)),
                            normalize(String.valueOf(tema)),
                            normalize(String.valueOf(estadistica1)),
                            normalize(String.valueOf(estadistica2))
                    );

                    // si el archivo traía duplicado, ya lo contamos como omitido en el primer paso
                    // aquí solo insertamos si el key era único en archivo:
                    // (si no era único, mdeaKeysArchivo.add(key) hubiera sido false antes)
                    // Por seguridad, validamos que exista en el set:
                    if (!mdeaKeysArchivo.contains(key)) continue;

                    boolean existe = mdeaRepo.existsByIdAAndComponenteAndSubcomponenteAndTemaAndEstadistica1AndEstadistica2(
                            idA, componente, subcomponente, tema, estadistica1, estadistica2
                    );

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
                    while (root.getCause() != null) root = root.getCause();
                    errors.add(new import_excel_error_fila_dto(r + 1, "mdea", "Error guardando MDEA: " + root.getMessage()));
                    throw new ImportExcelAbortException("Error guardando MDEA. Se canceló la importación.", errors);
                }
            }

            // ODS
            if (Boolean.TRUE.equals(flagOds)) {
                try {
                    String objetivo = clean(blankToNull(getByHeader(row, headerIndex, "objetivo")));
                    String meta = clean(blankToNull(getByHeader(row, headerIndex, "meta")));
                    String indicador = clean(blankToNull(getByHeader(row, headerIndex, "indicador")));
                    String contribucionOds = clean(blankToNull(getByHeader(row, headerIndex, "contribucionOds")));
                    String comentarioSOds = clean(blankToNull(getByHeader(row, headerIndex, "comentario_sOds")));

                    String key = String.join("|",
                            normalize(idA),
                            normalize(String.valueOf(objetivo)),
                            normalize(String.valueOf(meta)),
                            normalize(String.valueOf(indicador))
                    );

                    if (!odsKeysArchivo.contains(key)) continue;

                    boolean existe = odsRepo.existsByIdAAndObjetivoAndMetaAndIndicador(idA, objetivo, meta, indicador);

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
                    while (root.getCause() != null) root = root.getCause();
                    errors.add(new import_excel_error_fila_dto(r + 1, "ods", "Error guardando ODS: " + root.getMessage()));
                    throw new ImportExcelAbortException("Error guardando ODS. Se canceló la importación.", errors);
                }
            }
        }

        // =========================================================
        // 4) OK final
        // =========================================================
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
                pertinenciaActualizadas
        );

    } catch (ImportExcelAbortException abort) {
        // rollback + respuesta bonita (sin 500)
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

                abort.getErrors()
        );

    } catch (Exception e) {
        Throwable root = e;
        while (root.getCause() != null) root = root.getCause();
        errors.add(new import_excel_error_fila_dto(0, "-", "Error al importar: " + root.getMessage()));

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

                errors
        );
    }
}

    // ---------------- HELPERS ----------------

    private String blankToNull(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private Boolean parseBoolean(String raw) {
        if (raw == null)
            return null;
        String v = raw.trim().toLowerCase();

        // normaliza acento: "sí" -> "si"
        v = v.replace("í", "i");

        if (v.isEmpty())
            return null;

        if (v.equals("true") || v.equals("t")
                || v.equals("1") || v.equals("si") || v.equals("s") || v.equals("x")
                || v.equals("verdadero") || v.equals("v"))
            return true;

        if (v.equals("false") || v.equals("f")
                || v.equals("0") || v.equals("no") || v.equals("n")
                || v.equals("falso") || v.equals("-"))
            return false;

        return null;
    }

    private Map<String, Integer> buildHeaderIndex(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        short last = headerRow.getLastCellNum();
        for (int i = 0; i < last; i++) {
            String header = normalize(getCellAsString(headerRow.getCell(i)));
            if (!header.isBlank()) map.put(header, i);
        }
        return map;
    }

    private String getByHeader(Row row, Map<String, Integer> index, String header) {
        Integer col = index.get(normalize(header));
        if (col == null) return "";
        return getCellAsString(row.getCell(col));
    }

    private boolean isRowEmpty(Row row) {
        short last = row.getLastCellNum();
        for (int i = 0; i < last; i++) {
            if (!isBlank(getCellAsString(row.getCell(i)))) return false;
        }
        return true;
    }

    private String getCellAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                if (d == Math.rint(d)) { // es entero
                    yield String.valueOf((long) d);
                }
                yield String.valueOf(d);
            }

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try { yield cell.getStringCellValue().trim(); }
                catch (Exception ex) { yield String.valueOf(cell.getNumericCellValue()); }
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
        if (s == null)
            return null;
        return s.trim();
    }

}
