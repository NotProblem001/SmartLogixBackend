package com.smartlogix.inventario.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * RF-I02: Sincronización Multibodega
 * Entidad principal para manejar las diferentes bodegas físicas.
 */
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> products;

    public Warehouse() {
    }

    public Warehouse(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ProductInventory> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInventory> products) {
        this.products = products;
    }
}
