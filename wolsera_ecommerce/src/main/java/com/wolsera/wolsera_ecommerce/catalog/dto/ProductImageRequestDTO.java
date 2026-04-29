package com.wolsera.wolsera_ecommerce.catalog.dto;

public class ProductImageRequestDTO {

    private String imageUrl;
    private boolean isPrimary;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

}

