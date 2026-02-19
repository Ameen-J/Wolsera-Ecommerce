package com.wolsera.wolsera_ecommerce.order.dto;

import java.math.BigDecimal;

public class OrderItemResponseDTO {
    private Long id;
    private String productName;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;

    // Constructors
    public OrderItemResponseDTO() {}

    public OrderItemResponseDTO(Long id, String productName, String size, String color, Integer quantity, BigDecimal price, BigDecimal total) {
        this.id = id;
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
