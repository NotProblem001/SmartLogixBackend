package com.smartlogix.inventario.controller;

import com.smartlogix.inventario.service.InventoryService;
import com.smartlogix.inventario.dto.StockUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{sku}/warehouse/{warehouseId}/adjust")
    public ResponseEntity<String> adjustStock(
            @PathVariable String sku,
            @PathVariable Long warehouseId,
            @RequestParam int quantityChange) {
        
        inventoryService.adjustStock(sku, warehouseId, quantityChange);
        return ResponseEntity.ok("Stock actualizado exitosamente para el SKU: " + sku);
    }

    @PostMapping("/deduct")
    public ResponseEntity<Void> deductStock(@Valid @RequestBody StockUpdateRequestDTO request) {
        // En adjustStock el quantityChange debe ser negativo para descontar
        inventoryService.adjustStock(request.getSku(), request.getWarehouseId(), -request.getQuantity());
        return ResponseEntity.ok().build();
    }
}
