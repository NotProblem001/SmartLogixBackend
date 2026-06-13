package com.smartlogix.pedidos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita el auditing automático para trazabilidad en JPA.
 * Se separa en una clase de configuración para evitar errores al cargar el
 * contexto de la base de datos en las pruebas slice de controlador (WebMvcTest).
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
