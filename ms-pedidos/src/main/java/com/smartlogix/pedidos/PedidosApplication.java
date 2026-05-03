package com.smartlogix.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Microservicio de Pedidos.
 * RNF-M03: Contiene su propia base de datos.
 */
@SpringBootApplication
@EnableJpaAuditing // RF-P02: Habilita el auditing automático para trazabilidad
public class PedidosApplication {
    public static void main(String[] args) {
        SpringApplication.run(PedidosApplication.class, args);
    }
}
