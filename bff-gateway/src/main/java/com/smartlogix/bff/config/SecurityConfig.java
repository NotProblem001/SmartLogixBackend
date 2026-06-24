package com.smartlogix.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * RF-B02 y RNF-S01: Gestión de Seguridad, CORS y Autenticación en el Gateway.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilitado para APIs REST/Microservicios
            .cors(org.springframework.security.config.Customizer.withDefaults())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**", "/fallback/**").permitAll() // Rutas públicas locales
                .anyExchange().permitAll() // Permitir paso libre para que el filtro JwtValidationFilter gestione las rutas
            );
        
        return http.build();
    }
}
