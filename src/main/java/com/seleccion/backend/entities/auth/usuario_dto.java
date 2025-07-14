package com.seleccion.backend.entities.auth;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class usuario_dto {
    private Long id;
    private String nombre;
    private String aka;
    private Set<String> roles;
}
