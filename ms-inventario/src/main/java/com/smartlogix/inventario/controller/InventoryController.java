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
        inventoryService.adjustStock(request.getSku(), request.getWarehouseId(), -request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/synced-stock")
    public ResponseEntity<Iterable<com.smartlogix.inventario.entity.ProductInventory>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    @PostMapping("/create")
    public ResponseEntity<com.smartlogix.inventario.entity.ProductInventory> createStock(
            @Valid @RequestBody com.smartlogix.inventario.entity.ProductInventory inventory) {
        return ResponseEntity.ok(inventoryService.createStock(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.smartlogix.inventario.entity.ProductInventory> updateStock(
            @PathVariable Long id, @RequestBody com.smartlogix.inventario.entity.ProductInventory updatedData) {
        return ResponseEntity.ok(inventoryService.updateStock(id, updatedData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        inventoryService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
