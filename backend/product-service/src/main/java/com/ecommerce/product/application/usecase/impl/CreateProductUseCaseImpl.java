package com.ecommerce.product.application.usecase.impl;

import com.ecommerce.product.application.dto.request.CreateProductRequest;
import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.usecase.CreateProductUseCase;
import com.ecommerce.product.domain.exception.CategoryNotFoundException;
import com.ecommerce.product.domain.exception.DuplicateSkuException;
import com.ecommerce.product.domain.model.Category;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.CategoryRepository;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CreateProductUseCase.
 *
 * TODO: Implement the following business logic:
 * - Validate product data (price > 0, SKU format, etc.)
 * - Check category exists if provided
 * - Generate SKU if not provided (optional business rule)
 * - Set initial stock from inventory service (if applicable)
 * - Publish ProductCreatedEvent for downstream services
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse execute(CreateProductRequest request) {
        // TODO: Add custom validation logic here
        // e.g., validate price is positive, SKU format matches pattern, etc.

        // Check for duplicate SKU
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateSkuException(request.getSku());
        }

        // Resolve category if provided
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));
        }

        // Create product using factory method
        Product product = Product.create(
                request.getSku(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                category
        );

        // TODO: Set additional fields from request
        // e.g., compareAtPrice, costPrice, brand, images, tags, etc.

        // TODO: Integrate with inventory service to set initial stock (optional)

        // Save product (domain events will be published automatically via @DomainEvents)
        Product saved = productRepository.save(product);

        // TODO: Publish to SNS/SQS for downstream services if needed beyond domain events

        return productMapper.toResponse(saved);
    }
}
