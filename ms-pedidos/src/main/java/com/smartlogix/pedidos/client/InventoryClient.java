package com.smartlogix.pedidos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@Value("${inventory.url:http://localhost:8081}") String inventoryUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(inventoryUrl)
                .build();
    }

    public void deductStock(String sku, Long warehouseId, Integer quantity) {
        restClient.post()
                .uri("/api/v1/inventory/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "sku", sku,
                        "warehouseId", warehouseId,
                        "quantity", quantity
                ))
                .retrieve()
                .toBodilessEntity();
    }
}
