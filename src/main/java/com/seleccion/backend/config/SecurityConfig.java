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
                                                                "/api/armo/variables/**",
                                                                        "/api/pertinencia/**",
                                                        "/api/fuentes/**",
                                                        "/api/variables/**",
                                                        "/api/mdea/**",
                                                        "/api/ods/**",
                                                        "/api/procesos-locales/**",
                                                                "/api/usuarios/**","/api/catalog/**",
                                                                        "/api/unidad/**",
                                                        "/api/procesos/**",
                                                                "/api/sele/**",
                                                                "/api/armo/**",
                                                                "/api/armo/fuentes/**").permitAll()
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
                                        );
                                        

                return http.build();
        }

        
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(); // Encriptar contraseñas con BCrypt
        }

        
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of(
                                "http://localhost:4200",
                                "http://127.0.0.1:4200",
                                "http://10.200.130.27:8090",
                                "http://10.109.1.27:4200"
                                ));
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
