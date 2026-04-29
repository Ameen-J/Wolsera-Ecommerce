package com.wolsera.wolsera_ecommerce.catalog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private String gender;
    private String colour;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<ProductVariant> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    private Set <ProductImage> images;

    private boolean isActive = true;
    private Double averageRating;
    private Integer totalRatings;
    private Integer fiveStarCount = 0;
    private Integer fourStarCount = 0;
    private Integer threeStarCount = 0;
    private Integer twoStarCount = 0;
    private Integer oneStarCount = 0;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> categories) { this.categories = categories; }

    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }

    public Set<ProductImage> getImages() { return images; }
    public void setImages(Set<ProductImage> images) { this.images = images; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public  void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }

    public Integer getFiveStarCount() { return fiveStarCount; }
    public void setFiveStarCount(Integer fiveStarCount) { this.fiveStarCount = fiveStarCount; }

    public Integer getFourStarCount() { return fourStarCount; }
    public void setFourStarCount(Integer fourStarCount) { this.fourStarCount = fourStarCount; }

    public Integer getThreeStarCount() { return threeStarCount; }
    public void setThreeStarCount(Integer threeStarCount) { this.threeStarCount = threeStarCount; }

    public Integer getTwoStarCount() { return twoStarCount; }
    public void setTwoStarCount(Integer twoStarCount) { this.twoStarCount = twoStarCount; }

    public Integer getOneStarCount() { return oneStarCount; }
    public void setOneStarCount(Integer oneStarCount) { this.oneStarCount = oneStarCount; }

}
