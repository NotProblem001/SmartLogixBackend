package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * RNF-M03: Desacoplamiento de Datos con Repository Pattern
 */
@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

    // RNF-R02: Optimización de consulta rápida por SKU y Bodega
    Optional<ProductInventory> findByProductSkuAndWarehouseId(String productSku, Long warehouseId);

    // Consulta de stock a lo largo de todas las bodegas
    List<ProductInventory> findByProductSku(String productSku);
}
