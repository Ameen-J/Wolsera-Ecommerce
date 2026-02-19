package com.wolsera.wolsera_ecommerce.cart.repository;

import com.wolsera.wolsera_ecommerce.cart.model.Cart;
import com.wolsera.wolsera_ecommerce.cart.model.CartItem;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndVariant(Cart cart, ProductVariant variant);

    List<CartItem> findByCart(Cart cart);

    void deleteByCartAndId(Cart cart, Long id);
}

