package com.wolsera.wolsera_ecommerce.catalog.service;

import com.wolsera.wolsera_ecommerce.catalog.dto.*;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import com.wolsera.wolsera_ecommerce.catalog.model.ProductImage;
import com.wolsera.wolsera_ecommerce.catalog.mapper.ProductMapper;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductRepository;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductVariantRepository;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductImageRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;
    private final CategoryService categoryService;

    public AdminProductService(
            ProductRepository productRepository,
            ProductVariantRepository variantRepository,
            ProductImageRepository imageRepository,
            CategoryService categoryService
    ) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.imageRepository = imageRepository;
        this.categoryService = categoryService;
    }

    // ================= PRODUCT =================

    // ---------------- GET PRODUCTS ----------------

    public Page<AdminProductListDTO> getAllForAdmin(Pageable pageable) {
        return productRepository.findAllForAdmin(pageable);
    }
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductForAdmin(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        return ProductMapper.toResponse(product);
    }

    // ---------------- CREATE PRODUCT ----------------
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        Product product = ProductMapper.toEntity(dto);

        // Category resolution delegated
        product.setCategories(
                categoryService.resolveCategories(dto.getCategories())
        );

        // Variants
        List<ProductVariant> variants = dto.getVariants().stream()
                .map(ProductMapper::toVariantEntity)
                .peek(v -> v.setProduct(product))
                .toList();

        product.setVariants(variants);

        // Images
        Set<ProductImage> images = dto.getImages().stream()
                .map(ProductMapper::toImageEntity)
                .peek(img -> img.setProduct(product))
                .collect(Collectors.toSet());

        product.setImages(images);

        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO dto) {
        Product product = getProduct(productId);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setGender(dto.getGender());
        product.setColour(dto.getColour());
        product.setActive(dto.isActive());

        return ProductMapper.toResponse(productRepository.save(product));
    }

    public ProductResponseDTO deactivateProduct(Long productId) {
        Product product = getProduct(productId);
        product.setActive(false);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    public ProductResponseDTO activateProduct(Long productId) {
        Product product = getProduct(productId);
        product.setActive(true);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    // ================= VARIANT =================

    public ProductVariantResponseDTO addVariant(
            Long productId,
            ProductVariantRequestDTO dto
    ) {
        Product product = getProduct(productId);

        if (variantRepository.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        ProductVariant variant = ProductMapper.toVariantEntity(dto);
        variant.setProduct(product);

        return ProductMapper.toVariantResponse(
                variantRepository.save(variant)
        );
    }

    public ProductVariantResponseDTO updateVariant(
            Long variantId,
            ProductVariantRequestDTO dto
    ) {
        ProductVariant variant = getVariant(variantId);

        variant.setSize(dto.getSize());
        variant.setPrice(dto.getPrice());
        variant.setStockQuantity(dto.getStockQuantity());
        variant.setSku(dto.getSku());

        refreshProductActiveStatus(
                variant.getProduct().getId()
        );

        return ProductMapper.toVariantResponse(
                variantRepository.save(variant)
         );
    }
    @Transactional
    public void refreshProductActiveStatus(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean hasStock = variantRepository
                .existsByProductIdAndStockQuantityGreaterThan(productId, 0);

        product.setActive(hasStock);
    }

    public void deleteVariant(Long variantId) {
        ProductVariant variant = getVariant(variantId);
        Product product = variant.getProduct();

        if (product.getVariants().size() <= 1) {
            throw new IllegalStateException(
                    "Product must have at least one variant"
            );
        }
        product.getVariants().remove(variant);
    }

    // ================= IMAGE =================

    public ProductImageResponseDTO addImage(
            Long productId,
            ProductImageRequestDTO dto
    ) {
        Product product = getProduct(productId);

        ProductImage image = ProductMapper.toImageEntity(dto);
        image.setProduct(product);

        if (dto.isPrimary()) {
            unsetPrimaryImage(product);
            image.setPrimary(true);
        }

        return ProductMapper.toImageResponse(
                imageRepository.save(image)
        );
    }

    public ProductImageResponseDTO updateImage(
            Long imageId
    ) {
        ProductImage image = getImage(imageId);

        if (!image.isPrimary()) {
            unsetPrimaryImage(image.getProduct());
            image.setPrimary(true);
        }
        return ProductMapper.toImageResponse(
                imageRepository.save(image)
        );
    }

    public void deleteImage(Long imageId) {
        ProductImage image = getImage(imageId);
        Product product = image.getProduct();

        if(product.getImages().size() <= 1) {
            throw new IllegalStateException(
                    "Product must have at least one image"
            );
        }
        boolean wasPrimary = image.isPrimary();
        product.getImages().remove(image);

        if (wasPrimary) {
            product.getImages().stream()
                    .findFirst()
                    .ifPresent(img -> {
                        img.setPrimary(true);
                    });
        }
    }

    // ================= HELPERS =================

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }

    private ProductVariant getVariant(Long variantId) {
        return variantRepository.findById(variantId)
                .orElseThrow(() ->
                        new RuntimeException("Variant not found"));
    }

    private ProductImage getImage(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() ->
                        new RuntimeException("Image not found"));
    }

    private void unsetPrimaryImage(Product product) {
        product.getImages().forEach(img -> img.setPrimary(false));
    }
}

