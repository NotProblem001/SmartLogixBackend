package com.smartlogix.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class BffGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffGatewayApplication.class, args);
    }

    /**
     * RF-B03: Endpoint de Fallback.
     * Si el Circuit Breaker detecta la caída de ms-envios, devuelve esta respuesta.
     */
    @GetMapping("/fallback/shipping")
    public Mono<String> shippingFallback() {
        return Mono.just("{\"error\": \"Degradación de servicio\", \"message\": \"El servicio de coordinación de envíos está temporalmente inactivo. Puede continuar usando la plataforma, pero la asignación de transportistas está pausada.\"}");
    }
}
