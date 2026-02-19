package com.wolsera.wolsera_ecommerce.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDTO {
    private List<CartItemResponseDTO> items;
    private BigDecimal cartTotal;

    // ===== GETTERS =====

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }

    // ===== SETTERS =====

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }

    public void setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
    }
}
