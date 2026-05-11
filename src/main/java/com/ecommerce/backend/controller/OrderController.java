package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderRequest;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(Authentication authentication,
                                            @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(authentication.getName(), request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getMyOrders(authentication.getName()));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long orderId,
                                              @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, body.get("status")));
    }
}