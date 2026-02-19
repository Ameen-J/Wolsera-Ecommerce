package com.wolsera.wolsera_ecommerce.cart.model;

import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    private Integer quantity;

    private BigDecimal priceAtAddition;

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtAddition() {
        return priceAtAddition;
    }

    // ===== SETTERS =====

    public void setId(Long id) {
        this.id = id;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPriceAtAddition(BigDecimal priceAtAddition) {
        this.priceAtAddition = priceAtAddition;
    }
}

