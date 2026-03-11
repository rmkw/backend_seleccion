package com.seleccion.backend.entities.import_excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class import_excel_import_result_dto {
    private boolean ok;
    private String message;

    private int filasTotales;
    private int filasConDatos;
    private int filasImportadas;
    private int filasConError;

    // --- NUEVO: resumen por tabla ---
    private int fuentesInsertadas;
    private int fuentesActualizadas;

    private int variablesInsertadas;
    private int variablesActualizadas;

    // En tu import actual MDEA/ODS no se actualizan: se insertan o se omiten por
    // duplicado
    private int mdeaInsertados;
    private int mdeaOmitidos;

    private int odsInsertados;
    private int odsOmitidos;

    private int pertinenciaInsertadas;
    private int pertinenciaActualizadas;

    private List<import_excel_error_fila_dto> errors = new ArrayList<>();

    // OK con resumen
    public static import_excel_import_result_dto ok(
            String msg,
            int tot,
            int conDatos,
            int importadas,

            int fuentesIns,
            int fuentesUpd,

            int varsIns,
            int varsUpd,

            int mdeaIns,
            int mdeaOmi,

            int odsIns,
            int odsOmi,

            int perIns,
            int perUpd) {
        return new import_excel_import_result_dto(
                true,
                msg,
                tot,
                conDatos,
                importadas,
                0,

                fuentesIns,
                fuentesUpd,

                varsIns,
                varsUpd,

                mdeaIns,
                mdeaOmi,

                odsIns,
                odsOmi,

                perIns,
                perUpd,

                new ArrayList<>());
    }

    // FAIL con resumen (aunque falle, es útil mostrar qué alcanzó a procesar)
    public static import_excel_import_result_dto fail(
            String msg,
            int tot,
            int conDatos,
            int importadas, // nuevo: cuántas sí se importaron antes de fallar

            int fuentesIns,
            int fuentesUpd,

            int varsIns,
            int varsUpd,

            int mdeaIns,
            int mdeaOmi,

            int odsIns,
            int odsOmi,

            int perIns,
            int perUpd,

            List<import_excel_error_fila_dto> errs) {
        return new import_excel_import_result_dto(
                false,
                msg,
                tot,
                conDatos,
                importadas,
                errs == null ? 0 : errs.size(),

                fuentesIns,
                fuentesUpd,

                varsIns,
                varsUpd,

                mdeaIns,
                mdeaOmi,

                odsIns,
                odsOmi,

                perIns,
                perUpd,

                errs == null ? new ArrayList<>() : errs);
    }
    
    // ------------------------------------
    // Overloads simples (compatibilidad)
    // ------------------------------------
    public static import_excel_import_result_dto fail(String msg, int tot, int conDatos,
            List<import_excel_error_fila_dto> errs) {
        return fail(
                msg,
                tot,
                conDatos,
                0, // importadas

                0, 0, // fuentes
                0, 0, // variables
                0, 0, // mdea
                0, 0, // ods
                0, 0, // pertinencia

                errs);
    }

    public static import_excel_import_result_dto ok(String msg, int tot, int conDatos, int importadas) {
        return ok(
                msg,
                tot,
                conDatos,
                importadas,

                0, 0, // fuentes
                0, 0, // variables
                0, 0, // mdea
                0, 0, // ods
                0, 0 // pertinencia
        );
    }

}
