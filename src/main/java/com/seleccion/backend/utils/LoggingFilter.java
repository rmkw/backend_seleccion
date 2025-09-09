package com.seleccion.backend.utils;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.*;

import org.springframework.stereotype.Component;



@Component
public class LoggingFilter implements Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("Petición recibida: " + req.getMethod() + " " + req.getRequestURI());
        chain.doFilter(request, response);
    }

}
