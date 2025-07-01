package com.seleccion.backend.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.seleccion.backend.entities.usuario.usuario_enty;
import com.seleccion.backend.repositories.usuario.usuario_repo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@Service
public class auth_services {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private usuario_repo repo;

    public usuario_enty login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Autenticación después de login: " + SecurityContextHolder.getContext().getAuthentication());

        return repo.findByNombre(username);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public usuario_enty getUsuarioAutenticado(HttpServletRequest request) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Cookie recibida: " + cookie.getName() + " = " + cookie.getValue());
            }     
        } else {
            System.out.println("No se recibieron cookies en la petición.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Autenticación actual: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("No hay usuario autenticado");
            return null;
        }

        String nombreUsuario = authentication.getName();
        System.out.println("Nombre autenticado: " + nombreUsuario);

        return repo.findByNombre(nombreUsuario);
    }
}
