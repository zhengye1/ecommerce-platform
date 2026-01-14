package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for Product entity.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findByFeatured(boolean featured);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold AND p.status = 'ACTIVE'")
    List<Product> findLowStockProducts();

    boolean existsBySku(String sku);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") UUID id);
}
