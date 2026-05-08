package com.smartlogix.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockUpdateRequestDTO {

    @NotBlank(message = "El sku no puede estar vacío")
    private String sku;

    @NotNull(message = "El warehouseId no puede ser nulo")
    private Long warehouseId;

    @NotNull(message = "La cantidad a descontar no puede ser nula")
    private Integer quantity;

    public StockUpdateRequestDTO() {}

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
