package com.wolsera.wolsera_ecommerce.catalog.repository;

import com.wolsera.wolsera_ecommerce.catalog.dto.AdminProductListDTO;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO;


import java.math.BigDecimal;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
    SELECT new com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO(
        p.id,
        p.name,
        p.colour,
        p.gender,
        (
            SELECT i.imageUrl
            FROM ProductImage i
            WHERE i.product = p
              AND i.isPrimary = true
        ),
        (
            SELECT MIN(v.price)
            FROM ProductVariant v
            WHERE v.product = p
        ),
        leaf.name,
        p.averageRating,
        p.totalRating
    )
    FROM Product p
    LEFT JOIN p.categories leaf
    WHERE p.isActive = true
    AND NOT EXISTS (
        SELECT 1 FROM Category c2
        WHERE c2.parent = leaf
    )
    """)
    Page<ProductListDTO> findAllActiveProducts(Pageable pageable);


    /*@EntityGraph(attributePaths = {
            "variants",
            "images",
            "categories"
    })*/
    Optional<Product> findByIdAndIsActiveTrue(Long id);

    /*
    @Query("""
    SELECT new com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO(
        p.id,
        p.name,
        p.colour,
        p.gender,
        (
            SELECT i.imageUrl
            FROM ProductImage i
            WHERE i.product = p
              AND i.isPrimary = true
        ),
        (
            SELECT MIN(v.price)
            FROM ProductVariant v
            WHERE v.product = p
        ),
        (
            SELECT c.name
            FROM Category c
            WHERE c MEMBER OF p.categories
            AND c.children IS EMPTY
        )
    )
    FROM Product p
    JOIN p.categories c
    WHERE p.isActive = true
      AND c.id = :categoryId
    """)
    Page<ProductListDTO> findDistinctByIsActiveTrueAndCategories_Id(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
    */
    @Query("""
    SELECT DISTINCT new com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO(
        p.id,
        p.name,
        p.colour,
        p.gender,
        (
            SELECT img.imageUrl
            FROM ProductImage img
            WHERE img.product = p
            AND img.isPrimary = true
        ),
        (
            SELECT MIN(v.price)
            FROM ProductVariant v
            WHERE v.product = p
        ),
        leaf.name,
        p.averageRating,
        p.totalRating
    )
    FROM Product p
    LEFT JOIN p.categories c
    LEFT JOIN p.categories leaf
    WHERE p.isActive = true
    AND NOT EXISTS (
        SELECT 1 FROM Category c2
        WHERE c2.parent = leaf
    )
    AND (:categoryId IS NULL OR c.id = :categoryId)
    AND (
        :search IS NULL OR
        LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    AND (
        :minPrice IS NULL OR
        (
            SELECT MIN(v.price)
            FROM ProductVariant v
            WHERE v.product = p
        ) >= :minPrice
    )
    AND (
        :maxPrice IS NULL OR
        (
            SELECT MIN(v.price)
            FROM ProductVariant v
            WHERE v.product = p
        ) <= :maxPrice
    )
    """)
    Page<ProductListDTO> searchProducts(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("search") String search,
            Pageable pageable
    );

    boolean existsByCategories_Id(Long categoryId);

    @Query("""
    SELECT new com.wolsera.wolsera_ecommerce.catalog.dto.AdminProductListDTO(
        p.id,
        p.name,
        p.gender,
        p.colour,
        p.isActive
    )
    FROM Product p
    """)
    Page<AdminProductListDTO> findAllForAdmin(Pageable pageable);
}
