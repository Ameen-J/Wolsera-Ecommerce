package com.wolsera.wolsera_ecommerce.catalog.repository;

import com.wolsera.wolsera_ecommerce.catalog.model.ProductImage;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductOrderByDisplayOrderAsc(Product product);

    boolean existsByProductAndIsPrimaryTrue(Product product);
}
