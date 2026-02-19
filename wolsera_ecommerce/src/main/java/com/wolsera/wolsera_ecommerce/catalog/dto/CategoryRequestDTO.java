package com.wolsera.wolsera_ecommerce.catalog.dto;

public class CategoryRequestDTO {

    private String name;
    private Long parentId; // null → root category

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

