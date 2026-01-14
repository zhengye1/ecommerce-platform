package com.ecommerce.product.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for category details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private UUID parentId;
    private String parentName;
    private List<CategorySummary> children;
    private Integer displayOrder;
    private Boolean active;
    private String fullPath;
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
