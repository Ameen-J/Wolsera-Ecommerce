package com.wolsera.wolsera_ecommerce.catalog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter


public class SliderRequestDTO {
    private String imageUrl;
    private String title;
    private String subtitle;
    private String buttonText;
    private Map<String,String> link;
}