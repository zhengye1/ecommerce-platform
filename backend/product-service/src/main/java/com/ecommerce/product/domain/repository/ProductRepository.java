package com.ecommerce.product.domain.repository;

import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Product aggregate.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> findBySku(String sku);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findByFeatured(boolean featured);

    List<Product> findLowStockProducts();

    boolean existsBySku(String sku);

    void deleteById(UUID id);

    long count();
}
