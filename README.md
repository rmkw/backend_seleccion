# 🧩 Backend SIERNMA

Backend del sistema **SIIERNMA (Sistema Integrado de Inventarios y Encuestas sobre Recursos Naturales y Medio Ambiente)**, orientado a la **gestión, caracterización y armonización de variables** provenientes de procesos de producción estadística y geográfica del INEGI.

---

## 🎯 Descripción del proyecto

Este sistema implementa una plataforma backend para el **Inventario de Variables del SIIERNMA**, cuyo objetivo es:

* Estandarizar la información de variables
* Garantizar su **trazabilidad** hacia procesos de producción
* Facilitar su **comparabilidad y reutilización**
* Integrar su relación con:

  * fuentes documentales
  * productos de difusión (tabulados, microdatos, datos abiertos)
  * marcos de referencia internacionales (MDEA y ODS)

La base de datos está diseñada bajo un enfoque **relacional normalizado**, permitiendo organizar la información en componentes especializados y mantener la integridad de los datos.

---

## 🧠 Modelo conceptual

El sistema se basa en una relación central:

```id="t6x0f4"
Procesos → Fuentes → Variables
```

A partir de esta relación se derivan múltiples conexiones que permiten enriquecer la información de cada variable:

* Clasificaciones
* Tabulados
* Microdatos
* Datos abiertos
* Alineación con MDEA
* Alineación con ODS
* Evaluación de pertinencia

---

## 🗄️ Estructura de la base de datos

La base de datos está compuesta por **13 tablas normalizadas**, agrupadas en:

### 🔹 Procesos y fuentes

* `procesos`
* `fuentes`

### 🔹 Variables y caracterización

* `variables`
* `clasificaciones`
* `pertinencia`

### 🔹 Difusión de información

* `tabulados`
* `desagregacion`
* `desglose`
* `variables_tabulados`
* `microdatos`
* `datos_abiertos`

### 🔹 Marcos de referencia

* `mdea`
* `ods`

---

## 🔗 Tipos de relaciones

* **1:1** → Variable → Pertinencia
* **1:N** → Variable → Clasificaciones, ODS, MDEA
* **N:M** → Variables ↔ Tabulados (tabla puente)

---

## 🚀 Tecnologías utilizadas

* Java 17+
* Spring Boot
* Spring Security
* JPA / Hibernate
* PostgreSQL
* Maven

---

## ⚙️ Configuración del entorno

### 🔐 Seguridad

El archivo:

```id="7xv8bp"
application.properties
```

NO debe subirse al repositorio.

Usar:

```id="g3m0ys"
application-example.properties
```

---

## ▶️ Ejecución

```id="c0r9qg"
mvn spring-boot:run
```

---

## 🔌 Ejemplo de endpoint

### Crear usuario

```id="m9u6cw"
POST /api/usuarios
```

```json id="0m1lqz"
{
  "nombre": "admin",
  "aka": "root",
  "contrasena": "123456",
  "roles": ["ADMIN", "USER"]
}
```

---

## 📚 Documentación

```id="r3v9yf"
/docs/
 └── db/
      └── modelo.sql
```

Contiene:

* estructura de la base de datos
* relaciones
* scripts de creación

---

## ⚠️ Consideraciones importantes

* La base de datos **no se genera automáticamente**:

```id="d5r2mz"
spring.jpa.hibernate.ddl-auto=none
```

* Es responsabilidad del desarrollador mantener sincronía entre:

  * entidades JPA
  * estructura de la BD

* Las URLs de fuentes pueden cambiar, por lo que deben validarse periódicamente.

---

## 🧭 Alcance del sistema

El sistema permite:

* Registrar variables desde múltiples procesos
* Mantener trazabilidad documental
* Consultar disponibilidad en productos de difusión
* Relacionar variables con estándares internacionales
* Evaluar su pertinencia para análisis ambiental

---

## 👨‍💻 Autor

Proyecto desarrollado en el contexto del INEGI para la gestión del Inventario de Variables del SIIERNMA.

---

## 📌 Estado

En desarrollo 🚧
