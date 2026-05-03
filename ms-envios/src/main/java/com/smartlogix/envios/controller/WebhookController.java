package com.smartlogix.envios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RF-E03: Sincronización vía Webhooks.
 * Endpoints dedicados para recibir notificaciones en tiempo real de los transportistas.
 */
@RestController
@RequestMapping("/api/v1/shipping/webhooks")
public class WebhookController {

    @PostMapping("/carrier-update")
    public ResponseEntity<String> receiveCarrierUpdate(@RequestBody String payload) {
        // En producción: Parsear payload JSON, verificar firma de seguridad y actualizar base de datos
        System.out.println("Webhook recibido con actualización de estado: " + payload);
        return ResponseEntity.ok("ACK");
    }
}
