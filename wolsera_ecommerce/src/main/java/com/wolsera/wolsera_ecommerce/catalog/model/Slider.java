package com.wolsera.wolsera_ecommerce.catalog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "sliders")
public class Slider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String title;

    private String subtitle;

    private String buttonText;

    private String link;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}