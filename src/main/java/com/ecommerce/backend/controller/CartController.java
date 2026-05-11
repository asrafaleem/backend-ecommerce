package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(authentication.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(Authentication authentication,
                                              @RequestBody Map<String, Integer> body) {
        Long productId = Long.valueOf(body.get("productId"));
        Integer quantity = body.get("quantity");
        return ResponseEntity.ok(cartService.addToCart(authentication.getName(), productId, quantity));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(Authentication authentication,
                                                 @PathVariable Long productId) {
        cartService.removeFromCart(authentication.getName(), productId);
        return ResponseEntity.ok("Item removed");
    }
}