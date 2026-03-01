package com.wolsera.wolsera_ecommerce.order.service;

import com.wolsera.wolsera_ecommerce.cart.model.Cart;
import com.wolsera.wolsera_ecommerce.cart.model.CartItem;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import com.wolsera.wolsera_ecommerce.cart.repository.CartRepository;
import com.wolsera.wolsera_ecommerce.order.dto.OrderCreateRequestDTO;
import com.wolsera.wolsera_ecommerce.order.dto.OrderItemResponseDTO;
import com.wolsera.wolsera_ecommerce.order.dto.OrderResponseDTO;
import com.wolsera.wolsera_ecommerce.order.model.Order;
import com.wolsera.wolsera_ecommerce.order.model.OrderItem;
import com.wolsera.wolsera_ecommerce.order.mapper.OrderMapper;
import com.wolsera.wolsera_ecommerce.order.repository.OrderRepository;
import com.wolsera.wolsera_ecommerce.order.repository.OrderItemRepository;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductVariantRepository;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariantRepository variantRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository,
                        OrderItemRepository orderItemRepository,
                        ProductVariantRepository variantRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.variantRepository = variantRepository;
    }

    /**
     * Create an order from the user's cart
     */
    @Transactional
    public OrderResponseDTO createOrderFromCart(OrderCreateRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        //  Validate stock for all items first
        List<String> outOfStockMessages = cart.getItems().stream()
                .filter(cartItem -> cartItem.getQuantity() > cartItem.getVariant().getStockQuantity())
                .map(cartItem -> {
                    ProductVariant variant = cartItem.getVariant();
                    return "Not enough stock for SKU: " + variant.getSku()
                            + " (requested: " + cartItem.getQuantity()
                            + ", available: " + variant.getStockQuantity() + ")";
                })
                .toList();

        if (!outOfStockMessages.isEmpty()) {
            throw new RuntimeException(String.join("; ", outOfStockMessages));
        }

        //  All items have enough stock → reduce stock and create order items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .toList();

        //  Create and save order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(requestDTO.getShippingAddress());
        order.setItems(orderItems); // totalAmount calculated in Order entity

        Order savedOrder = orderRepository.save(order);

        //   Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toDto(savedOrder);
    }


    /**
     * Get all orders for a user
     */
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get orderItem by ID
     */
    public OrderItemResponseDTO getOrderItemById(Long orderItemId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        return OrderMapper.toDto(item);
    }
    /**
     * Get order by ID
     */
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toDto(order);
    }
    /**
     * Get all orders
     */
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Convert a CartItem → OrderItem snapshot
     */
    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        ProductVariant variant = cartItem.getVariant();

        // Reduce stock
        variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
        variantRepository.save(variant);

        // Create order item snapshot
        OrderItem item = new OrderItem();
        item.setProductName(variant.getProduct().getName());
        item.setSize(variant.getSize());
        item.setColor(variant.getProduct().getColour()); // if color is stored in product
        item.setQuantity(cartItem.getQuantity());
        item.setPrice(variant.getPrice());
        item.calculateTotal();

        return item;
    }
}

