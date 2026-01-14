package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.response.ProductResponse;

import java.util.UUID;

/**
 * Use case for retrieving a product.
 */
public interface GetProductUseCase {

    /**
     * Get product by ID.
     */
    ProductResponse getById(UUID id);

    /**
     * Get product by SKU.
     */
    ProductResponse getBySku(String sku);
}
