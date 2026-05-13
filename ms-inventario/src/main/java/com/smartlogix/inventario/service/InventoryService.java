package com.smartlogix.inventario.service;

import com.smartlogix.inventario.entity.ProductInventory;
import com.smartlogix.inventario.entity.Warehouse;
import com.smartlogix.inventario.repository.ProductInventoryRepository;
import com.smartlogix.inventario.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de Inventario (RF-I01).
 * Encapsula la lógica de negocio y transaccionalidad.
 */
@Service
public class InventoryService {

    private final ProductInventoryRepository repository;
    private final WarehouseRepository warehouseRepository;

    public InventoryService(ProductInventoryRepository repository, WarehouseRepository warehouseRepository) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
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

    public Iterable<ProductInventory> getAllStock() {
        return repository.findAll();
    }

    @Transactional
    public ProductInventory createStock(ProductInventory inventory) {
        // Asegurarse de que exista una bodega para insertar la demo
        Warehouse warehouse = warehouseRepository.findByName("Bodega Central").orElseGet(() -> {
            Warehouse newWarehouse = new Warehouse("Bodega Central", "Santiago");
            return warehouseRepository.save(newWarehouse);
        });
        
        inventory.setWarehouse(warehouse);
        if (inventory.getReservedQuantity() == null) {
            inventory.setReservedQuantity(0);
        }
        return repository.save(inventory);
    }

    @Transactional
    public ProductInventory updateStock(Long id, ProductInventory updatedData) {
        ProductInventory existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (updatedData.getProductSku() != null) {
            existing.setProductSku(updatedData.getProductSku());
        }
        if (updatedData.getAvailableQuantity() != null) {
            existing.setAvailableQuantity(updatedData.getAvailableQuantity());
        }
        return repository.save(existing);
    }

    @Transactional
    public void deleteStock(Long id) {
        repository.deleteById(id);
    }
}
