package com.wolsera.wolsera_ecommerce.catalog.service;

import com.wolsera.wolsera_ecommerce.catalog.model.Category;
import com.wolsera.wolsera_ecommerce.catalog.repository.CategoryRepository;
import com.wolsera.wolsera_ecommerce.catalog.dto.CategoryRequestDTO;
import com.wolsera.wolsera_ecommerce.catalog.dto.CategoryTreeDTO;
import com.wolsera.wolsera_ecommerce.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wolsera.wolsera_ecommerce.catalog.dto.CategoryResponseDTO;
import com.wolsera.wolsera_ecommerce.catalog.mapper.ProductMapper;

import java.util.*;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // ---------------- USER SIDE ----------------

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ---------------- ADMIN / INTERNAL ----------------

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category getByName(String name) {
        return categoryRepository.findByName(name.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Category not found: " + name));
    }

    /**
     * Returns category + all its ancestors
     * Used when linking product → category
     */
    public Set<Category> getAncestors(Category category) {
        Set<Category> result = new HashSet<>();

        Category current = category;
        while (current != null) {
            result.add(current);
            current = current.getParent();
        }

        return result;
    }

    /**
     * Resolves full category chain from category names
     * Used ONLY by ProductService
     */
    public Set<Category> resolveCategories(List<String> categoryNames) {
        Set<Category> result = new HashSet<>();

        for (String name : categoryNames) {
            Category category = getByName(name);
            result.addAll(getAncestors(category));
        }

        return result;
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {

        String normalizedName = dto.getName();

        if (categoryRepository.findByName(normalizedName).isPresent()) {
            throw new RuntimeException("Category already exists");
        }

        Category category = new Category();
        category.setName(normalizedName);

        if (dto.getParentId() != null) {
            Category parent = getById(dto.getParentId());
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);

        return ProductMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryTreeDTO> getCategoryTree() {

        List<Category> categories = categoryRepository.findAll();

        Map<Long, CategoryTreeDTO> map = new HashMap<>();
        List<CategoryTreeDTO> roots = new ArrayList<>();

        // Convert all categories to DTO first
        for (Category category : categories) {
            CategoryTreeDTO dto = new CategoryTreeDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setChildren(new ArrayList<>());
            map.put(category.getId(), dto);
        }

        // Build hierarchy
        for (Category category : categories) {
            CategoryTreeDTO dto = map.get(category.getId());

            if (category.getParent() != null) {
                CategoryTreeDTO parentDto =
                        map.get(category.getParent().getId());
                parentDto.getChildren().add(dto);
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }

    @Transactional
    public CategoryResponseDTO updateCategoryName(Long id, String name) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String normalizedName = name.trim().toLowerCase();

        Optional<Category> existing =
                categoryRepository.findByName(normalizedName);

        if (existing.isPresent() &&
                !existing.get().getId().equals(id)) {
            throw new RuntimeException("Category name already exists");
        }

        category.setName(normalizedName);

        Category updated = categoryRepository.save(category);

        return ProductMapper.toResponse(updated);
    }


    @Transactional
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 1️⃣ Check children
        if (!categoryRepository.findByParentId(id).isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }

        // 2️⃣ Check products
        if (productRepository.existsByCategories_Id(id)) {
            throw new RuntimeException("Cannot delete category assigned to products");
        }

        categoryRepository.delete(category);
    }

}

