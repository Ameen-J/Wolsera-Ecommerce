package com.wolsera.wolsera_ecommerce.catalog.mapper;

import com.wolsera.wolsera_ecommerce.catalog.dto.*;
import com.wolsera.wolsera_ecommerce.catalog.model.*;

import java.util.stream.Collectors;
import java.util.List;

public class ProductMapper {

    private ProductMapper() {} // Utility class, no instances

    // ------------------ REQUEST → ENTITY ------------------
    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setGender(dto.getGender());
        product.setColour(dto.getColour());
        // Categories, variants, and images handled in service layer
        return product;
    }
    // ------------------ VARIANT REQUEST → ENTITY ------------------
    public static ProductVariant toVariantEntity(ProductVariantRequestDTO dto) {
        ProductVariant variant = new ProductVariant();
        variant.setSize(dto.getSize());
        variant.setPrice(dto.getPrice());
        variant.setStockQuantity(dto.getStockQuantity());
        variant.setSku(dto.getSku());
        return variant;
    }

    // ------------------ IMAGE REQUEST → ENTITY ------------------
    public static ProductImage toImageEntity(ProductImageRequestDTO dto) {
        ProductImage image = new ProductImage();
        image.setImageUrl(dto.getImageUrl());
        image.setPrimary(dto.isPrimary());
        return image;
    }


    // ------------------ ENTITY → RESPONSE ------------------
    public static ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setGender(product.getGender());
        response.setColour(product.getColour());

        // Categories
        response.setCategories(
                product.getCategories() != null
                        ? product.getCategories()
                        .stream()
                        .map(Category::getName)
                        .toList()
                        : List.of()
        );

        // Variants
        response.setVariants(
                product.getVariants() != null
                        ? product.getVariants()
                        .stream()
                        .map(ProductMapper::toVariantResponse)
                        .collect(Collectors.toList())
                        : null
        );

        // Images
        response.setImages(
                product.getImages() != null
                        ? product.getImages()
                        .stream()
                        .map(ProductMapper::toImageResponse)
                        .collect(Collectors.toList())
                        : null
        );

        return response;
    }

    // ------------------ VARIANT MAPPER ------------------
    public static ProductVariantResponseDTO toVariantResponse(ProductVariant variant) {
        ProductVariantResponseDTO dto = new ProductVariantResponseDTO();
        dto.setId(variant.getId());
        dto.setSize(variant.getSize());
        dto.setPrice(variant.getPrice());
        dto.setStockQuantity(variant.getStockQuantity());
        dto.setSku(variant.getSku());
        return dto;
    }

    // ------------------ IMAGE MAPPER ------------------
    public static ProductImageResponseDTO toImageResponse(ProductImage image) {
        ProductImageResponseDTO dto = new ProductImageResponseDTO();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setPrimary(image.isPrimary());
        return dto;
    }

    public static CategoryResponseDTO toResponse(Category category) {

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }

        return dto;
    }
}
