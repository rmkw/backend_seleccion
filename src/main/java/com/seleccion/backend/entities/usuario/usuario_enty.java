package com.seleccion.backend.entities.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "users")
@Getter
@Setter
public class usuario_enty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String contrasena;

    @ElementCollection(fetch = FetchType.EAGER) // Guarda los roles como una colección
    @CollectionTable(
        name = "usuarios_roles", 
        joinColumns = @JoinColumn(name = "usuario_id"),
        schema = "users"
        )
    @Column(name = "rol")
    private Set<String> roles;
}
