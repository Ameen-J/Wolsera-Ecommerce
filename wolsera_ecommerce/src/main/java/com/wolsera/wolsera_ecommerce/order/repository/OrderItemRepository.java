package com.wolsera.wolsera_ecommerce.order.repository;

import com.wolsera.wolsera_ecommerce.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}