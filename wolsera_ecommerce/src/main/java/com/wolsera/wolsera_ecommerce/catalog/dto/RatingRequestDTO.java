package com.wolsera.wolsera_ecommerce.catalog.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RatingRequestDTO {

    private Long productId;
    private int rating;

    // Getters & Setters
}