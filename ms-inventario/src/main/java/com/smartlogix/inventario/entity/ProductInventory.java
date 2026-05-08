package com.smartlogix.inventario.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * RF-I01: Gestión de Stock transaccional
 * Mapeo JPA para el inventario de un producto en una bodega específica.
 */
@Entity
@Table(name = "product_inventory", indexes = {
        @Index(name = "idx_product_sku", columnList = "product_sku")
})
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "product_sku")
    private String productSku;

    @Column(nullable = false, name = "available_quantity")
    private Integer availableQuantity;

    @Column(nullable = false, name = "reserved_quantity")
    private Integer reservedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    public ProductInventory() {
    }

    public ProductInventory(String productSku, Integer availableQuantity, Integer reservedQuantity,
            Warehouse warehouse) {
        this.productSku = productSku;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.warehouse = warehouse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
