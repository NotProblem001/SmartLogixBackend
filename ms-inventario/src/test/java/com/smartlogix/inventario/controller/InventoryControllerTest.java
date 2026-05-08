package com.smartlogix.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.inventario.dto.StockUpdateRequestDTO;
import com.smartlogix.inventario.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deductStock_ValidRequest_ReturnsOk() throws Exception {
        StockUpdateRequestDTO dto = new StockUpdateRequestDTO();
        dto.setSku("SKU-123");
        dto.setWarehouseId(1L);
        dto.setQuantity(5);

        Mockito.doNothing().when(inventoryService).adjustStock(anyString(), anyLong(), anyInt());

        mockMvc.perform(post("/api/v1/inventory/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deductStock_InvalidRequest_ReturnsBadRequest() throws Exception {
        StockUpdateRequestDTO dto = new StockUpdateRequestDTO();
        // Missing sku and warehouseId

        mockMvc.perform(post("/api/v1/inventory/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
