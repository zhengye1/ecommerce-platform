package com.ecommerce.product.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.response.PageResponse;
import com.ecommerce.product.application.dto.request.CreateProductRequest;
import com.ecommerce.product.application.dto.request.UpdateProductRequest;
import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.usecase.CreateProductUseCase;
import com.ecommerce.product.application.usecase.GetProductUseCase;
import com.ecommerce.product.application.usecase.ListProductsUseCase;
import com.ecommerce.product.domain.model.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for product management.
 * Delegates to use cases for business logic.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    // TODO: Add UpdateProductUseCase, DeleteProductUseCase, PublishProductUseCase

    @GetMapping
    @Operation(summary = "List all products with pagination")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> listProducts(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String search) {

        Page<ProductResponse> page;
        if (status != null) {
            page = listProductsUseCase.listByStatus(status, pageable);
        } else if (categoryId != null) {
            page = listProductsUseCase.listByCategory(categoryId, pageable);
        } else if (search != null && !search.isBlank()) {
            page = listProductsUseCase.search(search, pageable);
        } else {
            page = listProductsUseCase.list(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        ProductResponse response = getProductUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {
        ProductResponse response = getProductUseCase.getBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        List<ProductResponse> products = listProductsUseCase.getFeatured();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new product", description = "Admin only")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    // TODO: Implement these endpoints after creating corresponding UseCases:
    //
    // @PutMapping("/{id}")
    // public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
    //         @PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request)
    //
    // @PostMapping("/{id}/publish")
    // public ResponseEntity<ApiResponse<ProductResponse>> publishProduct(@PathVariable UUID id)
    //
    // @PostMapping("/{id}/deactivate")
    // public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable UUID id)
    //
    // @DeleteMapping("/{id}")
    // public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id)
}
