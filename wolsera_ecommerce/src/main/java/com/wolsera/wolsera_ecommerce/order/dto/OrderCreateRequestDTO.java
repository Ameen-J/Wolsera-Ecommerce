package com.wolsera.wolsera_ecommerce.order.dto;

public class OrderCreateRequestDTO {
    private Long userId;
    private String shippingAddress;

    // Constructors
    public OrderCreateRequestDTO() {}

    public OrderCreateRequestDTO(Long userId, String shippingAddress) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
