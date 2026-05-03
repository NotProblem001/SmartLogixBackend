package com.smartlogix.inventario.service;

import com.smartlogix.inventario.entity.ProductInventory;
import com.smartlogix.inventario.repository.ProductInventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de Inventario (RF-I01).
 * Encapsula la lógica de negocio y transaccionalidad.
 */
@Service
public class InventoryService {

    private final ProductInventoryRepository repository;

    public InventoryService(ProductInventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void adjustStock(String sku, Long warehouseId, int quantityChange) {
        ProductInventory inventory = repository.findByProductSkuAndWarehouseId(sku, warehouseId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para SKU: " + sku + " en bodega: " + warehouseId));
        
        int newQuantity = inventory.getAvailableQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new RuntimeException("Stock insuficiente para procesar la orden del SKU: " + sku);
        }
        
        inventory.setAvailableQuantity(newQuantity);
        repository.save(inventory); // Hibernate actualiza el timestamp automáticamente (@PreUpdate)
    }
}
