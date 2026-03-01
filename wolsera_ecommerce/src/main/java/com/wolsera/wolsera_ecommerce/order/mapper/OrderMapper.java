package com.wolsera.wolsera_ecommerce.order.mapper;

import com.wolsera.wolsera_ecommerce.order.dto.OrderItemResponseDTO;
import com.wolsera.wolsera_ecommerce.order.dto.OrderResponseDTO;
import com.wolsera.wolsera_ecommerce.order.model.Order;
import com.wolsera.wolsera_ecommerce.order.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    // Convert OrderItem entity → OrderItemResponseDto
    public static OrderItemResponseDTO toDto(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getId(),
                item.getProductName(),
                item.getSize(),
                item.getColor(),
                item.getQuantity(),
                item.getPrice(),
                item.getTotal()
        );
    }

    // Convert Order entity → OrderResponseDto
    public static OrderResponseDTO toDto(Order order) {
        List<OrderItemResponseDTO> items = order.getItems()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                order.getShippingAddress(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}