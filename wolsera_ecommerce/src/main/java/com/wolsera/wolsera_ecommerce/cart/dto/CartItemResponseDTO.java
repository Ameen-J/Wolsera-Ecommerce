package com.wolsera.wolsera_ecommerce.cart.dto;

import java.math.BigDecimal;

public class CartItemResponseDTO {
    private Long cartItemId;
    private String productName;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;

    // ===== GETTERS =====

    public Long getCartItemId() {
        return cartItemId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    // ===== SETTERS =====

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
