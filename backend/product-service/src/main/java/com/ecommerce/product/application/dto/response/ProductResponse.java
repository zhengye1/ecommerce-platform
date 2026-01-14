package com.ecommerce.product.application.dto.response;

import com.ecommerce.product.domain.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for product details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private UUID id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private BigDecimal costPrice;
    private CategorySummary category;
    private String brand;
    private ProductStatus status;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private Boolean featured;
    private BigDecimal weightKg;
    private Set<String> imageUrls;
    private Set<String> tags;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummary {
        private UUID id;
        private String name;
        private String slug;
    }
}
