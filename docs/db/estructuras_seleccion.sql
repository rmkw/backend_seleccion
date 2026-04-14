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