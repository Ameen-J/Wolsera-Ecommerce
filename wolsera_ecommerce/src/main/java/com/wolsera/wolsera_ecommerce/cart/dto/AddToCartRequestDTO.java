package com.wolsera.wolsera_ecommerce.cart.dto;

public class AddToCartRequestDTO {

    private Long variantId;
    private Integer quantity;

    // ===== GETTERS =====

    public Long getVariantId() {
        return variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // ===== SETTERS =====

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
