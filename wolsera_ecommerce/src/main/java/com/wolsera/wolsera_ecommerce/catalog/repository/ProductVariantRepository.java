package com.wolsera.wolsera_ecommerce.catalog.repository;

import com.wolsera.wolsera_ecommerce.catalog.model.ProductVariant;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProduct(Product product);

    boolean existsBySku(String sku);
    boolean existsByProductIdAndStockQuantityGreaterThan(Long productId, Integer stockQuantity);


}
