package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderRequest;
import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Order placeOrder(String email, OrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setShippingAddress(request.getShippingAddress());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Order> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}