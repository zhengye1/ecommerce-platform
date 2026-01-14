package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.model.ProductStatus;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing ProductRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findByIdWithCategory(id);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository.findBySku(sku);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Product> findByCategoryId(UUID categoryId, Pageable pageable) {
        return jpaRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return jpaRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public List<Product> findByFeatured(boolean featured) {
        return jpaRepository.findByFeatured(featured);
    }

    @Override
    public List<Product> findLowStockProducts() {
        return jpaRepository.findLowStockProducts();
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
