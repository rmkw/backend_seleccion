-- Tabla principal de usuarios
CREATE TABLE usuarios.usuarios (
    id SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL,
    contrasena TEXT NOT NULL,
    aka TEXT
);

-- Tabla de roles (colección)
CREATE TABLE usuarios.usuarios_roles (
    usuario_id INT NOT NULL,
    rol TEXT NOT NULL,
    PRIMARY KEY (usuario_id, rol),
    CONSTRAINT fk_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios.usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- ----------------------------
-- Table structure for mdea_temas
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."mdea_temas";
CREATE TABLE "catalogos"."mdea_temas" (
  "unique_id" int4 NOT NULL DEFAULT nextval('"catalogos".mdea_temas_unique_id_seq'::regclass),
  "id_componente" int4 NOT NULL,
  "id_subcomponente" int4 NOT NULL,
  "id_tema" int4 NOT NULL,
  "nombre" text NOT NULL,
  "definicion" text NOT NULL
)
;

-- ----------------------------
-- Uniques structure for table mdea_temas
-- ----------------------------
ALTER TABLE "catalogos"."mdea_temas" ADD CONSTRAINT "uq_topicos_comp_sub_top" UNIQUE ("id_componente", "id_subcomponente", "id_tema");

-- ----------------------------
-- Primary Key structure for table mdea_temas
-- ----------------------------
ALTER TABLE "catalogos"."mdea_temas" ADD CONSTRAINT "mdea_temas_pkey" PRIMARY KEY ("unique_id");

-- ----------------------------
-- Foreign Keys structure for table mdea_temas
-- ----------------------------
ALTER TABLE "catalogos"."mdea_temas" ADD CONSTRAINT "fk_subcomponentes_temas" FOREIGN KEY ("id_componente", "id_subcomponente") REFERENCES "catalogos"."mdea_subcomponentes" ("id_componente", "id_subcomponente") ON DELETE NO ACTION ON UPDATE NO ACTION;





-- ----------------------------
-- Table structure for mdea_estadisticos1
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."mdea_estadisticos1";
CREATE TABLE "catalogos"."mdea_estadisticos1" (
  "unique_id" text NOT NULL,
  "id_componente" int4 NOT NULL,
  "id_subcomponente" int4 NOT NULL,
  "id_tema" int4 NOT NULL,
  "id_estadistico1" text NOT NULL,
  "nombre" text NOT NULL
)
;

-- ----------------------------
-- Uniques structure for table mdea_estadisticos1
-- ----------------------------
ALTER TABLE "catalogos"."mdea_estadisticos1" ADD CONSTRAINT "uq_variables_comp_sub_tema_estadistico2" UNIQUE ("id_componente", "id_subcomponente", "id_tema", "id_estadistico1");

-- ----------------------------
-- Primary Key structure for table mdea_estadisticos1
-- ----------------------------
ALTER TABLE "catalogos"."mdea_estadisticos1" ADD CONSTRAINT "mdea_estadisticos1_pkey" PRIMARY KEY ("unique_id");

-- ----------------------------
-- Foreign Keys structure for table mdea_estadisticos1
-- ----------------------------
ALTER TABLE "catalogos"."mdea_estadisticos1" ADD CONSTRAINT "fk_variables_temas" FOREIGN KEY ("id_componente", "id_subcomponente", "id_tema") REFERENCES "catalogos"."mdea_temas" ("id_componente", "id_subcomponente", "id_tema") ON DELETE NO ACTION ON UPDATE NO ACTION;





-- ----------------------------
-- Table structure for mdea_estadisticos2
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."mdea_estadisticos2";
CREATE TABLE "catalogos"."mdea_estadisticos2" (
  "unique_id" text NOT NULL,
  "id_componente" int4 NOT NULL,
  "id_subcomponente" int4 NOT NULL,
  "id_tema" int4 NOT NULL,
  "id_estadistico1" text NOT NULL,
  "id_estadistico2" int4 NOT NULL,
  "nombre" text NOT NULL
)
;

-- ----------------------------
-- Primary Key structure for table mdea_estadisticos2
-- ----------------------------
ALTER TABLE "catalogos"."mdea_estadisticos2" ADD CONSTRAINT "mdea_estadisticos2_pkey" PRIMARY KEY ("unique_id");

-- ----------------------------
-- Foreign Keys structure for table mdea_estadisticos2
-- ----------------------------
ALTER TABLE "catalogos"."mdea_estadisticos2" ADD CONSTRAINT "fk_estadisticos_variables" FOREIGN KEY ("id_componente", "id_subcomponente", "id_tema", "id_estadistico1") REFERENCES "catalogos"."mdea_estadisticos1" ("id_componente", "id_subcomponente", "id_tema", "id_estadistico1") ON DELETE NO ACTION ON UPDATE NO ACTION;


-- ----------------------------
-- Table structure for ods_indicador
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."ods_indicador";
CREATE TABLE "catalogos"."ods_indicador" (
  "id_objetivo" int4 NOT NULL,
  "id_meta" text NOT NULL,
  "id_indicador" int4 NOT NULL,
  "indicador" text NOT NULL,
  "unique_id" text
)
;

-- ----------------------------
-- Primary Key structure for table ods_indicador
-- ----------------------------
ALTER TABLE "catalogos"."ods_indicador" ADD CONSTRAINT "ods_indicador_pkey" PRIMARY KEY ("id_objetivo", "id_meta", "id_indicador");

-- ----------------------------
-- Foreign Keys structure for table ods_indicador
-- ----------------------------
ALTER TABLE "catalogos"."ods_indicador" ADD CONSTRAINT "fk_indicador_meta" FOREIGN KEY ("id_objetivo", "id_meta") REFERENCES "catalogos"."ods_meta" ("id_objetivo", "id_meta") ON DELETE CASCADE ON UPDATE NO ACTION;



-- ----------------------------
-- Table structure for ods_objetivo
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."ods_objetivo";
CREATE TABLE "catalogos"."ods_objetivo" (
  "id_objetivo" int4 NOT NULL,
  "objetivo" text NOT NULL
)
;

-- ----------------------------
-- Primary Key structure for table ods_objetivo
-- ----------------------------
ALTER TABLE "catalogos"."ods_objetivo" ADD CONSTRAINT "ods_objetivo_pkey" PRIMARY KEY ("id_objetivo");



-- ----------------------------
-- Table structure for ods_meta
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."ods_meta";
CREATE TABLE "catalogos"."ods_meta" (
  "id_objetivo" int4 NOT NULL,
  "id_meta" text NOT NULL,
  "meta" text NOT NULL,
  "unique_id" text
)
;

-- ----------------------------
-- Primary Key structure for table ods_meta
-- ----------------------------
ALTER TABLE "catalogos"."ods_meta" ADD CONSTRAINT "ods_meta_pkey" PRIMARY KEY ("id_objetivo", "id_meta");

-- ----------------------------
-- Foreign Keys structure for table ods_meta
-- ----------------------------
ALTER TABLE "catalogos"."ods_meta" ADD CONSTRAINT "fk_meta_objetivo" FOREIGN KEY ("id_objetivo") REFERENCES "catalogos"."ods_objetivo" ("id_objetivo") ON DELETE CASCADE ON UPDATE NO ACTION;


-- ----------------------------
-- Table structure for ods_indicador
-- ----------------------------
DROP TABLE IF EXISTS "catalogos"."ods_indicador";
CREATE TABLE "catalogos"."ods_indicador" (
  "id_objetivo" int4 NOT NULL,
  "id_meta" text NOT NULL,
  "id_indicador" int4 NOT NULL,
  "indicador" text NOT NULL,
  "unique_id" text
)
;

-- ----------------------------
-- Primary Key structure for table ods_indicador
-- ----------------------------
ALTER TABLE "catalogos"."ods_indicador" ADD CONSTRAINT "ods_indicador_pkey" PRIMARY KEY ("id_objetivo", "id_meta", "id_indicador");

-- ----------------------------
-- Foreign Keys structure for table ods_indicador
-- ----------------------------
ALTER TABLE "catalogos"."ods_indicador" ADD CONSTRAINT "fk_indicador_meta" FOREIGN KEY ("id_objetivo", "id_meta") REFERENCES "catalogos"."ods_meta" ("id_objetivo", "id_meta") ON DELETE CASCADE ON UPDATE NO ACTION;



-- ----------------------------
-- Table structure for procesos
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."procesos";
CREATE TABLE "seleccion"."procesos" (
  "acronimo" text NOT NULL,
  "proceso" text,
  "metodo" text,
  "objetivo" text,
  "pobjeto" text,
  "uobservacion" text,
  "unidad" text,
  "periodicidad" text,
  "iin" varchar(2),
  "estatus" text,
  "ipi" bool NOT NULL,
  "inicio" text,
  "conclusion" text
)
;

-- ----------------------------
-- Primary Key structure for table procesos
-- ----------------------------
ALTER TABLE "seleccion"."procesos" ADD CONSTRAINT "procesos_pkey" PRIMARY KEY ("acronimo");

-- ----------------------------
-- Table structure for fuentes
-- ----------------------------
CREATE TABLE seleccion.fuentes (
    acronimo TEXT NOT NULL,
    fuente TEXT NOT NULL,
    url TEXT,
    edicion TEXT,
    comentario_s TEXT,
    comentario_a TEXT,
    responsable_register INTEGER NOT NULL,
    responsable_actualizacion INTEGER,
    id_fuente_seleccion TEXT,

    id_fuente TEXT GENERATED ALWAYS AS (
        acronimo || '-' || fuente || '-' || COALESCE(edicion, '') || '-' || COALESCE(url, '')
    ) STORED,

    CONSTRAINT pk_fuentes PRIMARY KEY (id_fuente),

    CONSTRAINT fk_fuentes_acronimo_to_procesos_acronimo
        FOREIGN KEY (acronimo)
        REFERENCES seleccion.procesos(acronimo)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ----------------------------
-- Table structure for variables
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."variables";
CREATE TABLE "seleccion"."variables" (
  "id_a" text NOT NULL,
  "id_s" text NOT NULL,
  "id_fuente" text NOT NULL,
  "acronimo" text NOT NULL,
  "nombre" text NOT NULL,
  "definicion" text,
  "url" text,
  "comentario_s" text,
  "mdea" bool,
  "ods" bool,
  "responsable_register" int4 NOT NULL,
  "responsable_actualizacion" int4,
  "prioridad" int2,
  "revisada" bool NOT NULL DEFAULT false,
  "fecha_revision" timestamp(6),
  "responsable_revision" int4
)
;

-- ----------------------------
-- Checks structure for table variables
-- ----------------------------
ALTER TABLE "seleccion"."variables" ADD CONSTRAINT "chk_variables_prioridad" CHECK ((prioridad = ANY (ARRAY[1, 2])) OR prioridad IS NULL);

-- ----------------------------
-- Primary Key structure for table variables
-- ----------------------------
ALTER TABLE "seleccion"."variables" ADD CONSTRAINT "variables_pkey" PRIMARY KEY ("id_a");

-- ----------------------------
-- Foreign Keys structure for table variables
-- ----------------------------
ALTER TABLE "seleccion"."variables" ADD CONSTRAINT "fk_proceso_variable" FOREIGN KEY ("acronimo") REFERENCES "seleccion"."procesos" ("acronimo") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "seleccion"."variables" ADD CONSTRAINT "variables_id_fuente_fkey" FOREIGN KEY ("id_fuente") REFERENCES "seleccion"."fuentes" ("id_fuente") ON DELETE CASCADE ON UPDATE NO ACTION;



-- ----------------------------
-- Table structure for comentarios_pp
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."comentarios_pp";
CREATE TABLE "seleccion"."comentarios_pp" (
  "acronimo" varchar(50) NOT NULL,
  "comentario_s" text NOT NULL
)
;

-- ----------------------------
-- Primary Key structure for table comentarios_pp
-- ----------------------------
ALTER TABLE "seleccion"."comentarios_pp" ADD CONSTRAINT "comentarios_procesos_pkey" PRIMARY KEY ("acronimo");

-- ----------------------------
-- Table structure for mdea
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."mdea";
CREATE TABLE "seleccion"."mdea" (
  "id_unique" int4 NOT NULL DEFAULT nextval('"seleccion".mdea_id_unique_seq'::regclass),
  "id_a" text,
  "id_s" text NOT NULL,
  "componente" text,
  "subcomponente" text,
  "tema" text,
  "estadistica1" text,
  "estadistica2" text,
  "contribucion" text,
  "comentario_s" text
)
;

-- ----------------------------
-- Uniques structure for table mdea
-- ----------------------------
ALTER TABLE "seleccion"."mdea" ADD CONSTRAINT "unique_mdea_relacion" UNIQUE ("id_a", "componente", "subcomponente", "tema", "estadistica1", "estadistica2");

-- ----------------------------
-- Primary Key structure for table mdea
-- ----------------------------
ALTER TABLE "seleccion"."mdea" ADD CONSTRAINT "mdea_pkey" PRIMARY KEY ("id_unique");


-- ----------------------------
-- Table structure for ods
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."ods";
CREATE TABLE "seleccion"."ods" (
  "id_unique" int4 NOT NULL DEFAULT nextval('"seleccion".ods_id_unique_seq'::regclass),
  "id_a" text NOT NULL,
  "id_s" text NOT NULL,
  "objetivo" text,
  "meta" text,
  "indicador" text,
  "contribucion" text,
  "comentario_s" text
)
;

-- ----------------------------
-- Uniques structure for table ods
-- ----------------------------
ALTER TABLE "seleccion"."ods" ADD CONSTRAINT "unique_ods_relacion" UNIQUE ("id_a", "objetivo", "meta", "indicador");

-- ----------------------------
-- Primary Key structure for table ods
-- ----------------------------
ALTER TABLE "seleccion"."ods" ADD CONSTRAINT "ods_pkey" PRIMARY KEY ("id_unique");


-- ----------------------------
-- Table structure for pertinencia
-- ----------------------------
DROP TABLE IF EXISTS "seleccion"."pertinencia";
CREATE TABLE "seleccion"."pertinencia" (
  "id_unique" int4 NOT NULL DEFAULT nextval('"seleccion".pertinencia_id_unique_seq'::regclass),
  "id_a" text NOT NULL,
  "pertinencia" text NOT NULL,
  "contribucion" text,
  "viabilidad" text,
  "propuesta" text,
  "comentario_s" text,
  "id_s" text
)
;

-- ----------------------------
-- Uniques structure for table pertinencia
-- ----------------------------
ALTER TABLE "seleccion"."pertinencia" ADD CONSTRAINT "unq_pertinencia_id_a" UNIQUE ("id_a");

-- ----------------------------
-- Primary Key structure for table pertinencia
-- ----------------------------
ALTER TABLE "seleccion"."pertinencia" ADD CONSTRAINT "pertinencia_pkey" PRIMARY KEY ("id_unique");



