package com.wolsera.wolsera_ecommerce.catalog.dto;

import java.math.BigDecimal;

public class ProductListDTO {

    private Long id;
    private String name;
    private String thumbnailUrl;
    private BigDecimal minPrice;

    public ProductListDTO(Long id, String name, String thumbnailUrl, BigDecimal minPrice) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.minPrice = minPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }
}
