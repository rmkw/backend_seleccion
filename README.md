# 🧩 Backend SIERNMA

Backend del sistema **SIERNMA**, desarrollado con Java y Spring Boot, encargado de la gestión de variables, usuarios y procesos relacionados con armonización de información.

---

## 🚀 Tecnologías utilizadas

* Java 17+
* Spring Boot
* Spring Security
* JPA / Hibernate
* PostgreSQL
* Maven

---

## 📁 Estructura del proyecto

```
src/
 ├── main/
 │    ├── java/
 │    │    └── com.seleccion.backend/
 │    │         ├── controllers/
 │    │         ├── services/
 │    │         ├── repositories/
 │    │         ├── entities/
 │    │         └── config/
 │    └── resources/
 │         ├── application.properties
 │         └── db/
 │              └── schema.sql
```

---

## ⚙️ Configuración del entorno

### 🔐 Variables sensibles

El archivo `application.properties` **NO debe subirse al repositorio**.

Usar:

```
src/main/resources/application-example.properties
```

Copiar y renombrar:

```
application-example.properties → application.properties
```

Y configurar:

```properties
spring.datasource.url=jdbc:postgresql://<HOST>:<PUERTO>/<BD>
spring.datasource.username=<USUARIO>
spring.datasource.password=<PASSWORD>
```

---

## 🗄️ Base de datos

El proyecto utiliza PostgreSQL con esquema principal:

```
usuarios
```

### Tablas principales

* `usuarios.usuarios`
* `usuarios.usuarios_roles`

### Relación

* Un usuario puede tener múltiples roles
* Los roles se almacenan como colección (`@ElementCollection`)

---

## ▶️ Ejecución del proyecto

### 1. Clonar repositorio

```
git clone <URL_DEL_REPO>
cd backend
```

### 2. Configurar propiedades

Crear `application.properties` con tus credenciales

### 3. Ejecutar

```
mvn spring-boot:run
```

---

## 🔌 Endpoints (ejemplo)

### Crear usuario

```
POST /api/usuarios
```

**Body (JSON):**

```json
{
  "nombre": "admin",
  "aka": "root",
  "contrasena": "123456",
  "roles": ["ADMIN", "USER"]
}
```

---

## 🧪 Logs y debugging

El proyecto tiene habilitados logs para:

* Seguridad
* SQL
* Hibernate

```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 📚 Documentación adicional

```
/docs/
 └── db/
      └── schema.sql
```

Contiene la estructura de la base de datos y scripts relacionados.

---

## ⚠️ Notas importantes

* `spring.jpa.hibernate.ddl-auto=none`
  → La BD **no se genera automáticamente**

* Es necesario mantener sincronizada la estructura de la BD con las entidades

---

## 👨‍💻 Autor

Desarrollado por el equipo SIERNMA - INEGI

---

## 📌 Estado del proyecto

En desarrollo 🚧
