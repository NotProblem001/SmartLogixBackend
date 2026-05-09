package com.smartlogix.bff.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/shipping")
    public Mono<ResponseEntity<Map<String, Object>>> shippingFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "FALLBACK");
        response.put("message", "El servicio de envíos no está disponible temporalmente. Por favor, inténtelo de nuevo más tarde.");
        response.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }
}
