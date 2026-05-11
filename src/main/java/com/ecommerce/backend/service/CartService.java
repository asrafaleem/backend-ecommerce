package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartItemRepository.findByUserId(user.getId());
    }

    public CartItem addToCart(String email, Long productId, Integer quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<CartItem> existing = cartItemRepository.findByUserId(user.getId());
        for (CartItem item : existing) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartItemRepository.save(item);
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> items = cartItemRepository.findByUserId(user.getId());
        cartItemRepository.deleteAll(items);
    }
}