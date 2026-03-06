package com.wolsera.wolsera_ecommerce.catalog.controller;

import com.wolsera.wolsera_ecommerce.catalog.dto.CategoryTreeDTO;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductResponseDTO;
import com.wolsera.wolsera_ecommerce.catalog.service.ProductService;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductSearchRequestDTO;
import com.wolsera.wolsera_ecommerce.catalog.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // ----------------------------------
    // 1️⃣ Get all active products
    // ----------------------------------
    @GetMapping
    public Page<ProductListDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.getAllActiveProducts(page, size);
    }

    // ----------------------------------
    // 2️⃣ Get product by ID (detail page)
    // ----------------------------------
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getActiveProductById(id);
    }

    // ----------------------------------
    // 3️⃣ Search / Filter Products
    // ----------------------------------
    @GetMapping("/search")
    public Page<ProductListDTO> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProductSearchRequestDTO request = new ProductSearchRequestDTO();
        request.setCategoryId(categoryId);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setSearch(search);
        request.setPage(page);
        request.setSize(size);
        return productService.searchProducts(request);
    }

    @GetMapping("/categories/tree")
    public List<CategoryTreeDTO> getCategoryTree() {
        return categoryService.getCategoryTree();
    }
}
