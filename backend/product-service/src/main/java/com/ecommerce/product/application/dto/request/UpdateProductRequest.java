package com.ecommerce.product.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * Request DTO for updating a product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "Compare at price must be greater than 0")
    private BigDecimal compareAtPrice;

    @DecimalMin(value = "0.00", message = "Cost price must be non-negative")
    private BigDecimal costPrice;

    private UUID categoryId;

    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @Min(value = 0, message = "Low stock threshold must be non-negative")
    private Integer lowStockThreshold;

    private Boolean featured;

    @DecimalMin(value = "0.00", message = "Weight must be non-negative")
    private BigDecimal weightKg;

    private Set<String> imageUrls;

    private Set<String> tags;
}
