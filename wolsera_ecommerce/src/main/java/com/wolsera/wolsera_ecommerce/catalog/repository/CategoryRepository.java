package com.wolsera.wolsera_ecommerce.catalog.repository;

import com.wolsera.wolsera_ecommerce.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category> findByName(String name);
    List<Category> findByParentId(Long parentId);

}

