package com.wolsera.wolsera_ecommerce.cart.controller;

import com.wolsera.wolsera_ecommerce.auth.service.UserService;
import com.wolsera.wolsera_ecommerce.cart.dto.AddToCartRequestDTO;
import com.wolsera.wolsera_ecommerce.cart.dto.CartResponseDTO;
import com.wolsera.wolsera_ecommerce.cart.service.CartService;


import com.wolsera.wolsera_ecommerce.order.dto.OrderCreateRequestDTO;
import com.wolsera.wolsera_ecommerce.order.dto.OrderResponseDTO;
import com.wolsera.wolsera_ecommerce.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    public CartController(CartService cartService,UserService userService, OrderService orderService) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {

        CartResponseDTO cart = cartService.getCart();

        return ResponseEntity.ok(cart);
    }

    // 2️⃣ Add Item
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addItem(
            @RequestBody AddToCartRequestDTO dto) {

        CartResponseDTO cart = cartService.addToCart(dto);

        return ResponseEntity.ok(cart);
    }

    // 3️⃣ Update Quantity
    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateItem(
            @RequestParam Long cartItemId,
            @RequestParam Integer quantity) {

        CartResponseDTO cart =
                cartService.updateQuantity(cartItemId, quantity);

        return ResponseEntity.ok(cart);
    }

    // 4️⃣ Remove Item
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable Long cartItemId) {

        CartResponseDTO cart =
                cartService.removeItem(cartItemId);

        return ResponseEntity.ok(cart);
    }

    // 5️⃣ Clear Cart
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@RequestBody OrderCreateRequestDTO requestDTO) {
        OrderResponseDTO orderResponse = orderService.createOrderFromCart(requestDTO);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
