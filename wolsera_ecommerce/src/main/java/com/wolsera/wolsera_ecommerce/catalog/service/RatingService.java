package com.wolsera.wolsera_ecommerce.catalog.service;

import com.wolsera.wolsera_ecommerce.catalog.model.Product;
import com.wolsera.wolsera_ecommerce.catalog.model.Rating;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductRepository;
import com.wolsera.wolsera_ecommerce.catalog.repository.RatingRepository;
import com.wolsera.wolsera_ecommerce.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public RatingService(RatingRepository ratingRepository,
                         ProductRepository productRepository,
                         OrderRepository orderRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void rateProduct(Long userId, Long productId, int newRating) {

        // 1. Validate
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // 2. Product check
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Purchase check (adjust based on your schema)
        boolean hasPurchased = orderRepository.hasUserPurchasedProduct(userId, productId);
        if (!hasPurchased) {
            throw new RuntimeException("You can only rate purchased products");
        }

        // 4. Existing rating?
        Optional<Rating> existingRatingOpt =
                ratingRepository.findByUserIdAndProductId(userId, productId);

        if (existingRatingOpt.isEmpty()) {
            handleNewRating(userId, product, newRating);
        } else {
            handleUpdateRating(existingRatingOpt.get(), product, newRating);
        }
    }

    private void handleNewRating(Long userId, Product product, int newRating) {

        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setProductId(product.getId());
        rating.setRating(newRating);
        rating.setCreatedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        updateAverageOnNew(product, newRating);
    }

    private void handleUpdateRating(Rating existing, Product product, int newRating) {

        int oldRating = existing.getRating();
        existing.setRating(newRating);

        ratingRepository.save(existing);


        updateAverageOnUpdate(product, oldRating, newRating);
    }

    private void updateAverageOnNew(Product product, int newRating) {

        double currentAvg = product.getAverageRating() == null ? 0 : product.getAverageRating();
        int total = product.getTotalRatings() == null ? 0 : product.getTotalRatings();

        double newAvg = ((currentAvg * total) + newRating) / (total + 1);

        product.setAverageRating(newAvg);
        product.setTotalRatings(total + 1);

        incrementStarCount(product, newRating);

        productRepository.save(product);
    }

    private void updateAverageOnUpdate(Product product, int oldRating, int newRating) {

        double currentAvg = product.getAverageRating();
        int total = product.getTotalRatings();

        double newAvg = ((currentAvg * total) - oldRating + newRating) / total;

        decrementStarCount(product, oldRating);
        incrementStarCount(product, newRating);

        product.setAverageRating(newAvg);
        productRepository.save(product);
    }

    private void incrementStarCount(Product product, int rating) {
        switch (rating) {
            case 5 -> product.setFiveStarCount(product.getFiveStarCount() + 1);
            case 4 -> product.setFourStarCount(product.getFourStarCount() + 1);
            case 3 -> product.setThreeStarCount(product.getThreeStarCount() + 1);
            case 2 -> product.setTwoStarCount(product.getTwoStarCount() + 1);
            case 1 -> product.setOneStarCount(product.getOneStarCount() + 1);
        }
    }

    private void decrementStarCount(Product product, int rating) {
        switch (rating) {
            case 5 -> product.setFiveStarCount(product.getFiveStarCount() - 1);
            case 4 -> product.setFourStarCount(product.getFourStarCount() - 1);
            case 3 -> product.setThreeStarCount(product.getThreeStarCount() - 1);
            case 2 -> product.setTwoStarCount(product.getTwoStarCount() - 1);
            case 1 -> product.setOneStarCount(product.getOneStarCount() - 1);
        }
    }
}