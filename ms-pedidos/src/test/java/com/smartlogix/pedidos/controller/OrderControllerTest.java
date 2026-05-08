package com.smartlogix.pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.pedidos.dto.OrderRequestDTO;
import com.smartlogix.pedidos.entity.Order;
import com.smartlogix.pedidos.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createOrder_ValidRequest_ReturnsCreated() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerId("CUST-001");
        dto.setSku("SKU-123");
        dto.setWarehouseId(1L);
        dto.setQuantity(2);

        Order mockedOrder = new Order();
        mockedOrder.setId(1L);
        mockedOrder.setStatus("APPROVED");

        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(mockedOrder);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createOrder_InvalidRequest_ReturnsBadRequest() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO();
        // Missing customerId, sku, etc.
        dto.setQuantity(0); // Invalid quantity (<1)

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
