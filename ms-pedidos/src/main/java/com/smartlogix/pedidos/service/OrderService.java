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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrder(Long id, Order updatedData) {
        Order existing = getOrderById(id);
        
        if (updatedData.getStatus() != null) {
            String currentStatus = existing.getStatus();
            String newStatus = updatedData.getStatus();
            
            // Validaciones del ciclo de vida del pedido
            if ("DELIVERED".equals(currentStatus) && !currentStatus.equals(newStatus)) {
                throw new IllegalStateException("No se puede modificar el estado de un pedido ya entregado.");
            }
            if ("CANCELLED".equals(currentStatus) && !currentStatus.equals(newStatus)) {
                throw new IllegalStateException("No se puede modificar un pedido cancelado.");
            }
            
            existing.setStatus(newStatus);
        }
        if (updatedData.getQuantity() != null) {
            String currentStatus = existing.getStatus();
            if ("SHIPPED".equals(currentStatus) || "DELIVERED".equals(currentStatus)) {
                throw new IllegalStateException("No se puede cambiar la cantidad de un pedido enviado o entregado.");
            }
            existing.setQuantity(updatedData.getQuantity());
        }
        return orderRepository.save(existing);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order existing = getOrderById(id);
        orderRepository.delete(existing);
    }
}
