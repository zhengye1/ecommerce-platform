package com.ecommerce.product.application.usecase.impl;

import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.usecase.ListProductsUseCase;
import com.ecommerce.product.domain.model.ProductStatus;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of ListProductsUseCase.
 *
 * TODO: Implement additional business logic:
 * - Filter out inactive products for non-admin users
 * - Add Redis caching for product listings
 * - Implement Elasticsearch for full-text search (optional)
 * - Add personalization based on user preferences (optional)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListProductsUseCaseImpl implements ListProductsUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> list(Pageable pageable) {
        // TODO: Add caching
        // TODO: Filter by status=ACTIVE for non-admin users
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> listByStatus(ProductStatus status, Pageable pageable) {
        // TODO: Validate user has permission to view products with this status
        return productRepository.findByStatus(status, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> listByCategory(UUID categoryId, Pageable pageable) {
        // TODO: Validate category exists
        // TODO: Include products from child categories (optional)
        return productRepository.findByCategoryId(categoryId, pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> search(String query, Pageable pageable) {
        // TODO: Implement full-text search with Elasticsearch
        // TODO: Add search analytics tracking
        return productRepository.findByNameContainingIgnoreCase(query.trim(), pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public List<ProductResponse> getFeatured() {
        // TODO: Add Redis caching for featured products (high traffic endpoint)
        // TODO: Limit results and order by some criteria (e.g., popularity)
        return productMapper.toResponseList(productRepository.findByFeatured(true));
    }
}
