package com.wolsera.wolsera_ecommerce.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductListDTO {
    private Long id;
    private String name;
    private String gender;
    private String colour;
    private boolean active;
}