package com.wolsera.wolsera_ecommerce.catalog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(
        name = "ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int rating;

    private LocalDateTime createdAt;
}
