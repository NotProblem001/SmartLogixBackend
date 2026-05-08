package com.smartlogix.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDTO {

    @NotBlank(message = "El customerId no puede estar vacío")
    private String customerId;

    @NotBlank(message = "El sku no puede estar vacío")
    private String sku;

    @NotNull(message = "El warehouseId no puede ser nulo")
    private Long warehouseId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    private Integer quantity;

    public OrderRequestDTO() {}

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
