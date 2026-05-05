package com.wolsera.wolsera_ecommerce.catalog.dto;

import java.math.BigDecimal;

public class ProductListDTO {

    private Long id;
    private String name;
    private String colour;
    private String gender;
    private String thumbnailUrl;
    private BigDecimal minPrice;
    private String categoryName;
    private Double averageRating;
    private Integer totalRatings;


    public ProductListDTO(Long id, String name,  String colour, String gender, String thumbnailUrl, BigDecimal minPrice
            , String categoryName,  Double averageRating, Integer totalRatings
    ) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.minPrice = minPrice;
        this.colour = colour;
        this.gender = gender;
        this.categoryName = categoryName;
        this.averageRating = averageRating != null ? averageRating : 0.0;
        this.totalRatings = totalRatings != null ? totalRatings : 0;
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

    public String getColour() {
        return colour;
    }

    public String getGender() {
        return gender;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Double getAverageRating() {
        return averageRating;
    }
    public Integer getTotalRatings() {return totalRatings;}
}
