package com.ecommerce.product.application.usecase.impl;

import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.usecase.GetProductUseCase;
import com.ecommerce.product.domain.exception.ProductNotFoundException;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetProductUseCase.
 *
 * TODO: Implement additional business logic:
 * - Check product visibility based on user role
 * - Add caching for frequently accessed products
 * - Track product view analytics (optional)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductUseCaseImpl implements GetProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse getById(UUID id) {
        // TODO: Add access control logic (e.g., check if product is active for non-admin users)

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // TODO: Track product view for analytics (optional)

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getBySku(String sku) {
        // TODO: Add access control logic

        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        return productMapper.toResponse(product);
    }
}
