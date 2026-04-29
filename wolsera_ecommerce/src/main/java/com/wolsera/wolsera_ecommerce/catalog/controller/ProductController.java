package com.wolsera.wolsera_ecommerce.catalog.controller;

import com.wolsera.wolsera_ecommerce.catalog.dto.*;
import com.wolsera.wolsera_ecommerce.catalog.model.Rating;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.catalog.repository.RatingRepository;
import com.wolsera.wolsera_ecommerce.catalog.service.ProductService;
import com.wolsera.wolsera_ecommerce.catalog.service.CategoryService;
import com.wolsera.wolsera_ecommerce.catalog.service.RatingService;
import com.wolsera.wolsera_ecommerce.catalog.service.SliderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final RatingService ratingService;
    private final SliderService sliderService;
    private final RatingRepository ratingRepository;

    public ProductController(ProductService productService, CategoryService categoryService,RatingService ratingService,
                             SliderService sliderService, RatingRepository ratingRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.ratingService = ratingService;
        this.ratingRepository = ratingRepository;
        this.sliderService = sliderService;
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

    @PostMapping("/ratings")
    public ResponseEntity<String> rateProduct(@RequestBody RatingRequestDTO request,
                                              @AuthenticationPrincipal User user) {

        ratingService.rateProduct(user.getId(), request.getProductId(), request.getRating());

        return ResponseEntity.ok("Rating submitted successfully");
    }

    @GetMapping("/ratings/my/{productId}")
    public ResponseEntity<Integer> getMyRating(@PathVariable Long productId,
                                               @AuthenticationPrincipal User user) {

        Optional<Rating> rating =
                ratingRepository.findByUserIdAndProductId(user.getId(), productId);

        return ResponseEntity.ok(rating.map(Rating::getRating).orElse(0));
    }

    @GetMapping("/sliders")
    public ResponseEntity<List<SliderResponseDTO>> getSliders() {
        return ResponseEntity.ok(sliderService.getSliders());
    }

}
