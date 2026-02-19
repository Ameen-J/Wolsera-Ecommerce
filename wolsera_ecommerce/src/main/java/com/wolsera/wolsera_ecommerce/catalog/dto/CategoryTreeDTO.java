package com.wolsera.wolsera_ecommerce.catalog.dto;

import java.util.List;

public class CategoryTreeDTO {

    private Long id;
    private String name;
    private List<CategoryTreeDTO> children;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CategoryTreeDTO> getChildren() {
        return children;
    }

    public void setId(Long id) {
        this.id =  id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<CategoryTreeDTO> children) {
        this.children = children;
    }
}
