package com.ecommerce.product.domain.model;

import com.ecommerce.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Product category entity.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Create a new root category.
     */
    public static Category createRoot(String name, String slug, String description) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .slug(slug)
                .description(description)
                .active(true)
                .build();
    }

    /**
     * Create a subcategory.
     */
    public Category createSubcategory(String name, String slug, String description) {
        Category subcategory = Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .slug(slug)
                .description(description)
                .parent(this)
                .active(true)
                .build();
        this.children.add(subcategory);
        return subcategory;
    }

    /**
     * Check if this is a root category.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Get the full path from root to this category.
     */
    public String getFullPath() {
        if (parent == null) {
            return slug;
        }
        return parent.getFullPath() + "/" + slug;
    }
}
