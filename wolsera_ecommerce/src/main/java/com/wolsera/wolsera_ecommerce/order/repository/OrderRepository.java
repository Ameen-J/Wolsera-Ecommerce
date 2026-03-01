package com.wolsera.wolsera_ecommerce.order.repository;

import com.wolsera.wolsera_ecommerce.order.model.Order;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}