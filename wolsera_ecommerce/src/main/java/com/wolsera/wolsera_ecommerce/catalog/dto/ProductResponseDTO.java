package com.wolsera.wolsera_ecommerce.catalog.dto;

import java.util.List;
import java.util.Map;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String colour;
    private String gender;
    private List<String> categories; // Just the category name
    private List<ProductVariantResponseDTO> variants;
    private List<ProductImageResponseDTO> images;
    private Double averageRating;
    private Integer totalRatings;
    private Map<Integer, Integer> distribution;
    private boolean isActive;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<ProductVariantResponseDTO> getVariants() { return variants; }
    public void setVariants(List<ProductVariantResponseDTO> variants) { this.variants = variants; }

    public List<ProductImageResponseDTO> getImages() { return images; }
    public void setImages(List<ProductImageResponseDTO> images) { this.images = images; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }

    public Map<Integer, Integer> getDistribution() { return distribution; }
    public void setDistribution(Map<Integer, Integer> distribution) { this.distribution = distribution; }
}
