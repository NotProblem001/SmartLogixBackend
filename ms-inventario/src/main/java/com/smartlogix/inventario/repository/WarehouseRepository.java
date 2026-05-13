package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RNF-M03: Desacoplamiento de Datos con Repository Pattern
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findFirstByName(String name);
}
