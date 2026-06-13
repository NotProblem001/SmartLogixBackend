package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.client.InventoryClient;
import com.smartlogix.pedidos.dto.OrderRequestDTO;
import com.smartlogix.pedidos.entity.Order;
import com.smartlogix.pedidos.repository.OrderRepository;
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
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    private OrderRequestDTO orderRequest;
    private Order baseOrder;

    @BeforeEach
    public void setUp() {
        orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerId("CUST-01");
        orderRequest.setSku("SKU-PROD");
        orderRequest.setWarehouseId(1L);
        orderRequest.setQuantity(5);

        baseOrder = new Order();
        baseOrder.setId(1L);
        baseOrder.setCustomerId("CUST-01");
        baseOrder.setSku("SKU-PROD");
        baseOrder.setWarehouseId(1L);
        baseOrder.setQuantity(5);
        baseOrder.setStatus("APPROVED");
    }

    @Test
    public void createOrder_Successful() {
        Mockito.doNothing().when(inventoryClient).deductStock("SKU-PROD", 1L, 5);
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(baseOrder);

        Order result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        assertEquals("SKU-PROD", result.getSku());
        Mockito.verify(inventoryClient).deductStock("SKU-PROD", 1L, 5);
        Mockito.verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void getOrderById_Found() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(baseOrder));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getOrderById_NotFound_ThrowsException() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    public void updateOrder_Delivered_ThrowsException() {
        baseOrder.setStatus("DELIVERED");
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(baseOrder));

        Order updatedData = new Order();
        updatedData.setStatus("PENDING");

        assertThrows(IllegalStateException.class, () -> orderService.updateOrder(1L, updatedData));
    }

    @Test
    public void updateOrder_Cancelled_ThrowsException() {
        baseOrder.setStatus("CANCELLED");
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(baseOrder));

        Order updatedData = new Order();
        updatedData.setStatus("APPROVED");

        assertThrows(IllegalStateException.class, () -> orderService.updateOrder(1L, updatedData));
    }

    @Test
    public void updateOrder_ChangeQuantityOnShipped_ThrowsException() {
        baseOrder.setStatus("SHIPPED");
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(baseOrder));

        Order updatedData = new Order();
        updatedData.setQuantity(10);

        assertThrows(IllegalStateException.class, () -> orderService.updateOrder(1L, updatedData));
    }
}
