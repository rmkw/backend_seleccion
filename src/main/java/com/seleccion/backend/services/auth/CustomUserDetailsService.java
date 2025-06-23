package com.seleccion.backend.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.usuario.usuario_enty;
import com.seleccion.backend.repositories.usuario.usuario_repo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private usuario_repo usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar al usuario por su nombre
        usuario_enty usuario = usuarioRepo.findByNombre(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Retorna un objeto User (que es una implementación de UserDetails)
        return User.builder()
                .username(usuario.getNombre())
                .password(usuario.getContrasena()) // Asegúrate de que la contraseña esté cifrada en la base de datos
                .roles(usuario.getRoles().toArray(new String[0])) // Asignar el rol al usuario
                .build();
    }
}
