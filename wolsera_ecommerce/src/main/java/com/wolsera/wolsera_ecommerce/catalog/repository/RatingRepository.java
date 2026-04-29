package com.wolsera.wolsera_ecommerce.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wolsera.wolsera_ecommerce.catalog.model.Rating;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndProductId(Long userId, Long productId);

    List<Rating> findByProductId(Long productId);
}
