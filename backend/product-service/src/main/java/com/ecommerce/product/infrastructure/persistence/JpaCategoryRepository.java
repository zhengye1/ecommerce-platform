package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for Category entity.
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.displayOrder")
    List<Category> findRootCategories();

    List<Category> findByParentId(UUID parentId);

    boolean existsBySlug(String slug);
}
