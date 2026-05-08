package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.OrderRequestDTO;
import com.smartlogix.pedidos.entity.Order;
import com.smartlogix.pedidos.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody OrderRequestDTO request) {
        return orderService.createOrder(request);
    }
}
