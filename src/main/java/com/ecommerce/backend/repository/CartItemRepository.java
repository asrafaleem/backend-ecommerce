package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndProductId(Long userId, Long productId);
}