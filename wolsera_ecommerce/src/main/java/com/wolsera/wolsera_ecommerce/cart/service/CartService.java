package com.wolsera.wolsera_ecommerce.cart.service;

import com.wolsera.wolsera_ecommerce.auth.repository.UserRepository;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.cart.dto.AddToCartRequestDTO;
import com.wolsera.wolsera_ecommerce.cart.dto.CartItemResponseDTO;
import com.wolsera.wolsera_ecommerce.cart.dto.CartResponseDTO;
import com.wolsera.wolsera_ecommerce.cart.model.Cart;
import com.wolsera.wolsera_ecommerce.cart.model.CartItem;
import com.wolsera.wolsera_ecommerce.cart.repository.CartItemRepository;
import com.wolsera.wolsera_ecommerce.cart.repository.CartRepository;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductImage;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductVariantRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.*;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductVariantRepository variantRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.variantRepository = variantRepository;
        this.userRepository = userRepository;
    }

    /* =========================
       GET CART
    ========================== */

    @Transactional(readOnly = true)
    public CartResponseDTO getCart() {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        return mapToCartResponse(cart);
    }

    /* =========================
       ADD ITEM
    ========================== */

    public CartResponseDTO addToCart(AddToCartRequestDTO request) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartAndVariant(cart, variant);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setVariant(variant);
            item.setQuantity(request.getQuantity());
            item.setPriceAtAddition(variant.getPrice());
            cartItemRepository.save(item);
        }

        return mapToCartResponse(cart);
    }

    /* =========================
       UPDATE QUANTITY
    ========================== */

    public CartResponseDTO updateQuantity(Long cartItemId, Integer quantity) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        validateOwnership(cart, item);

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
        }

        return mapToCartResponse(cart);
    }

    /* =========================
       REMOVE ITEM
    ========================== */

    public CartResponseDTO removeItem(Long cartItemId) {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        validateOwnership(cart, item);

        cartItemRepository.delete(item);

        return mapToCartResponse(cart);
    }

    /* =========================
       CLEAR CART
    ========================== */

    public void clearCart() {

        User user = getLoggedInUser();
        Cart cart = getOrCreateCart(user);

        cart.getItems().clear();
    }


    /* =========================
       HELPER METHODS
    ========================== */

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private void validateOwnership(Cart cart, CartItem item) {
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized cart access");
        }
    }

    private CartResponseDTO mapToCartResponse(Cart cart) {

        List<CartItem> items = cartItemRepository.findByCart(cart);

        List<CartItemResponseDTO> itemResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {

            BigDecimal itemTotal =
                    item.getPriceAtAddition()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);

            CartItemResponseDTO response = new CartItemResponseDTO();

            Product product = item.getVariant().getProduct();

            String primaryImageUrl = product.getImages().stream()
                    .filter(ProductImage::isPrimary)
                    .findFirst()
                    .map(ProductImage::getImageUrl)
                    .orElse(null);



            response.setCartItemId(item.getId());
            response.setProductName(item.getVariant().getProduct().getName());
            response.setSize(item.getVariant().getSize());
            response.setColor(item.getVariant().getProduct().getColour());
            response.setQuantity(item.getQuantity());
            response.setImageUrl(primaryImageUrl);
            response.setPrice(item.getPriceAtAddition());
            response.setTotal(itemTotal);

            itemResponses.add(response);
        }

        CartResponseDTO cartResponse = new CartResponseDTO();
        cartResponse.setItems(itemResponses);
        cartResponse.setCartTotal(total);

        return cartResponse;
    }

    private User getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
