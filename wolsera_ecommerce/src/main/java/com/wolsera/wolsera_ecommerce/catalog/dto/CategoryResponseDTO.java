package com.wolsera.wolsera_ecommerce.catalog.dto;

public class CategoryResponseDTO {

    private Long id;
    private String name;
    private Long parentId;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setId(Long id) {
        this.id =  id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
