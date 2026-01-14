package com.ecommerce.product.domain.exception;

import com.ecommerce.common.constant.ErrorCodes;
import com.ecommerce.common.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 * Exception thrown when a category is not found.
 */
public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(UUID categoryId) {
        super(ErrorCodes.CATEGORY_NOT_FOUND, "Category not found with id: %s", categoryId);
    }

    public CategoryNotFoundException(String slug) {
        super(ErrorCodes.CATEGORY_NOT_FOUND, "Category not found with slug: %s", slug);
    }
}
