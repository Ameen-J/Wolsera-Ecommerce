package com.wolsera.wolsera_ecommerce.order.repository;

import com.wolsera.wolsera_ecommerce.order.model.Order;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT COUNT(oi) > 0
        FROM Order o
        JOIN o.items oi
        WHERE o.user.id = :userId
        AND oi.product.id = :productId
        AND o.status = 'DELIVERED'
        """)
    boolean hasUserPurchasedProduct(Long userId, Long productId);
    List<Order> findByUser(User user);

}