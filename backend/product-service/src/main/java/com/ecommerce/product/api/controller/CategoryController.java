package com.ecommerce.product.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.product.application.dto.request.CreateCategoryRequest;
import com.ecommerce.product.application.dto.response.CategoryResponse;
import com.ecommerce.product.application.mapper.CategoryMapper;
import com.ecommerce.product.domain.exception.CategoryNotFoundException;
import com.ecommerce.product.domain.model.Category;
import com.ecommerce.product.domain.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for category management.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @Operation(summary = "List all categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponseList(categories)));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get category tree (root categories with children)")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponseList(rootCategories)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(category)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException(slug));
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(category)));
    }

    @GetMapping("/{id}/children")
    @Operation(summary = "Get subcategories of a category")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubcategories(@PathVariable UUID id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        List<Category> children = categoryRepository.findByParentId(id);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponseList(children)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new category", description = "Admin only")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {

        Category category = categoryMapper.toEntity(request);
        category.setId(UUID.randomUUID());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getParentId()));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryMapper.toResponse(saved)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a category", description = "Admin only")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());

        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }

        if (request.getParentId() != null && !request.getParentId().equals(category.getId())) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getParentId()));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(saved)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a category", description = "Admin only")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
