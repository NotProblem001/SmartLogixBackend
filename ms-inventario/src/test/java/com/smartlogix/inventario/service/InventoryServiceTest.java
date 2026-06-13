package com.smartlogix.inventario.service;

import com.smartlogix.inventario.entity.ProductInventory;
import com.smartlogix.inventario.entity.Warehouse;
import com.smartlogix.inventario.repository.ProductInventoryRepository;
import com.smartlogix.inventario.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private ProductInventoryRepository repository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private ProductInventory inventory;
    private Warehouse warehouse;

    @BeforeEach
    public void setUp() {
        warehouse = new Warehouse("Bodega Central", "Santiago");
        warehouse.setId(1L);

        inventory = new ProductInventory();
        inventory.setId(10L);
        inventory.setProductSku("SKU-01");
        inventory.setAvailableQuantity(15);
        inventory.setReservedQuantity(0);
        inventory.setWarehouse(warehouse);
    }

    @Test
    public void adjustStock_Success() {
        Mockito.when(repository.findByProductSkuAndWarehouseId("SKU-01", 1L))
                .thenReturn(Optional.of(inventory));
        Mockito.when(repository.save(any(ProductInventory.class))).thenReturn(inventory);

        inventoryService.adjustStock("SKU-01", 1L, -5);

        assertEquals(10, inventory.getAvailableQuantity());
        Mockito.verify(repository).save(inventory);
    }

    @Test
    public void adjustStock_InsufficientStock_ThrowsException() {
        Mockito.when(repository.findByProductSkuAndWarehouseId("SKU-01", 1L))
                .thenReturn(Optional.of(inventory));

        assertThrows(RuntimeException.class, () -> inventoryService.adjustStock("SKU-01", 1L, -20));
        Mockito.verify(repository, Mockito.never()).save(any(ProductInventory.class));
    }

    @Test
    public void createStock_Success() {
        Mockito.when(warehouseRepository.findFirstByName("Bodega Central"))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(repository.save(any(ProductInventory.class))).thenReturn(inventory);

        ProductInventory result = inventoryService.createStock(inventory);

        assertNotNull(result);
        assertEquals(warehouse, result.getWarehouse());
        Mockito.verify(repository).save(inventory);
    }
}
