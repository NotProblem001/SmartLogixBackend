package com.smartlogix.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * RF-B02 y RNF-S01: Gestión de Seguridad y Autenticación en el Gateway.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilitado para APIs REST/Microservicios
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**", "/fallback/**").permitAll() // Exponer healthchecks y fallbacks
                .anyExchange().authenticated() // RNF-S01: Rechazar todo tráfico no autenticado
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})); // Validación del token JWT
        
        return http.build();
    }
}
