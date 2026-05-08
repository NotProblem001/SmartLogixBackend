package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.client.InventoryClient;
import com.smartlogix.pedidos.dto.OrderRequestDTO;
import com.smartlogix.pedidos.entity.Order;
import com.smartlogix.pedidos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Transactional
    public Order createOrder(OrderRequestDTO request) {
        // 1. Descontar stock en ms-inventario de forma sincrónica
        inventoryClient.deductStock(request.getSku(), request.getWarehouseId(), request.getQuantity());

        // 2. Si el descuento fue exitoso, creamos el pedido
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setSku(request.getSku());
        order.setWarehouseId(request.getWarehouseId());
        order.setQuantity(request.getQuantity());
        order.setStatus("APPROVED");

        return orderRepository.save(order);
    }
}
