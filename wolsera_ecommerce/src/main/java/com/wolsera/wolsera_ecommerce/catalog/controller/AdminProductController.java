package com.wolsera.wolsera_ecommerce.catalog.controller;

import com.wolsera.wolsera_ecommerce.catalog.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.wolsera.wolsera_ecommerce.catalog.service.AdminProductService;
import com.wolsera.wolsera_ecommerce.catalog.service.CategoryService;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final CategoryService categoryService;

    public AdminProductController(AdminProductService adminProductService,CategoryService categoryService) {
        this.adminProductService = adminProductService;
        this.categoryService = categoryService;
    }

    // ---------------- PRODUCT ----------------

    @GetMapping
    public Page<AdminProductListDTO> getAllProductsForAdmin(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return adminProductService.getAllForAdmin(pageable);
    }
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return adminProductService.getProductForAdmin(id);
    }

    @PostMapping("/create-product")
    public ProductResponseDTO createProduct(
            @RequestBody ProductRequestDTO dto
    ) {
        return adminProductService.createProduct(dto);
    }

    @PutMapping("/{productId}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequestDTO dto
    ) {
        return adminProductService.updateProduct(productId, dto);
    }

    @PatchMapping("/{productId}/activate")
    public ProductResponseDTO activateProduct(@PathVariable Long productId) {
        return adminProductService.activateProduct(productId);
    }

    @PatchMapping("/{productId}/deactivate")
    public ProductResponseDTO deactivateProduct(@PathVariable Long productId) {
        return adminProductService.deactivateProduct(productId);
    }

    // ---------------- VARIANTS ----------------
    @PostMapping("/{productId}/variants")
    public ProductVariantResponseDTO addVariant(
            @PathVariable Long productId,
            @RequestBody ProductVariantRequestDTO dto
    ) {
        return adminProductService.addVariant(productId, dto);
    }

    @PutMapping("/variants/{variantId}")
    public ProductVariantResponseDTO updateVariant(
            @PathVariable Long variantId,
            @RequestBody ProductVariantRequestDTO dto
    ){
        return adminProductService.updateVariant(variantId, dto);
    }

    @DeleteMapping("/variants/{variantId}")
    public void deleteVariant(@PathVariable Long variantId) {
        adminProductService.deleteVariant(variantId);
    }

    // ---------------- IMAGES ----------------
    @PostMapping("/{productId}/images")
    public ProductImageResponseDTO addImage(
            @PathVariable Long productId,
            @RequestBody ProductImageRequestDTO dto
    ) {
        return adminProductService.addImage(productId, dto);
    }

    @DeleteMapping("/images/{imageId}")
    public void deleteImage(@PathVariable Long imageId) {
        adminProductService.deleteImage(imageId);
    }

    // ---------------- CATEGORY ----------------

    @GetMapping("/categories/tree")
    public List<CategoryTreeDTO> getCategoryTree() {
        return categoryService.getCategoryTree();
    }

    @PostMapping("/categories/create-category")
    public CategoryResponseDTO createNewCategory(
            @RequestBody CategoryRequestDTO dto
    ) {
        return categoryService.createCategory(dto);
    }

    @DeleteMapping("/categories/delete/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @PutMapping("/categories/change-name/{id}")
    public CategoryResponseDTO updateCategoryName(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        return categoryService.updateCategoryName(id, name);
    }

}

