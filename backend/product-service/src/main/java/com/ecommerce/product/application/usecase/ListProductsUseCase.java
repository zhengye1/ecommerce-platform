package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Use case for listing products.
 */
public interface ListProductsUseCase {

    /**
     * List all products with pagination.
     */
    Page<ProductResponse> list(Pageable pageable);

    /**
     * List products by status.
     */
    Page<ProductResponse> listByStatus(ProductStatus status, Pageable pageable);

    /**
     * List products by category.
     */
    Page<ProductResponse> listByCategory(UUID categoryId, Pageable pageable);

    /**
     * Search products by name.
     */
    Page<ProductResponse> search(String query, Pageable pageable);

    /**
     * Get featured products.
     */
    List<ProductResponse> getFeatured();
}
