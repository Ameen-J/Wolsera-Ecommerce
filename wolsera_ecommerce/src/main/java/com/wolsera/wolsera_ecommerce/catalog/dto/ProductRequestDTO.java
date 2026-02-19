package com.wolsera.wolsera_ecommerce.catalog.dto;

import java.util.List;

public class ProductRequestDTO {

    private String name;
    private String description;
    private String gender;
    private String colour;
    private List<String> categories;
    private List<ProductVariantRequestDTO> variants;
    private List<ProductImageRequestDTO> images;
    private boolean isActive = true;

    // Getters and setters
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

    public List<ProductVariantRequestDTO> getVariants() { return variants; }
    public void setVariants(List<ProductVariantRequestDTO> variants) { this.variants = variants; }

    public List<ProductImageRequestDTO> getImages() { return images; }
    public void setImages(List<ProductImageRequestDTO> images) { this.images = images; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}