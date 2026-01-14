package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.request.CreateProductRequest;
import com.ecommerce.product.application.dto.response.ProductResponse;

/**
 * Use case for creating a new product.
 */
public interface CreateProductUseCase {

    /**
     * Create a new product.
     *
     * @param request the product creation request
     * @return the created product response
     */
    ProductResponse execute(CreateProductRequest request);
}
