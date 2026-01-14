package com.ecommerce.product.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating a new category.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 100, message = "Slug must not exceed 100 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String imageUrl;

    private UUID parentId;

    private Integer displayOrder;
}
