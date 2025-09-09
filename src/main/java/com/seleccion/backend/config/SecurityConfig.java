package com.seleccion.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.seleccion.backend.services.auth.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        CustomUserDetailsService userDetailsService)
                        throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder.userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());

                return authenticationManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers( 
                                                        "/api/auth/**",
                                                        "/api/pertinencia/**",
                                                        "/api/fuentes/**",
                                                        "/api/variables/**",
                                                        "/api/mdea/**",
                                                        "/api/ods/**",
                                                        "/api/procesos-locales/**",
                                                        
                                                        "/api/unidad/**",
                                                        "/api/procesos/**").permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1) // Solo permite una sesión por usuario
                                                .maxSessionsPreventsLogin(false) // Permite que el usuario vuelva a
                                                                                 // iniciar sesión (expulsa la sesión
                                                                                 // anterior)
                                )
                                .securityContext(securityContext -> securityContext
                                        .requireExplicitSave(false)
                                        )
                                        .cors(cors -> cors.configurationSource(corsConfigurationSource())
                                        )
                                        .anonymous(anonymous -> anonymous.disable());

                return http.build();
        }

        // @Bean
        // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
        // Exception {
        // http
        // .csrf(csrf -> csrf.disable())
        // .authorizeHttpRequests(authz -> authz
        // .anyRequest().permitAll()); // Permitir todas las solicitudes sin
        // // autenticación ni autorización

        // return http.build();
        // }
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(); // Encriptar contraseñas con BCrypt
        }

        // @Bean
        // public CorsConfigurationSource corsConfigurationSource() {
        //         CorsConfiguration configuration = new CorsConfiguration();

        //         // Permitir solicitudes desde Angular y cualquier IP en la red 10.109.1.X:4200
        //         configuration.setAllowedOrigins(List.of(
        //                         "http://10.109.1.13:4200", // IP exacta de tu frontend
        //                         "http://localhost:4200",
        //                         "http://192.168.0.8:4200"));

        //         // Permitir todos los métodos HTTP
        //         configuration.setAllowedMethods(List.of("*"));

        //         // Permitir todas las cabeceras
        //         configuration.setAllowedHeaders(List.of("*"));

        //         // Permitir el uso de cookies (JSESSIONID)
        //         configuration.setAllowCredentials(true);

        //         // Aplicar esta configuración a todas las rutas
        //         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //         source.registerCorsConfiguration("/**", configuration);

        //         return source;
        // }
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.addAllowedOriginPattern("*");
                configuration.setAllowCredentials(true);

                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ incluye
                                                                                                     // OPTIONS
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setMaxAge(3600L); // cache preflight

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }


   
}
