package com.ecommerce.product.domain.repository;

import com.ecommerce.product.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Category entity.
 */
public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(UUID id);

    Optional<Category> findBySlug(String slug);

    List<Category> findAll();

    List<Category> findRootCategories();

    List<Category> findByParentId(UUID parentId);

    boolean existsBySlug(String slug);

    void deleteById(UUID id);
}
