package com.smartlogix.pedidos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;
    private final String inventoryUrl;

    public InventoryClient(RestTemplate restTemplate, @Value("${inventory.url:http://localhost:8081}") String inventoryUrl) {
        this.restTemplate = restTemplate;
        this.inventoryUrl = inventoryUrl;
    }

    public void deductStock(String sku, Long warehouseId, Integer quantity) {
        String url = inventoryUrl + "/api/v1/inventory/deduct";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "sku", sku,
                "warehouseId", warehouseId,
                "quantity", quantity
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, Void.class);
    }
}
