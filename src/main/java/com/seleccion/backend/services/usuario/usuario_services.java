package com.seleccion.backend.services.usuario;

import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.usuario.usuario_enty;
import com.seleccion.backend.repositories.usuario.usuario_repo;

import java.util.HashSet;
import java.util.List;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class usuario_services {
    
    @Autowired
    private usuario_repo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<usuario_enty> getAllUsuarios() {
        return repo.findAll();
    }

    public usuario_enty getUsuarioById(Long id){
        return repo.findById(id).orElse(null);
    }

    public usuario_enty registrarUsuario(usuario_enty usuario) {
        if (repo.findByNombre(usuario.getNombre()) != null) {
            throw new RuntimeException("El nombre de usuario ya existe");
            
        }

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        Set<String> roles = usuario.getRoles();
        if (roles == null) {
            roles = new HashSet<>(); // Inicializarlo si es null
        }

        roles.add("USER"); // Agregar el rol USER por defecto
        usuario.setRoles(roles); // Establecer la lista de roles en el usuario

        return repo.save(usuario);
    }
}
