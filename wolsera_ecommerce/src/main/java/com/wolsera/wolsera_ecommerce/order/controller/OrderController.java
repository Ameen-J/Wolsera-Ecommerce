package com.wolsera.wolsera_ecommerce.order.controller;

import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.repository.UserRepository;
import com.wolsera.wolsera_ecommerce.order.dto.OrderResponseDTO;
import com.wolsera.wolsera_ecommerce.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    /**
     * Get all orders for a specific user
     */
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found"));
        List<OrderResponseDTO> orders = orderService.getOrdersByUser(user.getId());
        return ResponseEntity.ok(orders);
    }

    /**
     * Get a single order by its ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    /**
     * Get all orders (for admin / internal use)
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
