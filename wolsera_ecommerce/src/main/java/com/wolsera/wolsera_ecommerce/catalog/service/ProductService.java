package com.wolsera.wolsera_ecommerce.catalog.service;


import com.wolsera.wolsera_ecommerce.catalog.dto.ProductResponseDTO;
import com.wolsera.wolsera_ecommerce.catalog.mapper.ProductMapper;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductListDTO;
import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import com.wolsera.wolsera_ecommerce.catalog.dto.ProductSearchRequestDTO;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.util.*;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductListDTO> getAllActiveProducts(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllActiveProducts(pageable);
    }


    @Transactional(readOnly = true)
    public ProductResponseDTO getActiveProductById(Long id) {

        Product product = productRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        return ProductMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductListDTO> getProductsByCategory(
            Long categoryId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository
                .findDistinctByIsActiveTrueAndCategories_Id(
                        categoryId,
                        pageable
                );
    }

    public Page<ProductListDTO> searchProducts(ProductSearchRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        return productRepository.searchProducts(
                request.getCategoryId(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getSearch(),
                pageable
        );
    }


}

