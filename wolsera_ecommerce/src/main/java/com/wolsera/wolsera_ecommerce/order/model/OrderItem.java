package com.wolsera.wolsera_ecommerce.order.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each item belongs to one order
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    // Snapshot of product at purchase time
    @Column(nullable = false)
    private String productName;

    private String size; // optional
    private String color; // optional

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price; // unit price at time of order

    @Column(nullable = false)
    private BigDecimal total; // price * quantity

    // Constructors
    public OrderItem() {}

    public OrderItem(String productName, String size, String color, Integer quantity, BigDecimal price, String imageUrl) {
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
        calculateTotal();
    }

    // Calculate total based on price and quantity
    public void calculateTotal() {
        if (price != null && quantity != null) {
            this.total = price.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.total = BigDecimal.ZERO;
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public  String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) {
        this.price = price;
        calculateTotal();
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
