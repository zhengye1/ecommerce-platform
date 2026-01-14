package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Category;
import com.ecommerce.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing CategoryRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final JpaCategoryRepository jpaRepository;

    @Override
    public Category save(Category category) {
        return jpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Category> findRootCategories() {
        return jpaRepository.findRootCategories();
    }

    @Override
    public List<Category> findByParentId(UUID parentId) {
        return jpaRepository.findByParentId(parentId);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
